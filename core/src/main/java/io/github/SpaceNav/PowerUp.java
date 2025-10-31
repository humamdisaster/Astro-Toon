package io.github.SpaceNav;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Representa un objeto de {@code Power-Up} que aparece en la pantalla
 * y puede ser recogido por el jugador para obtener beneficios.
 * <p>
 * Los Power-Ups se desplazan lentamente hacia la izquierda y desaparecen
 * al salir del área visible o al ser recogidos por la nave del jugador.
 * </p>
 *
 * @see TipoPowerUp
 * @see NaveJugador
 * @see Colisionable
 */
public class PowerUp implements Colisionable {

	/** Sprite que representa visualmente al power-up en pantalla. */
    private Sprite spr;

    /** Tipo del power-up (por ejemplo, VIDA o ESCUDO). */
    private TipoPowerUp tipo;

    /** Indica si el power-up debe ser eliminado del juego. */
    private boolean destroyed = false;

    /** Velocidad horizontal con la que se desplaza hacia la izquierda. */
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
     * Actualiza el estado del power-up en cada frame.
     * <p>
     * Controla su desplazamiento y verifica si debe ser eliminado
     * al salir del área visible de la pantalla.
     * </p>
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
     * Devuelve el tipo de este power-up.
     *
     * @return el {@link TipoPowerUp} asociado.
     */
    public TipoPowerUp getTipo() {
        return tipo;
    }

    /**
     * Indica si el power-up debe eliminarse del juego.
     *
     * @return {@code true} si el power-up ha sido destruido,
     *         {@code false} en caso contrario.
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    // --- Métodos de la Interfaz Colisionable ---

    /**
     * Obtiene el área rectangular del sprite, usada para detección de colisiones.
     *
     * @return el área del power-up como {@link Rectangle}.
     */
    @Override
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    /**
     * Verifica si este power-up colisiona con otro objeto colisionable.
     * <p>
     * Solo se considera colisión si el otro objeto es una instancia de {@link NaveJugador}.
     * </p>
     *
     * @param otro el objeto con el cual comprobar colisión.
     * @return {@code true} si hay superposición con la nave del jugador.
     */
    @Override
    public boolean colisionaCon(Colisionable otro) {
        // Solo colisiona con el jugador
        if (otro instanceof NaveJugador) {
            return this.getArea().overlaps(otro.getArea());
        }
        return false;
    }

    /**
     * Define la reacción del power-up al colisionar con otro objeto.
     * <p>
     * En este caso, se marca como destruido al ser recogido por el jugador.
     * </p>
     *
     * @param otro el objeto con el que ha colisionado.
     */
    @Override
    public void alColisionar(Colisionable otro) {
        // Se destruye al ser recogido
        this.destroyed = true;
    }
}