package io.github.SpaceNav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import io.github.SpaceNav.PantallaJuego; 

/**
 * Representa la nave controlada por el jugador.
 * Hereda de NaveBase y maneja el input del teclado.
 */
public class NaveJugador extends NaveBase {

    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private Sound soundPowerUp;

    public NaveJugador(float x, float y, Texture tx, Texture txBala) {
        super(tx, x, y, 3); 
        this.txBala = txBala;
        this.sonidoHerido = Gdx.audio.newSound(Gdx.files.internal("hit.mp3"));
        this.soundBala = Gdx.audio.newSound(Gdx.files.internal("shoot.mp3"));
        this.soundPowerUp = Gdx.audio.newSound(Gdx.files.internal("powerup.mp3"));
    }

    @Override
    public void update(PantallaJuego juego) {
        actualizarEstadoHerido();

        if (!herido) {
            // Lógica de input
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) xVel--;
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) xVel++;
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) yVel--;
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) yVel++;
        
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
            yVel *= -1;
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
    
    @Override
    public void alColisionar(Colisionable otro) {
        super.alColisionar(otro); // Llama al rebote de NaveBase
        
        if (otro instanceof NaveEnemiga) {
            this.recibirDano(1); 
        }
    }

    @Override
    public void recibirDano(int dano) {
        if (!herido) {
            super.recibirDano(dano); 
            sonidoHerido.play();
        }
    }

    /**
     * Procesa el efecto de un power-up recolectado.
     * @param tipo El TipoPowerUp que se recogió.
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
    
    public void dispose() {
        sonidoHerido.dispose();
        soundBala.dispose();
        soundPowerUp.dispose();
    }
}