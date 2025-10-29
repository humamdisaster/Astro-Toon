package io.github.SpaceNav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class NaveJugador extends NaveBase {

    // Atributos específicos del jugador
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;

    public NaveJugador(float x, float y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        // Llamar al constructor del padre (NaveBase)
        super(tx, x, y, 3); // 3 vidas por defecto para el jugador
        
        // Inicializar atributos específicos
        this.sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
    }

    @Override
    public void update(PantallaJuego juego) {
        // 1. Actualizar estado de "herido" (lógica del padre)
        actualizarEstadoHerido();

        // 2. Solo procesar input y movimiento si no está herido
        if (!herido) {
            // Lógica de input (tomada de tu Nave4)
            // NOTA: Esta lógica de velocidad es extraña (acumulativa).
            // Quizás quieras cambiarla a "xVel = 5" en lugar de "xVel++".
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) xVel--;
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) xVel++;
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) yVel--;
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) yVel++;
        
            // Lógica de disparo
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                Bullet bala = new Bullet(spr.getX() + spr.getWidth() / 2 - 5, spr.getY() + spr.getHeight() - 5, 0, 3, txBala);
                juego.agregarBala(bala);
                soundBala.play();
            }
        }
        
        // 3. Mantenerse en pantalla (lógica de Nave4)
        float x = spr.getX();
        float y = spr.getY();
        if (x + xVel < 0 || x + xVel + spr.getWidth() > Gdx.graphics.getWidth())
            xVel *= -1;
        if (y + yVel < 0 || y + yVel + spr.getHeight() > Gdx.graphics.getHeight())
            yVel *= -1;

        // 4. Mover la nave (lógica del padre)
        mover();
    }

    /**
     * Método para manejar la colisión con un enemigo (lo que antes era Ball2).
     * Ahora debes pasarle un NaveEnemiga en lugar de Ball2.
     */
    public boolean checkCollision(NaveEnemiga enemigo) {
        if (!herido && enemigo.getArea().overlaps(this.getArea())) {
            
            // Lógica de rebote (tomada de tu Nave4)
            if (xVel == 0) xVel += enemigo.getXVel() / 2;
            if (enemigo.getXVel() == 0) enemigo.setXVel(enemigo.getXVel() + xVel / 2);
            xVel = -xVel;
            enemigo.setXVel(-enemigo.getXVel());

            if (yVel == 0) yVel += enemigo.getYVel() / 2;
            if (enemigo.getYVel() == 0) enemigo.setYVel(enemigo.getYVel() + yVel / 2);
            yVel = -yVel;
            enemigo.setYVel(-enemigo.getYVel());

            // Usar el método unificado para recibir daño
            recibirDano(1);
            sonidoHerido.play(); // El sonido se reproduce al recibir daño
            
            return true;
        }
        return false;
    }
    
    // Sobrescribimos recibirDano para añadir el sonido específico del jugador
    @Override
    public void recibirDano(int dano) {
        if (!herido) {
            super.recibirDano(dano); // Llama a la lógica del padre
            sonidoHerido.play(); // Añade el sonido
        }
    }
}