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
		camera.setToOrtho(false, 800, 640);
		
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
		explosionSound.setVolume(1,0.5f);
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav")); 
		
		gameMusic.setLooping(true);
		gameMusic.setVolume(0.5f);
		gameMusic.play();
		
        texturaNaveJugador = new Texture(Gdx.files.internal("MainShip3.png"));
        texturaNaveEnemiga = new Texture(Gdx.files.internal("MainShip3.png")); 
        texturaBalaJugador = new Texture(Gdx.files.internal("Rocket2.png"));
        
	    nave = new NaveJugador(Gdx.graphics.getWidth()/2-50, 30, texturaNaveJugador,
	    				Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
	    				texturaBalaJugador,
	    				Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"))); 
        nave.setVidas(vidas);
        
        Random r = new Random();
	    for (int i = 0; i < cantAsteroides; i++) {
	        NaveEnemiga enemigo = new NaveEnemiga(texturaNaveEnemiga,
	    			r.nextInt((int)Gdx.graphics.getWidth()),
	  	            50+r.nextInt((int)Gdx.graphics.getHeight()-50),
	  	            velXAsteroides+r.nextInt(4), 
	  	            velYAsteroides+r.nextInt(4),
	  	            1); 
	  	    enemigos.add(enemigo);
	  	}
	}
    
	public void dibujaEncabezado() {
		CharSequence str = "Vidas: "+nave.getVidas()+" Ronda: "+ronda;
		game.getFont().getData().setScale(2f);		
		game.getFont().draw(batch, str, 10, 30);
		game.getFont().draw(batch, "Score:"+this.score, Gdx.graphics.getWidth()-150, 30);
		game.getFont().draw(batch, "HighScore:"+game.getHighScore(), Gdx.graphics.getWidth()/2-100, 30);
	}
	
	@Override
	public void render(float delta) {
		  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
          batch.begin();
		  dibujaEncabezado();
		  
		  nave.update(this); 
		  
	      if (!nave.estaHerido()) {
		      // colisiones entre balas y enemigos
	    	  for (int i = 0; i < balas.size(); i++) {
		            Bullet b = balas.get(i);
		            b.update();
		            
		            for (int j = 0; j < enemigos.size(); j++) {    
		              NaveEnemiga enemigo = enemigos.get(j);
		              
		              // --- USO DE LA INTERFAZ ---
		              if (b.colisionaCon(enemigo)) { 
		            	 b.alColisionar(enemigo); // La bala se destruye y el enemigo recibe daño
		            	 
		            	 // Revisamos si el enemigo fue destruido por la colisión
		            	 if (enemigo.estaDestruido()) {
		            		 explosionSound.play();
		            		 enemigos.remove(j);
		            		 j--;
		            		 score +=10;
		            	 }
		              }   	  
		  	        }
		                
		            if (b.isDestroyed()) {
		                balas.remove(b);
		                i--; 
		            }
		      }
		      
		      //actualizar movimiento de enemigos
		      for (NaveEnemiga enemigo : enemigos) {
		          enemigo.update(this);
		      }
		      
		      //colisiones entre enemigos (rebotes)
		      for (int i=0;i<enemigos.size();i++) {
		    	NaveEnemiga enemigo1 = enemigos.get(i);   
		        for (int j=0;j<enemigos.size();j++) {
		          NaveEnemiga enemigo2 = enemigos.get(j); 
		          if (i<j) {
		        	  // --- USO DE LA INTERFAZ ---
		        	  if (enemigo1.colisionaCon(enemigo2)) {
		        		  enemigo1.alColisionar(enemigo2); // Solo rebotan
		        	  }
		          }
		        }
		      } 
	      }
	      
	      //dibujar balas
	      for (Bullet b : balas) {       
	          b.draw(batch);
	      }
	      
	      nave.draw(batch); 
	      
	      //dibujar enemigos y manejar colision con nave
	      for (int i = 0; i < enemigos.size(); i++) {
	    	    NaveEnemiga enemigo = enemigos.get(i);
	    	    enemigo.draw(batch);
		          
	    	    // --- USO DE LA INTERFAZ ---
	            if (nave.colisionaCon(enemigo)) {
	            	 nave.alColisionar(enemigo); // Jugador y enemigo rebotan, jugador recibe daño
	            	 
	            	 // En esta colisión, el enemigo también se destruye
	            	 enemigos.remove(i);
	            	 i--;
              }   	  
  	        }
	      
	      if (nave.estaDestruido()) {
  			if (score > game.getHighScore())
  				game.setHighScore(score);
	    	Screen ss = new PantallaGameOver(game);
  			ss.resize(1200, 800);
  			game.setScreen(ss);
  			dispose();
  		  }
	      batch.end();
	      
	      if (enemigos.size()==0) {
			Screen ss = new PantallaJuego(game,ronda+1, nave.getVidas(), score, 
					velXAsteroides+3, velYAsteroides+3, cantAsteroides+10);
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
	}
   
}