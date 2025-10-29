package io.github.SpaceNav;

import com.badlogic.gdx.graphics.Texture;
import io.github.SpaceNav.PantallaJuego;

public class NaveEnemiga extends NaveBase {
    private NaveJugador objetivo; // referencia al jugador

    public NaveEnemiga(Texture tx, NaveJugador jugador, float x, float y, int vidas) {
        super(tx, x, y, vidas);
        this.objetivo = jugador;
    }

    @Override
    public void update(PantallaJuego juego) {
        actualizarEstadoHerido();

        // Movimiento hacia la nave
        float dx = objetivo.getX() - spr.getX();
        float dy = objetivo.getY() - spr.getY();
        float distancia = (float)Math.sqrt(dx*dx + dy*dy);

        float velocidad = 2f; // ajusta para que sea más rápida o lenta
        if (distancia > 0) {
            xVel = velocidad * dx / distancia;
            yVel = velocidad * dy / distancia;
        }

        mover();
    }
}