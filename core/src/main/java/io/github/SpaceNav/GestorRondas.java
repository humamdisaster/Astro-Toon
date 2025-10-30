package io.github.SpaceNav;

import com.badlogic.gdx.Screen;

public class GestorRondas {
	
	public void manejarRondas(PantallaJuego juego, NaveJugador nave, int enemigosCreados, int enemigosMaxNivel) {
	    // Condición de victoria
	    if (juego.getEnemigos().isEmpty() && enemigosCreados >= enemigosMaxNivel) {
	        juego.setRondaCompletada(true); // activa bandera para transición
	        return;
	    }

	    // Condición de derrota
	    if (nave.estaDestruido()) {
	        if (juego.getScore() > juego.getGame().getHighScore())
	            juego.getGame().setHighScore(juego.getScore());

	        Screen gameOver = new PantallaGameOver(juego.getGame());
	        gameOver.resize(1200, 800);
	        juego.getGame().setScreen(gameOver);
	        juego.dispose();
	    }
	}

	
}
