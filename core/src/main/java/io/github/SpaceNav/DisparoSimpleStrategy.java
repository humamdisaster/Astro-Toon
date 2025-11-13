package io.github.SpaceNav;

import com.badlogic.gdx.graphics.Texture;

/**
 * [CLASE GM2.3 - PATRÓN STRATEGY (Estrategia Concreta)]
 * Implementación de la estrategia de disparo simple.
 * Esta es la lógica original que estaba en NaveJugador.
 */
public class DisparoSimpleStrategy implements DisparoStrategy {

    @Override
    public void disparar(NaveJugador nave, Texture txBala, PantallaJuego juego) {
        // Lógica de disparo simple: una sola bala
        Bullet bala = new Bullet(nave.getX() + nave.spr.getWidth() - 5, 
                                 nave.getY() + nave.spr.getHeight() / 2 - 5, 
                                 3, 0, txBala);
        juego.agregarBala(bala);
    }
}