package io.github.SpaceNav;

import com.badlogic.gdx.graphics.Texture;
import io.github.SpaceNav.PantallaJuego;

public class NaveEnemiga extends NaveBase {

    public NaveEnemiga(Texture tx, float x, float y, float xVel, float yVel, int vidas) {
        super(tx, x, y, vidas);
        this.xVel = xVel;
        this.yVel = yVel;
    }

    @Override
    public void update(PantallaJuego juego) {
        actualizarEstadoHerido();
        
        float x = spr.getX();
        float y = spr.getY();

        if (x + xVel < 0 || x + xVel + spr.getWidth() > PantallaJuego.WORLD_WIDTH)
            xVel *= -1;
        if (y + yVel < 0 || y + yVel + spr.getHeight() > PantallaJuego.WORLD_HEIGHT)
            yVel *= -1;

        mover();
    }
}