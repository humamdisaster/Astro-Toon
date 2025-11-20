package io.github.SpaceNav.estrategias;

import com.badlogic.gdx.graphics.Texture;

import io.github.SpaceNav.entidades.Bullet;
import io.github.SpaceNav.entidades.NaveJugador;
import io.github.SpaceNav.pantallas.PantallaJuego;

/**
 * [CLASE GM2.3 - PATRÓN STRATEGY (Estrategia Concreta)]
 * Implementación de la estrategia de disparo simple.
 * Esta es la lógica original que estaba en NaveJugador.
 */
public class DisparoSimpleStrategy implements DisparoStrategy {

    @Override
    public void disparar(NaveJugador nave, Texture txBala, PantallaJuego juego) {
        // Lógica de disparo simple: una sola bala
    	Bullet bala = new Bullet(nave.getX() + nave.getAncho() - 5, 
                nave.getY() + nave.getAlto() / 2 - 5, 
                3, 0, txBala);
        juego.agregarBala(bala);
    }
}