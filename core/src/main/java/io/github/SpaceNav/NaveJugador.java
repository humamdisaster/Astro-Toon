package io.github.SpaceNav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class NaveJugador extends NaveBase {

    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;

    public NaveJugador(float x, float y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        super(tx, x, y, 3); 
        this.sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
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
        
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                Bullet bala = new Bullet(spr.getX() + spr.getWidth() / 2 - 5, spr.getY() + spr.getHeight() - 5, 0, 3, txBala);
                juego.agregarBala(bala);
                soundBala.play();
            }
        }
        
        // Mantenerse en pantalla
        float x = spr.getX();
        float y = spr.getY();
        if (x + xVel < 0 || x + xVel + spr.getWidth() > Gdx.graphics.getWidth())
            xVel *= -1;
        if (y + yVel < 0 || y + yVel + spr.getHeight() > Gdx.graphics.getHeight())
            yVel *= -1;

        mover();
    }
    
    /**
     * Sobrescribimos 'alColisionar' para añadir la lógica de daño
     * específica del jugador.
     */
    @Override
    public void alColisionar(Colisionable otro) {
        super.alColisionar(otro); // 1. Llama al rebote de NaveBase
        
        // 2. Añade la lógica de daño del jugador
        if (otro instanceof NaveEnemiga) {
            this.recibirDano(1); // El sonido se maneja en recibirDano
        }
    }

    @Override
    public void recibirDano(int dano) {
        if (!herido) {
            super.recibirDano(dano); // Llama a la lógica del padre
            sonidoHerido.play(); // Añade el sonido
        }
    }
}