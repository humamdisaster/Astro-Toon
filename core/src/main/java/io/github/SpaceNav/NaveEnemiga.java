package io.github.SpaceNav;

import com.badlogic.gdx.graphics.Texture;

/**
 * Clase que representa a una nave enemiga del juego.
 * Hereda de {@link NaveBase} y persigue un objetivo (normalmente el jugador).
 * Implementa la lógica de movimiento autónomo hacia el objetivo y gestión de estado herido.
 */
public class NaveEnemiga extends NaveBase {
	
	/** Referencia al objetivo que la nave enemiga sigue, generalmente el jugador */
    private Objetivo objetivo; // referencia al jugador

    /**
     * Constructor de la nave enemiga.
     * Inicializa la textura, la posición, las vidas y asigna el objetivo.
     *
     * @param tx Textura de la nave enemiga
     * @param objetivo Objetivo que la nave perseguirá (por ejemplo, la nave del jugador)
     * @param x Posición horizontal inicial
     * @param y Posición vertical inicial
     * @param vidas Cantidad de vidas iniciales
     */
    public NaveEnemiga(Texture tx, Objetivo objetivo, float x, float y, int vidas) {
        super(tx, x, y, vidas);
        this.objetivo = objetivo;
    }

    /**
     * Actualiza la lógica de la nave enemiga en cada frame.
     * - Gestiona el estado de herido/invencibilidad.
     * - Calcula el movimiento hacia el objetivo si éste existe y no está destruido.
     *
     * @param juego Instancia de {@link PantallaJuego} para acceder al estado del juego
     */
    @Override
    public void update(PantallaJuego juego) {
        actualizarEstadoHerido();

        if (objetivo == null || objetivo.estaDestruido()) return;
        
        // Movimiento hacia la nave
        float dx = objetivo.getX() - spr.getX();
        float dy = objetivo.getY() - spr.getY();
        float distancia = (float)Math.sqrt(dx*dx + dy*dy);

        float velocidad = 4; // ajusta para que sea más rápida o lenta
        if (distancia > 0) {
            xVel = velocidad * dx / distancia;
            yVel = velocidad * dy / distancia;
        }

        mover();
    }
}
