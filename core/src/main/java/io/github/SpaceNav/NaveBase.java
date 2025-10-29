package io.github.SpaceNav;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Clase abstracta para todas las naves del juego (Jugador y Enemigos).
 * Implementa Colisionable y define la lógica común de vida, movimiento y daño.
 */
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
        spr.setBounds(x, y, 90, 90); 
    }

    /**
     * Método abstracto para actualizar la lógica de la nave.
     * @param juego La pantalla de juego principal.
     */
    public abstract void update(PantallaJuego juego);

    /**
     * Dibuja la nave en la pantalla.
     * @param batch El SpriteBatch para dibujar.
     */
    public void draw(SpriteBatch batch) {
        if (herido && !destruida) {
            float xOriginal = spr.getX();
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.draw(batch);
            spr.setX(xOriginal); 
        } else if (!destruida) { 
            spr.draw(batch);
        }
    }

    /**
     * Aplica la velocidad actual para mover el sprite.
     */
    protected void mover() {
        spr.setPosition(spr.getX() + xVel, spr.getY() + yVel);
    }
    
    /**
     * Reduce el temporizador de "herido" (invencibilidad) cada frame.
     */
    protected void actualizarEstadoHerido() {
        if (herido) {
            tiempoHerido--;
            if (tiempoHerido <= 0) {
                herido = false;
            }
        }
    }

    /**
     * Aplica daño a la nave si no está herida.
     * @param dano La cantidad de daño a recibir.
     */
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

    /**
     * Activa el estado de invencibilidad (herido) por una duración específica.
     * Usado por el Power-Up de Escudo.
     * @param duracion El número de frames que durará.
     */
    public void activarInvencibilidad(int duracion) {
        this.herido = true;
        this.tiempoHerido = duracion;
    }

    // --- Getters y Setters ---
    
    public boolean estaDestruido() {
        return destruida;
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