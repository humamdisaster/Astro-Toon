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
 * Pantalla principal del juego. Se encarga de la lógica general,
 * incluyendo renderizado, control de entidades, detección de colisiones,
 * administración de rondas y actualización del estado del juego.
 *
 * <p>Esta clase implementa {@link Screen} y funciona como el núcleo
 * donde se combinan los diferentes sistemas del juego, como el
 * {@link GestorColisiones} y el {@link GestorRondas}.</p>
 */
public class PantallaJuego implements Screen {

	/** Ancho del mundo del juego. */
    public static final float WORLD_WIDTH = 800;
    /** Alto del mundo del juego. */
    public static final float WORLD_HEIGHT = 640;

    private SpaceNavigation game;
    private OrthographicCamera camera;	
    private SpriteBatch batch;
    private Sound explosionSound;
    private Music gameMusic;
    private int score;
    private int ronda;
    private int cantEnemigos;

    // Entidades del juego
    private NaveJugador nave;
    private ArrayList<NaveEnemiga> enemigos = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();
    /** Lista de power-ups activos en pantalla. */
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    /** Generador de números aleatorios para el drop de power-ups. */
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
    private float intervaloSpawn = 1f; // segundos entre cada enemigo
    private GestorColisiones gestorColisiones;
    private GestorRondas gestorRondas;
    private boolean rondaCompletada = false;
    private float tiempoTransicion = 0f;
    private boolean mostrandoTransicion = false;

    /**
     * Constructor principal. Inicializa los recursos y entidades necesarias
     * para comenzar la ronda.
     *
     * @param game referencia al juego principal.
     * @param ronda número actual de la ronda.
     * @param vidas cantidad de vidas del jugador.
     * @param score puntuación acumulada.
     * @param cantEnemigos cantidad de enemigos que aparecerán en la ronda.
     */
    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score, int cantEnemigos) {
        this.game = game;
        this.ronda = ronda;
        this.score = score;
        this.cantEnemigos = cantEnemigos;
        this.enemigosMaxNivel = cantEnemigos;
        this.enemigosCreados = 0;

        batch = game.getBatch();
        camera = new OrthographicCamera();	
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        gestorColisiones = new GestorColisiones();
        gestorRondas = new GestorRondas();

        // Cargar sonidos y música
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(1f);
        gameMusic.play();

        // Cargar texturas
        texturaFondo = new Texture(Gdx.files.internal("sala.png"));
        texturaNaveJugador = new Texture(Gdx.files.internal("gato1.png"));
        texturaNaveEnemiga = new Texture(Gdx.files.internal("secador.png"));
        texturaBalaJugador = new Texture(Gdx.files.internal("bolaPelo.png"));
        texturaVida = new Texture(Gdx.files.internal("pezVida.png"));
        texturaEscudo = new Texture(Gdx.files.internal("cajaEscudo.png"));

        // Crear nave del jugador
        nave = new NaveJugador(30, WORLD_HEIGHT / 2 - 50, texturaNaveJugador, texturaBalaJugador);
        nave.setVidas(vidas);
    }

    /**
     * Dibuja la interfaz del jugador (HUD), incluyendo vidas, ronda y puntuación.
     */
    public void dibujaEncabezado() {
        CharSequence str = "Vidas: " + nave.getVidas() + " Ronda: " + ronda;
        game.getFont().getData().setScale(2f);		
        game.getFont().draw(batch, str, 10, 30);
        game.getFont().draw(batch, "Score:" + this.score, WORLD_WIDTH - 150, 30);
        game.getFont().draw(batch, "HighScore:" + game.getHighScore(), WORLD_WIDTH / 2 - 100, 30);
    }
    
    /**
     * Lógica principal del juego. Actualiza entidades, gestiona colisiones,
     * dibuja los elementos y controla el avance entre rondas.
     *
     * @param delta tiempo transcurrido desde el último frame (en segundos).
     */
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
        gestorColisiones.manejarColisiones(nave, enemigos, balas, powerUps, explosionSound, this);
		
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
        
        gestorRondas.manejarRondas(this, nave, enemigosCreados, enemigosMaxNivel);
        
        if (rondaCompletada && !mostrandoTransicion) {
            mostrandoTransicion = true;
            tiempoTransicion = 0f;
        }

        // Si estamos en transición, mostrar texto y contar tiempo
        if (mostrandoTransicion) {
            tiempoTransicion += delta;

            batch.begin();
            game.getFont().getData().setScale(3f);
            game.getFont().draw(batch, "RONDA " + (ronda + 1), WORLD_WIDTH / 2f - 130, WORLD_HEIGHT / 2f);
            batch.end();

            if (tiempoTransicion >= 3f) {
                Screen siguiente = new PantallaJuego(game, ronda + 1, nave.getVidas(), score, cantEnemigos + 5);
                siguiente.resize(1200, 800);
                game.setScreen(siguiente);
                dispose();
            }
        }

    }

    /**
     * Incrementa la puntuación del jugador.
     * @param cantidad puntos a añadir al marcador actual.
     */
    public void incrementarScore(int cantidad) {
        	score += cantidad;
    }
    
    /**
     * Genera un Power-Up en una posición determinada tras la destrucción de un enemigo.
     * @param x posición X donde aparece el Power-Up.
     * @param y posición Y donde aparece el Power-Up.
     */
    public void soltarPowerUp(float x, float y) {
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
	
    public boolean isRondaCompletada() {
        return rondaCompletada;
    }

    public void setRondaCompletada(boolean valor) {
        this.rondaCompletada = valor;
    }

    public SpaceNavigation getGame() {
        return game;
    }

    public ArrayList<NaveEnemiga> getEnemigos() {
        return enemigos;
    }
    
    public int getScore() {
        return score;
    }
    
    // Métodos del ciclo de vida de la pantalla
    
    /** Se ejecuta cuando la pantalla se muestra. */
    @Override
    public void show() {
        gameMusic.play();
    }

    /** Se ejecuta al cambiar el tamaño de la ventana. */
    @Override
    public void resize(int width, int height) { }

    /** Se ejecuta cuando el juego entra en pausa. */
    @Override
    public void pause() { }

    /** Se ejecuta cuando el juego se reanuda. */
    @Override
    public void resume() { }

    /** Se ejecuta cuando la pantalla deja de ser visible. */
    @Override
    public void hide() { }

    /** Libera los recursos gráficos y de audio asociados a la pantalla. */
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