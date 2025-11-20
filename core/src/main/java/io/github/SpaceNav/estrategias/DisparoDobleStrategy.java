package io.github.SpaceNav.estrategias;

import com.badlogic.gdx.graphics.Texture;

import io.github.SpaceNav.entidades.Bullet;
import io.github.SpaceNav.entidades.NaveJugador;
import io.github.SpaceNav.pantallas.PantallaJuego;

/**
 * [CLASE GM2.3 - PATRÓN STRATEGY (Estrategia Concreta)]
 * Implementación de la estrategia de disparo doble.
 * Crea dos balas en lugar de una.
 */
public class DisparoDobleStrategy implements DisparoStrategy {

    @Override
    public void disparar(NaveJugador nave, Texture txBala, PantallaJuego juego) {
        // Lógica de disparo doble: dos balas
    	float x = nave.getX() + nave.getAncho() - 5;
        float yCentro = nave.getY() + nave.getAlto() / 2 - 5;

        Bullet bala1 = new Bullet(x, yCentro + 10, 3, 0, txBala);
        Bullet bala2 = new Bullet(x, yCentro - 10, 3, 0, txBala);

        juego.agregarBala(bala1);
        juego.agregarBala(bala2);
    }
}