package io.github.SpaceNav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture; // <-- Importar Texture
import com.badlogic.gdx.utils.ScreenUtils;


public class PantallaGameOver implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;
    private Texture backgroundTexture;

	public PantallaGameOver(SpaceNavigation game) {
		this.game = game;
        
		camera = new OrthographicCamera();
		camera.setToOrtho(false, PantallaJuego.WORLD_WIDTH, PantallaJuego.WORLD_HEIGHT); // Usar dimensiones del juego
        backgroundTexture = new Texture(Gdx.files.internal("gameOver.png"));
	}

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
 
	
	@Override
	public void show() {
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		backgroundTexture.dispose();
	}
   
}