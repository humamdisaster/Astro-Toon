package io.github.SpaceNav;

import com.badlogic.gdx.graphics.Texture;

public class NaveEnemiga extends NaveBase {
    private Objetivo objetivo; // referencia al jugador

    public NaveEnemiga(Texture tx, Objetivo objetivo, float x, float y, int vidas) {
        super(tx, x, y, vidas);
        this.objetivo = objetivo;
    }

    @Override
    public void update(PantallaJuego juego) {
        actualizarEstadoHerido();

        if (objetivo == null || objetivo.estaDestruido()) return;
        
        // Movimiento hacia la nave
        float dx = objetivo.getX() - spr.getX();
        float dy = objetivo.getY() - spr.getY();
        float distancia = (float)Math.sqrt(dx*dx + dy*dy);

        float velocidad = 4; // ajusta para que sea más rápida o lenta
        if (distancia > 0) {
            xVel = velocidad * dx / distancia;
            yVel = velocidad * dy / distancia;
        }

        mover();
    }
}
