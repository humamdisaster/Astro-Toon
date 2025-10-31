package io.github.SpaceNav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture; // <-- Importar Texture
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Pantalla que se muestra cuando el jugador pierde el juego.
 * Muestra un fondo de "Game Over", reproduce música y permite reiniciar el juego
 * al tocar la pantalla o presionar cualquier tecla.
 */
public class PantallaGameOver implements Screen {

	/** Referencia al juego principal */
	private SpaceNavigation game;
	
	/** Cámara ortográfica para la proyección 2D */
	private OrthographicCamera camera;
    
	/** Textura de fondo de la pantalla de Game Over */
    private Texture backgroundTexture;

    /** Música reproducida en la pantalla de Game Over */
    private Music gameOverMusic;

    /**
     * Constructor de la pantalla de Game Over.
     * Inicializa cámara, textura de fondo y música.
     *
     * @param game Instancia del juego principal
     */
	public PantallaGameOver(SpaceNavigation game) {
		this.game = game;
        
		camera = new OrthographicCamera();
		camera.setToOrtho(false, PantallaJuego.WORLD_WIDTH, PantallaJuego.WORLD_HEIGHT); // Usar dimensiones del juego
        backgroundTexture = new Texture(Gdx.files.internal("gameOver.png"));
        
        gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("gameOver.mp3")); 
        gameOverMusic.setLooping(false);
        gameOverMusic.setVolume(0.6f);
        gameOverMusic.play();
	}

	/**
     * Renderiza la pantalla cada frame.
     * - Limpia la pantalla y dibuja la textura de fondo.
     * - Muestra los mensajes de "Game Over" y reinicio.
     * - Permite reiniciar el juego al tocar la pantalla o presionar cualquier tecla.
     *
     * @param delta Tiempo transcurrido desde el último frame
     */
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);

		game.getBatch().begin();
        game.getBatch().draw(backgroundTexture, 0, 0, PantallaJuego.WORLD_WIDTH, PantallaJuego.WORLD_HEIGHT);
        
		game.getFont().draw(game.getBatch(), "Game Over !!! ", 120, 400,400,1,true);
		game.getFont().draw(game.getBatch(), "Pincha en cualquier lado para reiniciar ...", 100, 300);
	
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
	 * En esta implementación no realiza acciones adicionales.
	 */
	@Override
	public void show() {
		
	}

	/**
	 * Se ejecuta al cambiar el tamaño de la pantalla.
	 * En esta implementación no realiza acciones adicionales.
	 *
	 * @param width Nuevo ancho de la pantalla
	 * @param height Nuevo alto de la pantalla
	 */
	@Override
	public void resize(int width, int height) {
		
	}

	/**
	 * Se ejecuta cuando el juego se pausa.
	 * Pausa la música de Game Over.
	 */
	@Override
	public void pause() {
		gameOverMusic.pause();
	}

	/**
	 * Se ejecuta cuando el juego se reanuda.
	 * Reproduce la música de Game Over.
	 */
	@Override
	public void resume() {
		gameOverMusic.play();
	}

	/**
	 * Se ejecuta cuando la pantalla deja de ser activa.
	 * En esta implementación no realiza acciones adicionales.
	 */
	@Override
	public void hide() {
		
	}

	/**
     * Libera los recursos utilizados por la pantalla (textura y música).
     */
	@Override
	public void dispose() {
		backgroundTexture.dispose();
        gameOverMusic.dispose();
	}
   
}