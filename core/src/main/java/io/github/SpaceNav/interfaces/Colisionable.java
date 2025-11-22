package io.github.SpaceNav.interfaces;

import com.badlogic.gdx.math.Rectangle;

/**
 * Define un objeto que puede interactuar (colisionar) con otros objetos.
 */
public interface Colisionable {
    
    /**
     * Retorna el rectángulo que define el área de colisión del objeto.
     * @return El Rectangle del sprite.
     */
    public Rectangle getArea();
    
    /**
     * Verifica si este objeto está colisionando con otro.
     * @param otro El otro objeto Colisionable.
     * @return true si hay colisión, false si no.
     */
    public boolean colisionaCon(Colisionable otro);
    
    /**
     * Define la acción a tomar cuando ocurre una colisión.
     * @param otro El objeto con el que se colisionó.
     */
    public void alColisionar(Colisionable otro);
}
