package io.github.SpaceNav;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public abstract class NaveBase {
	
    // Atributos comunes para todas las naves
    protected Sprite spr;
    protected float xVel = 0;
    protected float yVel = 0;
    protected int vidas;
    protected boolean herido = false;
    protected boolean destruida = false;
    protected int tiempoHerido;
    protected int tiempoHeridoMax = 50; // Tiempo de invencibilidad

    public NaveBase(Texture tx, float x, float y, int vidas) {
        this.vidas = vidas;
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setBounds(x, y, 45, 45); // Tamaño por defecto, puedes ajustarlo
    }

    /**
     * Método abstracto que cada hijo DEBE implementar.
     * Aquí va la lógica de movimiento (Input o IA) y disparo.
     */
    public abstract void update(PantallaJuego juego);

    /**
     * Método común para dibujar la nave.
     * Incluye la lógica de parpadeo cuando está herido.
     */
    public void draw(SpriteBatch batch) {
        if (herido) {
            // Lógica de "temblar" o parpadear cuando está herido
            float xOriginal = spr.getX();
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.draw(batch);
            spr.setX(xOriginal); // Devolver a la posición original
        } else {
            spr.draw(batch);
        }
    }

    /**
     * Lógica común para mover la nave según su velocidad.
     */
    protected void mover() {
        spr.setPosition(spr.getX() + xVel, spr.getY() + yVel);
    }
    
    /**
     * Actualiza el temporizador de "herido".
     * Se debe llamar en el método update() de las clases hijas.
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
     * Lógica para recibir daño.
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
    
    // Getters y Setters comunes
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }
    
    public boolean estaDestruido() {
        return !herido && destruida; // Asegurarse de que no esté en estado "herido"
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