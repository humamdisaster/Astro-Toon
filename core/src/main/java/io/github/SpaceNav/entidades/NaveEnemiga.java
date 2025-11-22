package io.github.SpaceNav.entidades;

import com.badlogic.gdx.graphics.Texture;

import io.github.SpaceNav.interfaces.Objetivo;
import io.github.SpaceNav.pantallas.PantallaJuego;

/**
 * Clase que representa a una nave enemiga del juego.
 * Hereda de {@link NaveBase} y persigue un objetivo (normalmente el jugador).
 * Implementa la lógica de movimiento autónomo hacia el objetivo y gestión de estado herido.
 */
public class NaveEnemiga extends NaveBase {
	
	/** Referencia al objetivo que la nave enemiga sigue, generalmente el jugador */
    private Objetivo objetivo; // referencia al jugador
    
    /** Cantidad de vidas propias del enemigo (separadas de las de NaveBase) */
    private int vidasEnemigo;
    
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
        super(tx, x, y); // Llama al constructor de NaveBase (sin vidas)
        this.objetivo = objetivo;
        this.vidasEnemigo = vidas; // Guarda vidas ENEMIGAS localmente
    }
    
    /**
     * [CAMBIO GM2.2 - PASO ABSTRACTO IMPLEMENTADO]
     * Este método implementa el 'gestionarLogica' de NaveBase.
     * Solo se preocupa de la lógica de IA (perseguir al objetivo).
     * Ya NO llama a actualizarEstadoHerido() ni a mover().
     *
     * @param juego Instancia de {@link PantallaJuego}
     */
    @Override
    protected void gestionarLogica(PantallaJuego juego) {
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
    }

    /**
     * Implementación del método abstracto 'recibirDano' de NaveBase.
     * Esta lógica es específica del ENEMIGO.
     * Utiliza su variable 'vidas' local.
     * NO llama al GameManager.
     *
     * @param dano Cantidad de daño a recibir
     */
    @Override
    public void recibirDano(int dano) {
        if (!herido) {
            this.vidasEnemigo -= dano;
            this.herido = true;
            this.tiempoHerido = this.tiempoHeridoMax;
            
            if (this.vidasEnemigo <= 0) {
                this.destruida = true;
            }
        }
    }
}