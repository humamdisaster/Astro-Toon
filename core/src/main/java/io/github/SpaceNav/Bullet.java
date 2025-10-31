package io.github.SpaceNav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle; 


/**
 * Representa un proyectil disparado en el juego.
 * Implementa la interfaz {@link Colisionable} para poder interactuar
 * con otros objetos que puedan colisionar.
 * La bala se mueve según una velocidad definida en X e Y, y se destruye
 * al salir del área del mundo o al impactar un objeto colisionable.
 */
public class Bullet implements Colisionable {

	/** Velocidad horizontal de la bala */
    private int xSpeed;
    
    /** Velocidad vertical de la bala */
    private int ySpeed;
    
    /** Indica si la bala ha sido destruida */
    private boolean destroyed = false;
    
    /** Sprite que representa visualmente la bala */
    private Sprite spr;

    /**
     * Crea una nueva bala en la posición dada con la velocidad y textura especificadas.
     * @param x Posición horizontal inicial
     * @param y Posición vertical inicial
     * @param xSpeed Velocidad horizontal de la bala
     * @param ySpeed Velocidad vertical de la bala
     * @param tx Textura que representa la bala
     */
    public Bullet(float x, float y, int xSpeed, int ySpeed, Texture tx) {
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setSize(20,20);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    /**
     * Actualiza la posición de la bala según su velocidad.
     * Marca la bala como destruida si sale del área del mundo.
     */
    public void update() {
        spr.setPosition(spr.getX() + xSpeed, spr.getY() + ySpeed);
        
        if (spr.getX() < 0 || spr.getX() + spr.getWidth() > PantallaJuego.WORLD_WIDTH ||
             spr.getY() < 0 || spr.getY() + spr.getHeight() > PantallaJuego.WORLD_HEIGHT) {
            destroyed = true;
        }
    }

    /**
     * Dibuja la bala en pantalla usando el SpriteBatch proporcionado.
     * @param batch SpriteBatch utilizado para el renderizado
     */
    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    /**
     * Indica si la bala ha sido destruida.
     * @return true si la bala está destruida, false en caso contrario
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Devuelve el área de colisión de la bala.
     * @return Un {@link Rectangle} que representa el área de colisión
     */
    @Override
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }
    
    /**
     * Comprueba si la bala colisiona con otro objeto colisionable.
     * @param otro Otro objeto que implementa {@link Colisionable}
     * @return true si hay colisión, false en caso contrario
     */
    @Override
    public boolean colisionaCon(Colisionable otro) {
        if (otro instanceof NaveEnemiga) {
            NaveEnemiga enemigo = (NaveEnemiga) otro; 
            return this.getArea().overlaps(enemigo.getArea());
        }
        return false;
    }

    /**
     * Define la acción que ocurre al colisionar con otro objeto colisionable.
     * Marca la bala como destruida y, si el objeto es un {@link NaveEnemiga}, le aplica daño.
     * @param otro Otro objeto que implementa {@link Colisionable}
     */
    @Override
    public void alColisionar(Colisionable otro) {
        this.destroyed = true; 
        
        if (otro instanceof NaveEnemiga) {
            NaveEnemiga enemigo = (NaveEnemiga) otro; 
            enemigo.recibirDano(1); 
        }
    }
}