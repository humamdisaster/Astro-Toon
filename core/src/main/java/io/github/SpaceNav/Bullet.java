package io.github.SpaceNav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle; 
import io.github.SpaceNav.PantallaJuego;

public class Bullet implements Colisionable {

    private int xSpeed;
    private int ySpeed;
    private boolean destroyed = false;
    private Sprite spr;

    public Bullet(float x, float y, int xSpeed, int ySpeed, Texture tx) {
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setSize(20,20);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public void update() {
        spr.setPosition(spr.getX() + xSpeed, spr.getY() + ySpeed);
        
        if (spr.getX() < 0 || spr.getX() + spr.getWidth() > PantallaJuego.WORLD_WIDTH ||
             spr.getY() < 0 || spr.getY() + spr.getHeight() > PantallaJuego.WORLD_HEIGHT) {
            destroyed = true;
        }
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }
    
    @Override
    public boolean colisionaCon(Colisionable otro) {
        if (otro instanceof NaveEnemiga) {
            NaveEnemiga enemigo = (NaveEnemiga) otro; 
            return this.getArea().overlaps(enemigo.getArea());
        }
        return false;
    }

    @Override
    public void alColisionar(Colisionable otro) {
        this.destroyed = true; 
        
        if (otro instanceof NaveEnemiga) {
            NaveEnemiga enemigo = (NaveEnemiga) otro; 
            enemigo.recibirDano(1); 
        }
    }
}