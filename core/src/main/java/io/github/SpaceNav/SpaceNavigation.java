package io.github.SpaceNav;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Clase principal del juego.
 * Extiende {@link Game} de LibGDX y se encarga de:
 * - Inicializar recursos gráficos y fuentes
 * - Mantener el puntaje más alto (high score)
 * - Establecer la pantalla inicial del juego (menú)
 */
public class SpaceNavigation extends Game {
	
	/** Nombre del juego */
	private String nombreJuego = "Space Navigation";
	
	/** Lote de sprites para dibujar en pantalla */
	private SpriteBatch batch;
	
	/** Fuente utilizada para mostrar texto */
	private BitmapFont font;
	
	/** Puntaje más alto registrado durante la ejecución */
	private int highScore;	

	/**
     * Inicializa los recursos del juego y establece la pantalla inicial.
     * Se ejecuta al iniciar la aplicación.
     */
	public void create() {
		highScore = 0;
		batch = new SpriteBatch();
		font = new BitmapFont(); // usa Arial font x defecto
		font.getData().setScale(2f);
		Screen ss = new PantallaMenu(this);
		this.setScreen(ss);
	}

	/**
     * Renderiza la pantalla actual.
     * Llama a {@link Game#render()} para actualizar la lógica y dibujado de la pantalla activa.
     */
	public void render() {
		super.render(); // important!
	}

	/**
     * Libera los recursos utilizados por el juego (batch y fuente).
     * Se ejecuta al cerrar la aplicación.
     */
	public void dispose() {
		batch.dispose();
		font.dispose();
	}

	/**
     * Obtiene el SpriteBatch utilizado para dibujar en pantalla.
     * @return SpriteBatch activo
     */
	public SpriteBatch getBatch() {
		return batch;
	}

	/**
     * Obtiene la fuente utilizada para dibujar texto.
     * @return Fuente activa (BitmapFont)
     */
	public BitmapFont getFont() {
		return font;
	}

	/**
     * Obtiene el puntaje más alto registrado.
     * @return Valor del high score
     */
	public int getHighScore() {
		return highScore;
	}

	/**
     * Establece un nuevo puntaje más alto.
     * @param highScore Nuevo valor de high score
     */
	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}
}