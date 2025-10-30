package io.github.SpaceNav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;


public class PantallaMenu implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;
	private Texture backgroundTexture;

	public PantallaMenu(SpaceNavigation game) {
		this.game = game;
        
		camera = new OrthographicCamera();
		camera.setToOrtho(false, PantallaJuego.WORLD_WIDTH, PantallaJuego.WORLD_HEIGHT);
		backgroundTexture = new Texture(Gdx.files.internal("portada.png"));
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);

		game.getBatch().begin();
		game.getBatch().draw(backgroundTexture, 0, 0, PantallaJuego.WORLD_WIDTH, PantallaJuego.WORLD_HEIGHT);
		game.getFont().draw(game.getBatch(), "Bienvenid@ a Astro-Toon !", 140, 400);
		game.getFont().draw(game.getBatch(), "Pincha en cualquier lado o presiona cualquier tecla para comenzar ...", 100, 300);
	
		game.getBatch().end();

		if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
			Screen ss = new PantallaJuego(game,1,3,0,1,1,10);
			ss.resize((int)PantallaJuego.WORLD_WIDTH, (int)PantallaJuego.WORLD_HEIGHT);
			game.setScreen(ss);
			dispose();
		}
	}
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		backgroundTexture.dispose();
		
	}  
}