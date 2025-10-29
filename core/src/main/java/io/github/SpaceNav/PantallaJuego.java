package io.github.SpaceNav;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class PantallaJuego implements Screen {
	
	public static final float WORLD_WIDTH = 800;
	public static final float WORLD_HEIGHT = 640;

	private SpaceNavigation game;
	private OrthographicCamera camera;	
	private SpriteBatch batch;
	private Sound explosionSound;
	private Music gameMusic;
	private int score;
	private int ronda;
	private int velXAsteroides; 
	private int velYAsteroides; 
	private int cantAsteroides;
	
	private NaveJugador nave;
	private ArrayList<NaveEnemiga> enemigos = new ArrayList<>();
	private ArrayList<Bullet> balas = new ArrayList<>();

    private Texture texturaNaveJugador;
    private Texture texturaNaveEnemiga;
    private Texture texturaBalaJugador;
    private Texture texturaFondo;


	public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,  
			int velXAsteroides, int velYAsteroides, int cantAsteroides) {
		this.game = game;
		this.ronda = ronda;
		this.score = score;
		this.velXAsteroides = velXAsteroides;
		this.velYAsteroides = velYAsteroides;
		this.cantAsteroides = cantAsteroides;
		
		batch = game.getBatch();
		camera = new OrthographicCamera();	
		
		camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
		
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
		explosionSound.setVolume(1,0.5f);
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav")); 
		
		gameMusic.setLooping(true);
		gameMusic.setVolume(0.5f);
		gameMusic.play();
		
        texturaFondo = new Texture(Gdx.files.internal("sala.png")); 
        texturaNaveJugador = new Texture(Gdx.files.internal("gato1.png")); 
        texturaNaveEnemiga = new Texture(Gdx.files.internal("secador.png")); 
        texturaBalaJugador = new Texture(Gdx.files.internal("bolaPelo.png")); 
        
	    nave = new NaveJugador(30, WORLD_HEIGHT/2-50, texturaNaveJugador,
	    				Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
	    				texturaBalaJugador,
	    				Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"))); 
        nave.setVidas(vidas);
        
        Random r = new Random();
	    for (int i = 0; i < cantAsteroides; i++) {
	        NaveEnemiga enemigo = new NaveEnemiga(texturaNaveEnemiga,
	    			WORLD_WIDTH - 50, // Posición X
	  	            r.nextInt((int)WORLD_HEIGHT), // Posición Y
	  	            -(velXAsteroides+r.nextInt(4)), 
	  	            velYAsteroides+r.nextInt(4),
	  	            1); 
	  	            
	  	    enemigos.add(enemigo);
	  	}
	}
    
	public void dibujaEncabezado() {
		CharSequence str = "Vidas: "+nave.getVidas()+" Ronda: "+ronda;
		game.getFont().getData().setScale(2f);		
		game.getFont().draw(batch, str, 10, 30);
		game.getFont().draw(batch, "Score:"+this.score, WORLD_WIDTH-150, 30);
		game.getFont().draw(batch, "HighScore:"+game.getHighScore(), WORLD_WIDTH/2-100, 30);
	}
	
	@Override
	public void render(float delta) {
		
		// Logica de actualización
		// (Todo se mueve primero)
		
		nave.update(this); 
		
		// Actualizar Balas
		for (int i = 0; i < balas.size(); i++) {
	        Bullet b = balas.get(i);
	        b.update();
	        if (b.isDestroyed()) {
	            balas.remove(i);
	            i--; 
	        }
	    }
		
		// Actualizar Enemigos
	    for (NaveEnemiga enemigo : enemigos) {
	        enemigo.update(this);
	    }

	    // Logica de colision
	    
	    if (!nave.estaHerido()) {
	    	// Colisiones Balas vs Enemigos
	    	for (int i = 0; i < balas.size(); i++) {
		        Bullet b = balas.get(i);
		        for (int j = 0; j < enemigos.size(); j++) {    
		            NaveEnemiga enemigo = enemigos.get(j);
		              
		            if (b.colisionaCon(enemigo)) { 
		                b.alColisionar(enemigo); // La bala se destruye, el enemigo recibe daño
		                
		                if (enemigo.estaDestruido()) {
		                    explosionSound.play();
		                    enemigos.remove(j);
		                    j--;
		                    score +=10;
		                }
		            }   	  
		  	    }
		    }
	    	
	    	// Colisiones Enemigo vs Enemigo (rebotes)
		    for (int i=0;i<enemigos.size();i++) {
		        NaveEnemiga enemigo1 = enemigos.get(i);
		        for (int j=i+1; j<enemigos.size(); j++) { 
		            NaveEnemiga enemigo2 = enemigos.get(j); 
		            if (enemigo1.colisionaCon(enemigo2)) {
		                enemigo1.alColisionar(enemigo2);
		            }
		        }
		    }
		    
		    // Colisiones Jugador vs Enemigo
		    for (int i = 0; i < enemigos.size(); i++) {
	    	    NaveEnemiga enemigo = enemigos.get(i);
	            if (nave.colisionaCon(enemigo)) {
	            	 nave.alColisionar(enemigo); 
	            	 enemigos.remove(i);
	            	 i--;
	            }   	  
  	        }
	    }
		
	    // Logica de dibujo
	    // (Limpiamos y dibujamos todo al final)
	    
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
		camera.update();
        batch.setProjectionMatrix(camera.combined);
          
        batch.begin();
          
        // Dibujar Fondo
        batch.draw(texturaFondo, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
          
        // Dibujar Balas
        for (Bullet b : balas) {       
	        b.draw(batch);
	    }
        
        // Dibujar Enemigos
        for (NaveEnemiga enemigo : enemigos) {
	    	enemigo.draw(batch);
	    }
        
        // Dibujar Nave
        nave.draw(batch); 
        
        // Dibujar Encabezado (UI)
		dibujaEncabezado();
	      
        batch.end();
	      
        // Logica del juego
        if (enemigos.isEmpty()) {
			Screen ss = new PantallaJuego(game,ronda+1, nave.getVidas(), score, 
					velXAsteroides+3, velYAsteroides+3, cantAsteroides+10);
			ss.resize(1200, 800);
			game.setScreen(ss);
			dispose();
		}
        
        if (nave.estaDestruido()) {
  			if (score > game.getHighScore())
  				game.setHighScore(score);
	    	Screen ss = new PantallaGameOver(game);
  			ss.resize(1200, 800);
  			game.setScreen(ss);
  			dispose();
  		}
	}
    
    public boolean agregarBala(Bullet bb) {
    	return balas.add(bb);
    }
	
	@Override
	public void show() {
		gameMusic.play();
	}

	@Override
	public void resize(int width, int height) { }

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void hide() { }

	@Override
	public void dispose() {
		this.explosionSound.dispose();
		this.gameMusic.dispose();
		texturaNaveJugador.dispose();
		texturaNaveEnemiga.dispose();
		texturaBalaJugador.dispose();
		texturaFondo.dispose(); 
	}
}