package io.github.SpaceNav;

/**
 * Interfaz que define un objetivo dentro del juego.
 * Cualquier clase que pueda ser objetivo de otras entidades (por ejemplo, enemigos)
 * debe implementar esta interfaz para proporcionar su posición y estado de vida.
 */
public interface Objetivo {
	
	/**
     * Obtiene la coordenada horizontal (X) del objetivo.
     * @return Posición X
     */
	float getX();
    
	/**
     * Obtiene la coordenada vertical (Y) del objetivo.
     * @return Posición Y
     */
	float getY();
    
	/**
     * Indica si el objetivo ha sido destruido.
     * @return true si el objetivo está destruido, false en caso contrario
     */
	boolean estaDestruido();
}