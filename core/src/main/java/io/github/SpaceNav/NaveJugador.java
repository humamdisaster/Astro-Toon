package io.github.SpaceNav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Clase que representa la nave controlada por el jugador.
 * Hereda de {@link NaveBase} y gestiona el input del teclado para moverse y disparar.
 * Además maneja efectos de sonido al recibir daño, disparar o recolectar power-ups.
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
     * Constructor de la nave del jugador.
     * Inicializa la textura, posición, vidas y los sonidos.
     *
     * @param x Posición horizontal inicial
     * @param y Posición vertical inicial
     * @param tx Textura de la nave
     * @param txBala Textura de las balas disparadas
     */
    public NaveJugador(float x, float y, Texture tx, Texture txBala) {
        super(tx, x, y, 3); 
        this.txBala = txBala;
        this.sonidoHerido = Gdx.audio.newSound(Gdx.files.internal("hit.mp3"));
        this.soundBala = Gdx.audio.newSound(Gdx.files.internal("shoot.mp3"));
        this.soundPowerUp = Gdx.audio.newSound(Gdx.files.internal("powerup.mp3"));
    }

    /**
     * Actualiza la lógica de la nave del jugador en cada frame.
     * - Gestiona el estado de herido/invencibilidad
     * - Procesa input de teclado para moverse y disparar
     * - Mantiene la nave dentro de los límites de la pantalla
     *
     * @param juego Instancia de {@link PantallaJuego} para agregar balas y acceder al estado del juego
     */
    @Override
    public void update(PantallaJuego juego) {
        actualizarEstadoHerido();

        if (!herido) {
            // Lógica de input
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) xVel -= 3;
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) xVel += 3;
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) yVel -= 3;
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) yVel += 3;
        
            // Disparo
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                Bullet bala = new Bullet(spr.getX() + spr.getWidth() - 5, spr.getY() + spr.getHeight() / 2 - 5, 3, 0, txBala);
                juego.agregarBala(bala);
                soundBala.play();
            }
        }
        
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

        mover();
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
     * Aplica daño a la nave, reproduciendo el sonido correspondiente.
     * @param dano Cantidad de daño a recibir
     */
    @Override
    public void recibirDano(int dano) {
        if (!herido) {
            super.recibirDano(dano); 
            sonidoHerido.play();
        }
    }

    /**
     * Procesa el efecto de un power-up recolectado.
     * - VIDA: incrementa las vidas del jugador
     * - ESCUDO: activa invencibilidad temporal
     *
     * @param tipo El {@link TipoPowerUp} que se recogió
     */
    public void recibirPowerUp(TipoPowerUp tipo) {
    	soundPowerUp.play(0.8f);
        switch (tipo) {
            case VIDA:
                this.setVidas(this.getVidas() + 1);
                break;
            case ESCUDO:
                this.activarInvencibilidad(180); // 3 segundos
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