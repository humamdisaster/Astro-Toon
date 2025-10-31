package io.github.SpaceNav;

import com.badlogic.gdx.Screen;

/**
 * Clase responsable de gestionar las rondas del juego.
 * Controla las condiciones de victoria y derrota, 
 * y gestiona las transiciones correspondientes entre pantallas.
 */
public class GestorRondas {
	
	/**
     * Maneja la lógica de avance de rondas.
     * Evalúa si la ronda ha sido completada o si el jugador ha sido derrotado,
     * aplicando las acciones correspondientes:
     * - Marca la ronda como completada si se han creado suficientes enemigos y 
     *   no quedan enemigos activos.
     * - Cambia a la pantalla de Game Over si el jugador ha sido destruido, 
     *   actualizando el récord si corresponde.
     *
     * @param juego Instancia de {@link PantallaJuego} que contiene el estado actual del juego
     * @param nave Nave del jugador
     * @param enemigosCreados Número total de enemigos generados en la ronda actual
     * @param enemigosMaxNivel Número máximo de enemigos permitidos en el nivel
     */
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
