package io.github.SpaceNav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Representa la pantalla inicial o menú principal del juego {@code Space Navigation}.
 * <p>
 * Esta pantalla muestra una imagen de fondo y reproduce música mientras
 * el jugador espera para iniciar la partida. Al hacer clic o presionar cualquier tecla,
 * se inicia la primera ronda del juego.
 * </p>
 *
 * @see PantallaJuego
 * @see SpaceNavigation
 */
public class PantallaMenu implements Screen {

	/** Referencia al juego principal. */
    private SpaceNavigation game;

    /** Cámara utilizada para renderizar la escena. */
    private OrthographicCamera camera;

    /** Textura de fondo del menú. */
    private Texture backgroundTexture;

    /** Música que se reproduce en el menú principal. */
    private Music menuMusic;

    /**
     * Crea una nueva instancia de la pantalla de menú principal.
     *
     * @param game instancia principal del juego {@link SpaceNavigation}.
     */
	public PantallaMenu(SpaceNavigation game) {
		this.game = game;
        
		camera = new OrthographicCamera();
		camera.setToOrtho(false, PantallaJuego.WORLD_WIDTH, PantallaJuego.WORLD_HEIGHT);
		backgroundTexture = new Texture(Gdx.files.internal("portada.png"));
		
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("inicio.mp3")); 
        menuMusic.setLooping(true);
        menuMusic.setVolume(0.5f);
        menuMusic.play();
	}

	/**
     * Renderiza la pantalla del menú y detecta la interacción del jugador.
     *
     * @param delta tiempo transcurrido desde el último frame, en segundos.
     */
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);

		game.getBatch().begin();
		game.getBatch().draw(backgroundTexture, 0, 0, PantallaJuego.WORLD_WIDTH, PantallaJuego.WORLD_HEIGHT);
		game.getFont().draw(game.getBatch(), "Bienvenid@ a Astro-Toon !", 140, 400);
		game.getFont().draw(game.getBatch(), "Haz click o Presiona Una Tecla para Despegar...", 100, 300);
	
		game.getBatch().end();

		if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
			Screen ss = new PantallaJuego(game,1,3,0,10);
			ss.resize((int)PantallaJuego.WORLD_WIDTH, (int)PantallaJuego.WORLD_HEIGHT);
			game.setScreen(ss);
			dispose();
		}
	}
	
	/**
     * Se ejecuta cuando la pantalla se establece como activa.
     */
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	/**
     * Se ejecuta cuando la pantalla cambia de tamaño.
     *
     * @param width  nuevo ancho de la ventana.
     * @param height nueva altura de la ventana.
     */
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	/**
     * Se ejecuta cuando el juego es pausado (por ejemplo, al minimizar la ventana).
     */
	@Override
	public void pause() {
		menuMusic.pause();
	}

	/**
     * Se ejecuta cuando el juego se reanuda tras una pausa.
     */
	@Override
	public void resume() {
		menuMusic.play();
	}

	/**
     * Se ejecuta cuando la pantalla deja de ser visible o activa.
     */
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	/**
     * Libera los recursos utilizados por la pantalla de menú.
     */
	@Override
	public void dispose() {
		backgroundTexture.dispose();
        menuMusic.dispose();
	}  
}