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

    // Variables para spawn gradual
    private int enemigosMaxNivel;
    private int enemigosCreados;
    private float tiempoSpawn = 0f;
    private float intervaloSpawn = 1f; // segundos entre cada enemigo
    private GestorColisiones gestorColisiones;

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
        gestorColisiones = new GestorColisiones();

        // Sonidos y música
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        explosionSound.setVolume(1, 0.5f);
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play();

        // Texturas
        texturaFondo = new Texture(Gdx.files.internal("sala.png"));
        texturaNaveJugador = new Texture(Gdx.files.internal("gato1.png"));
        texturaNaveEnemiga = new Texture(Gdx.files.internal("secador.png"));
        texturaBalaJugador = new Texture(Gdx.files.internal("bolaPelo.png"));

        // Nave jugador
        nave = new NaveJugador(30, WORLD_HEIGHT / 2 - 50, texturaNaveJugador,
                Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
                texturaBalaJugador,
                Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        nave.setVidas(vidas);
    }

    public void dibujaEncabezado() {
        CharSequence str = "Vidas: " + nave.getVidas() + " Ronda: " + ronda;
        game.getFont().getData().setScale(2f);
        game.getFont().draw(batch, str, 10, 30);
        game.getFont().draw(batch, "Score:" + this.score, WORLD_WIDTH - 150, 30);
        game.getFont().draw(batch, "HighScore:" + game.getHighScore(), WORLD_WIDTH / 2 - 100, 30);
    }
    
    @Override
    public void render(float delta) {

        // --- Spawn gradual de enemigos ---
        tiempoSpawn += delta;
        if (enemigosCreados < enemigosMaxNivel && tiempoSpawn >= intervaloSpawn) {
            tiempoSpawn = 0f;

            Random r = new Random();
            float posY;
            boolean colision;
            int intentos = 0;
            do {
                colision = false;
                posY = r.nextInt((int) (WORLD_HEIGHT - 100));
                for (NaveEnemiga e : enemigos) {
                    if (Math.abs(e.getY() - posY) < 100) { // mínima separación
                        colision = true;
                        break;
                    }
                }
                intentos++;
            } while (colision && intentos < 100);

            NaveEnemiga enemigo = new NaveEnemiga(texturaNaveEnemiga, nave,
                    WORLD_WIDTH - 100 + r.nextInt(50),
                    posY,
                    1);
            enemigos.add(enemigo);
            enemigosCreados++;
        }

        // --- Actualización ---
        nave.update(this);

        // Balas
        for (int i = 0; i < balas.size(); i++) {
            Bullet b = balas.get(i);
            b.update();
            if (b.isDestroyed()) {
                balas.remove(i);
                i--;
            }
        }

        // Enemigos
        for (NaveEnemiga enemigo : enemigos) {
            enemigo.update(this);
        }

        // --- Colisiones ---
        gestorColisiones.manejarColisiones(nave, enemigos, balas, explosionSound, this);
        
        // --- Dibujado ---
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(texturaFondo, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        for (Bullet b : balas) b.draw(batch);
        for (NaveEnemiga enemigo : enemigos) enemigo.draw(batch);
        nave.draw(batch);
        dibujaEncabezado();
        batch.end();

        // --- Cambio de nivel ---
        if (enemigos.isEmpty() && enemigosCreados >= enemigosMaxNivel) {
            Screen ss = new PantallaJuego(game, ronda + 1, nave.getVidas(), score,
                    velXAsteroides + 3, velYAsteroides + 3, cantAsteroides + 10);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }

        // --- Game Over ---
        if (nave.estaDestruido()) {
            if (score > game.getHighScore())
                game.setHighScore(score);
            Screen ss = new PantallaGameOver(game);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }
    }

    public void incrementarScore(int cantidad) {
        	score += cantidad;
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