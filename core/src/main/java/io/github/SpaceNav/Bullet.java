package io.github.SpaceNav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Bullet implements Colisionable {

    private int xSpeed;
    private int ySpeed;
    private boolean destroyed = false;
    private Sprite spr;

    public Bullet(float x, float y, int xSpeed, int ySpeed, Texture tx) {
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public void update() {
        spr.setPosition(spr.getX() + xSpeed, spr.getY() + ySpeed);
        
        if (spr.getX() < 0 || spr.getX() + spr.getWidth() > Gdx.graphics.getWidth() ||
             spr.getY() < 0 || spr.getY() + spr.getHeight() > Gdx.graphics.getHeight()) {
            destroyed = true;
        }
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    // --- Métodos de la interfaz Colisionable ---
    
    @Override
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }
    
    @Override
    public boolean colisionaCon(Colisionable otro) {
        // Una bala solo colisiona con enemigos
        if (otro instanceof NaveEnemiga) {
            // Usa getArea() de la interfaz (que NaveEnemiga hereda de NaveBase)
            return this.getArea().overlaps(otro.getArea());
        }
        return false;
    }

    @Override
    public void alColisionar(Colisionable otro) {
        // La bala siempre se destruye
        this.destroyed = true; 
        
        if (otro instanceof NaveEnemiga) {
            NaveEnemiga enemigo = (NaveEnemiga) otro; 
            enemigo.recibirDano(1); 
        }
    }
}