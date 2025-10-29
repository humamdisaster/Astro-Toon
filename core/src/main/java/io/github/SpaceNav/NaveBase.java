package io.github.SpaceNav;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public abstract class NaveBase implements Colisionable {
	
    protected Sprite spr;
    protected float xVel = 0;
    protected float yVel = 0;
    protected int vidas;
    protected boolean herido = false;
    protected boolean destruida = false;
    protected int tiempoHerido;
    protected int tiempoHeridoMax = 50; 

    public NaveBase(Texture tx, float x, float y, int vidas) {
        this.vidas = vidas;
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setBounds(x, y, 45, 45); 
    }

    public abstract void update(PantallaJuego juego);

    public void draw(SpriteBatch batch) {
        if (herido) {
            float xOriginal = spr.getX();
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.draw(batch);
            spr.setX(xOriginal); 
        } else {
            spr.draw(batch);
        }
    }

    protected void mover() {
        spr.setPosition(spr.getX() + xVel, spr.getY() + yVel);
    }
    
    protected void actualizarEstadoHerido() {
        if (herido) {
            tiempoHerido--;
            if (tiempoHerido <= 0) {
                herido = false;
            }
        }
    }

    public void recibirDano(int dano) {
        if (!herido) {
            this.vidas -= dano;
            this.herido = true;
            this.tiempoHerido = this.tiempoHeridoMax;
            
            if (this.vidas <= 0) {
                this.destruida = true;
            }
        }
    }
    
    // --- Métodos de la interfaz Colisionable ---
    
    @Override
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }
    
    @Override
    public boolean colisionaCon(Colisionable otro) {
        if (otro instanceof NaveBase) {
            if (this == otro) {
                return false;
            }
            return this.getArea().overlaps(otro.getArea());
        }
        return false;
    }

    @Override
    public void alColisionar(Colisionable otro) {
        // Lógica de rebote (común para todas las naves)
        if (otro instanceof NaveBase) {
            NaveBase otraNave = (NaveBase) otro; 
            
            if (xVel == 0) xVel += otraNave.getXVel() / 2;
            if (otraNave.getXVel() == 0) otraNave.setXVel(otraNave.getXVel() + xVel / 2);
            xVel = -xVel;
            otraNave.setXVel(-otraNave.getXVel());
            
            if (yVel == 0) yVel += otraNave.getYVel() / 2;
            if (otraNave.getYVel() == 0) otraNave.setYVel(otraNave.getYVel() + yVel / 2);
            yVel = -yVel;
            otraNave.setYVel(-otraNave.getYVel());
        }
    }

    // --- Getters y Setters ---
    
    public boolean estaDestruido() {
        return !herido && destruida;
    }

    public boolean estaHerido() {
        return herido;
    }

    public int getVidas() {
        return vidas;
    }
    
    public void setVidas(int vidas) {
    	this.vidas = vidas;
    }

    public float getX() {
        return spr.getX();
    }

    public float getY() {
        return spr.getY();
    }
    
    public float getXVel() {
    	return xVel;
    }
    
    public float getYVel() {
    	return yVel;
    }
    
    public void setXVel(float xVel) {
    	this.xVel = xVel;
    }
    
    public void setYVel(float yVel) {
    	this.yVel = yVel;
    }
}