package io.github.SpaceNav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Clase que representa la nave controlada por el jugador.
 * Hereda de {@link NaveBase} y gestiona el input del teclado para moverse y disparar.
 * Además maneja efectos de sonido al recibir daño, disparar o recolectar power-ups.
 * [CAMBIO GM2.3 - PATRÓN STRATEGY (Contexto)]
 * Esta clase ahora actúa como el "Contexto" del patrón Strategy.
 * Delega la lógica de disparo a un objeto IDisparoStrategy.
 */
public class NaveJugador extends NaveBase {

	/** Sonido reproducido al recibir daño */
    private Sound sonidoHerido;
    
    /** Sonido reproducido al disparar una bala */
    private Sound soundBala;
    
    /** Textura utilizada para las balas disparadas */
    private Texture txBala;
    
    /** Textura utilizada para las balas disparadas */
    private Sound soundPowerUp;
    
    /**
     * [CAMBIO GM2.3 - PATRÓN STRATEGY]
     * Almacena la estrategia de disparo actual. Por defecto, será DisparoSimpleStrategy.
     */
    private DisparoStrategy disparoStrategy;

    /**
     * Constructor de la nave del jugador.
     * Inicializa la textura, posición, vidas y los sonidos.
     *
     *[CAMBIO GM2.3] Inicializa la estrategia de disparo por defecto.
     *
     * @param x Posición horizontal inicial
     * @param y Posición vertical inicial
     * @param tx Textura de la nave
     * @param txBala Textura de las balas disparadas
     */
    public NaveJugador(float x, float y, Texture tx, Texture txBala) {
        super(tx, x, y); // Llama al constructor de NaveBase (sin vidas)
        this.txBala = txBala;
        this.sonidoHerido = Gdx.audio.newSound(Gdx.files.internal("hit.mp3"));
        this.soundBala = Gdx.audio.newSound(Gdx.files.internal("shoot.mp3"));
        this.soundPowerUp = Gdx.audio.newSound(Gdx.files.internal("powerup.mp3"));
        // [CAMBIO GM2.3] Establece la estrategia de disparo inicial
        this.disparoStrategy = new DisparoSimpleStrategy();
    }
    
    /**
     * [CAMBIO GM2.2 - PASO ABSTRACTO IMPLEMENTADO]
     * Este método implementa el 'gestionarLogica' de NaveBase.
     * Solo se preocupa de la lógica de Input y disparo.
     * Ya NO llama a actualizarEstadoHerido() ni a mover().
     * [CAMBIO GM2.3] La lógica de disparo ahora se delega a la estrategia.
     *
     * @param juego Instancia de {@link PantallaJuego} para agregar balas
     */
    @Override
    protected void gestionarLogica(PantallaJuego juego) {
        if (!herido) {
            // Lógica de input
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) xVel -= 3;
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) xVel += 3;
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) yVel -= 3;
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) yVel += 3;
        
            // Disparo
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                // [CAMBIO GM2.3 - PATRÓN STRATEGY]
                // Delega la lógica de disparo a la estrategia actual.
                // La NaveJugador ya no "sabe" cómo dispara, solo "ordena" disparar.
                this.disparoStrategy.disparar(this, txBala, juego);
                
                soundBala.play();
            }
        }
    }
    
    /**
     * [CAMBIO GM2.2 - HOOK IMPLEMENTADO]
     * Este método sobrescribe el "hook" 'limitarMovimiento' de NaveBase
     * para implementar la lógica de límites de pantalla, específica del jugador.
     */
    @Override
    protected void limitarMovimiento() {
        // Límites de pantalla
        float x = spr.getX();
        float y = spr.getY();
        
        if (y + yVel < 0 || y + yVel + spr.getHeight() > PantallaJuego.WORLD_HEIGHT) {
            yVel = 0;
        }
        
        if (x + xVel < 0) {
            xVel = 0;
            spr.setX(0);
        }
        if (x + xVel + spr.getWidth() > PantallaJuego.WORLD_WIDTH) {
            xVel = 0;
            spr.setX(PantallaJuego.WORLD_WIDTH - spr.getWidth());
        }
    }
    
    /**
     * Maneja la colisión con otro objeto {@link Colisionable}.
     * Llama al rebote de {@link NaveBase} y aplica daño si colisiona con una nave enemiga.
     *
     * @param otro Objeto colisionable que colisiona con la nave
     */
    @Override
    public void alColisionar(Colisionable otro) {
        super.alColisionar(otro); // Llama al rebote de NaveBase
        
        if (otro instanceof NaveEnemiga) {
            this.recibirDano(1); 
        }
    }

    /**
     * Implementación del método abstracto 'recibirDano' de NaveBase.
     * Esta lógica es específica del JUGADOR.
     * Llama al GameManager (Singleton) para restar vidas.
     *
     * @param dano Cantidad de daño a recibir
     */
    @Override
    public void recibirDano(int dano) {
        if (!herido) {
            // 1. Llama al Singleton para restar una vida global
            GameManager.getInstance().loseLife(); 
            
            // 2. Activa estado herido local
            this.herido = true;
            this.tiempoHerido = this.tiempoHeridoMax;
            
            // 3. Comprueba si el juego terminó (usando el Singleton)
            if (GameManager.getInstance().isGameOver()) {
                this.destruida = true;
            }
            
            // 4. Reproduce sonido
            sonidoHerido.play();
        }
    }

    /**
     * Procesa el efecto de un power-up recolectado.
     * - VIDA: [CAMBIO GM2.1] Llama al GameManager para sumar vidas.
     * - ESCUDO: activa invencibilidad temporal
     * [CAMBIO GM2.3] Añade el caso para DISPARO_DOBLE.
     *
     * @param tipo El {@link TipoPowerUp} que se recogió
     */
    public void recibirPowerUp(TipoPowerUp tipo) {
    	soundPowerUp.play(0.8f);
        switch (tipo) {
            case VIDA:
                GameManager.getInstance().addLife(1);
                break;
            case ESCUDO:
                this.activarInvencibilidad(180); // 3 segundos
                break;
            // [CAMBIO GM2.3 - PATRÓN STRATEGY]
            // Al recibir el power-up, se cambia la estrategia de disparo.
            case DISPARO_DOBLE:
                this.disparoStrategy = new DisparoDobleStrategy();
                break;
        }
    }
    
    /**
     * Libera los recursos de sonido utilizados por la nave.
     */
    public void dispose() {
        sonidoHerido.dispose();
        soundBala.dispose();
        soundPowerUp.dispose();
    }
}