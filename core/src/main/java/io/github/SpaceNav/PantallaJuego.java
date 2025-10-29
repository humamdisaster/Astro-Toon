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

/**
 * Pantalla principal del juego. Maneja la lógica de renderizado,
 * actualizaciones, colisiones y estado del juego.
 */
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

    // Entidades del juego
    private NaveJugador nave;
    private ArrayList<NaveEnemiga> enemigos = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();
    /** Lista de power-ups activos en pantalla. */
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    /** Generador de números aleatorios para drops de power-ups. */
    private Random random = new Random();

    // Texturas
    private Texture texturaNaveJugador;
    private Texture texturaNaveEnemiga;
    private Texture texturaBalaJugador;
    private Texture texturaFondo;
    /** Textura para el power-up de vida. */
    private Texture texturaVida;
    /** Textura para el power-up de escudo. */
    private Texture texturaEscudo;

    // Variables para spawn gradual
    private int enemigosMaxNivel;
    private int enemigosCreados;
    private float tiempoSpawn = 0f;
    private float intervaloSpawn = 1f;


    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,  
                         int velXAsteroides, int velYAsteroides, int cantAsteroides) {
        this.game = game;
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;
        this.cantAsteroides = cantAsteroides;
        this.enemigosMaxNivel = cantAsteroides;
        this.enemigosCreados = 0;

        batch = game.getBatch();
        camera = new OrthographicCamera();	
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        // Cargar sonidos y música
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1, 0.5f);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play();

        // Cargar texturas
        texturaFondo = new Texture(Gdx.files.internal("sala.png"));
        texturaNaveJugador = new Texture(Gdx.files.internal("gato1.png"));
        texturaNaveEnemiga = new Texture(Gdx.files.internal("secador.png"));
        texturaBalaJugador = new Texture(Gdx.files.internal("bolaPelo.png"));
        texturaVida = new Texture(Gdx.files.internal("pezVida.png"));
        texturaEscudo = new Texture(Gdx.files.internal("cajaEscudo.png"));

        // Crear nave del jugador
        nave = new NaveJugador(30, WORLD_HEIGHT / 2 - 50, texturaNaveJugador,
                Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
                texturaBalaJugador,
                Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        nave.setVidas(vidas);
    }

    /**
     * Dibuja el HUD (Vidas, Ronda, Score) en la pantalla.
     */
    public void dibujaEncabezado() {
        CharSequence str = "Vidas: " + nave.getVidas() + " Ronda: " + ronda;
        game.getFont().getData().setScale(2f);		
        game.getFont().draw(batch, str, 10, 30);
        game.getFont().draw(batch, "Score:" + this.score, WORLD_WIDTH - 150, 30);
        game.getFont().draw(batch, "HighScore:" + game.getHighScore(), WORLD_WIDTH / 2 - 100, 30);
    }

    @Override
    public void render(float delta) {
        
        // --- 1. LÓGICA DE ACTUALIZACIÓN (UPDATE) ---

        // Spawn gradual de enemigos
        tiempoSpawn += delta;
        if (enemigosCreados < enemigosMaxNivel && tiempoSpawn >= intervaloSpawn) {
            tiempoSpawn = 0f;
            // Usamos el constructor de NaveEnemiga
            NaveEnemiga enemigo = new NaveEnemiga(texturaNaveEnemiga, nave,
                    WORLD_WIDTH - 100 + random.nextInt(50),
                    random.nextInt((int)WORLD_HEIGHT),
                    1);
            enemigos.add(enemigo);
            enemigosCreados++;
        }

        nave.update(this); 

        for (int i = 0; i < balas.size(); i++) {
            Bullet b = balas.get(i);
            b.update();
            if (b.isDestroyed()) {
                balas.remove(i);
                i--; 
            }
        }

        for (NaveEnemiga enemigo : enemigos) {
            enemigo.update(this);
        }

        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp p = powerUps.get(i);
            p.update();
            if (p.isDestroyed()) {
                powerUps.remove(i);
                i--;
            }
        }

        // --- 2. LÓGICA DE COLISIÓN ---
        if (!nave.estaHerido()) {
            // Balas vs Enemigos
            for (int i = 0; i < balas.size(); i++) {
                Bullet b = balas.get(i);
                for (int j = 0; j < enemigos.size(); j++) {    
                    NaveEnemiga enemigo = enemigos.get(j);
                    
                    if (b.colisionaCon(enemigo)) { 
                        b.alColisionar(enemigo); 
                        
                        if (enemigo.estaDestruido()) {
                            explosionSound.play();
                            
                            // Probabilidad de soltar PowerUp
                            if (random.nextInt(100) < 20) { // 20% de chance
                                soltarPowerUp(enemigo.getX(), enemigo.getY());
                            }
                            
                            enemigos.remove(j);
                            j--;
                            score += 10;
                        }
                    }   	  
                }
            }
            
            // Enemigo vs Enemigo
            for (int i = 0; i < enemigos.size(); i++) {
                NaveEnemiga enemigo1 = enemigos.get(i);   
                for (int j = i + 1; j < enemigos.size(); j++) { 
                    NaveEnemiga enemigo2 = enemigos.get(j); 
                    if (enemigo1.colisionaCon(enemigo2)) {
                        enemigo1.alColisionar(enemigo2);
                    }
                }
            }
            
            // Jugador vs Enemigo
            for (int i = 0; i < enemigos.size(); i++) {
                NaveEnemiga enemigo = enemigos.get(i);
                if (nave.colisionaCon(enemigo)) {
                    nave.alColisionar(enemigo); 
                    enemigos.remove(i);
                    i--;
                }   	  
            }
        }

        // Jugador vs PowerUps (se comprueba incluso si está herido)
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp p = powerUps.get(i);
            if (p.colisionaCon(nave)) {
                nave.recibirPowerUp(p.getTipo());
                p.alColisionar(nave); 
            }
        }
		
        // --- 3. LÓGICA DE DIBUJO (DRAW) ---
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        batch.draw(texturaFondo, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        for (Bullet b : balas) b.draw(batch);
        for (NaveEnemiga enemigo : enemigos) enemigo.draw(batch);
        for (PowerUp p : powerUps) p.draw(batch);
        nave.draw(batch); 
        dibujaEncabezado();
	      
        batch.end();
	      
        // --- 4. LÓGICA DE ESTADO DEL JUEGO ---
        
        // Condición de victoria (Ronda completada)
        if (enemigos.isEmpty() && enemigosCreados >= enemigosMaxNivel) {
            Screen ss = new PantallaJuego(game, ronda + 1, nave.getVidas(), score,
                    velXAsteroides + 1, velYAsteroides + 1, cantAsteroides + 5);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
            return; 
        }
        
        // Condición de derrota (Game Over)
        if (nave.estaDestruido()) {
            if (score > game.getHighScore())
                game.setHighScore(score);
            Screen ss = new PantallaGameOver(game);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }
    }
    
    /**
     * Crea un Power-Up en una posición específica.
     * @param x Posición en x donde soltar el item.
     * @param y Posición en y donde soltar el item.
     */
    private void soltarPowerUp(float x, float y) {
        if (random.nextBoolean()) {
            powerUps.add(new PowerUp(x, y, texturaVida, TipoPowerUp.VIDA));
        } else {
            powerUps.add(new PowerUp(x, y, texturaEscudo, TipoPowerUp.ESCUDO));
        }
    }
    
    /**
     * Permite a otras clases (como NaveJugador) añadir balas a la lista.
     * @param bb La bala a añadir.
     * @return true si la bala fue añadida.
     */
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
        texturaVida.dispose();
        texturaEscudo.dispose();
    }
}