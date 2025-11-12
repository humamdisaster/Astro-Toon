package io.github.SpaceNav;

import com.badlogic.gdx.Screen;

/**
 * Clase responsable de gestionar las rondas del juego.
 * Controla las condiciones de victoria y 
 * gestiona las transiciones correspondientes entre pantallas.
 * * [CAMBIO GM2.1] La lógica de derrota (nave.estaDestruido())
 * se ha movido a PantallaJuego.render(). Este gestor
 * ahora solo se enfoca en la condición de victoria (completar ronda).
 */
public class GestorRondas {
	
	/**
     * Maneja la lógica de avance de rondas.
     * Evalúa si la ronda ha sido completada:
     * - Marca la ronda como completada si se han creado suficientes enemigos y 
     * no quedan enemigos activos.
     *
     * [CAMBIO GM2.1] La condición de derrota ya no se comprueba aquí.
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

	    // [CAMBIO GM2.1] Condición de derrota eliminada.
	    // Esta lógica ahora vive en PantallaJuego.render()
	    /*
	    if (nave.estaDestruido()) {
	        if (juego.getScore() > juego.getGame().getHighScore()) // <-- esto quedó roto con los cambios
	            juego.getGame().setHighScore(juego.getScore());

	        Screen gameOver = new PantallaGameOver(juego.getGame());
	        gameOver.resize(1200, 800);
	        juego.getGame().setScreen(gameOver);
	        juego.dispose();
	    }
	    */
	}
}