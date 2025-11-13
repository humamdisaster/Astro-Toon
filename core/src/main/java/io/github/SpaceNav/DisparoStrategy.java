package io.github.SpaceNav;

import com.badlogic.gdx.graphics.Texture;

/**
 * [CLASE GM2.3 - PATRÓN STRATEGY (Interfaz)]
 * Interfaz que define el "esqueleto" de un algoritmo de disparo.
 * Cualquier comportamiento de disparo (simple, doble, láser, etc.)
 * debe implementar esta interfaz.
 */
public interface DisparoStrategy {
    
    /**
     * Ejecuta la lógica de disparo.
     * @param nave La nave (Contexto) que está disparando.
     * @param txBala La textura a usar para la(s) bala(s).
     * @param juego La pantalla de juego donde se añadirán las balas.
     */
    void disparar(NaveJugador nave, Texture txBala, PantallaJuego juego);
}