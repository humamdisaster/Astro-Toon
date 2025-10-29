package io.github.SpaceNav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class NaveEnemiga extends NaveBase {

    public NaveEnemiga(Texture tx, float x, float y, float xVel, float yVel, int vidas) {
        super(tx, x, y, vidas);
        this.xVel = xVel;
        this.yVel = yVel;
    }

    @Override
    public void update(PantallaJuego juego) {
        actualizarEstadoHerido();
        
        // Lógica de IA (rebotar en paredes)
        float x = spr.getX();
        float y = spr.getY();

        if (x + xVel < 0 || x + xVel + spr.getWidth() > Gdx.graphics.getWidth())
            xVel *= -1;
        if (y + yVel < 0 || y + yVel + spr.getHeight() > Gdx.graphics.getHeight())
            yVel *= -1;

        mover();
        
        // Aquí se podría añadir lógica de disparo enemigo
    }
}