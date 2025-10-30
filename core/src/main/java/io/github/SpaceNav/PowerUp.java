package io.github.SpaceNav;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Representa un objeto de Power-Up que se mueve por la pantalla
 * y puede ser recogido por el jugador.
 */
public class PowerUp implements Colisionable {

    private Sprite spr;
    private TipoPowerUp tipo;
    private boolean destroyed = false;
    private float xVel = -1; // Se moverá lentamente a la izquierda

    /**
     * Constructor para el Power-Up.
     * @param x Posición inicial en x.
     * @param y Posición inicial en y.
     * @param tx Textura del power-up.
     * @param tipo El tipo de power-up (VIDA o ESCUDO).
     */
    public PowerUp(float x, float y, Texture tx, TipoPowerUp tipo) {
        this.tipo = tipo;
        spr = new Sprite(tx);
        spr.setBounds(x, y, 60, 60);
    }

    /**
     * Actualiza la lógica del power-up (movimiento y límites).
     */
    public void update() {
        spr.setPosition(spr.getX() + xVel, spr.getY());

        if (spr.getX() + spr.getWidth() < 0) {
            destroyed = true;
        }
    }

    /**
     * Dibuja el power-up en la pantalla.
     * @param batch El SpriteBatch para dibujar.
     */
    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    /**
     * Obtiene el tipo de este power-up.
     * @return El TipoPowerUp.
     */
    public TipoPowerUp getTipo() {
        return tipo;
    }

    /**
     * Verifica si el power-up debe ser eliminado.
     * @return true si debe ser destruido, false en caso contrario.
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    // --- Métodos de la Interfaz Colisionable ---

    @Override
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    @Override
    public boolean colisionaCon(Colisionable otro) {
        // Solo colisiona con el jugador
        if (otro instanceof NaveJugador) {
            return this.getArea().overlaps(otro.getArea());
        }
        return false;
    }

    @Override
    public void alColisionar(Colisionable otro) {
        // Se destruye al ser recogido
        this.destroyed = true;
    }
}