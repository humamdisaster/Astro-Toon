package io.github.SpaceNav;

import java.util.ArrayList;
import com.badlogic.gdx.audio.Sound;

/**
 * Clase responsable de gestionar todas las colisiones entre los objetos
 * del juego, incluyendo jugador, enemigos, balas y power-ups.
 * Se encarga de detectar colisiones y aplicar las consecuencias correspondientes,
 * como daño, destrucción de objetos, incremento de puntuación o activación de efectos.
 */
public class GestorColisiones {

	/**
     * Maneja todas las colisiones del juego en el estado actual.
     * Comprueba colisiones entre balas y enemigos, entre enemigos, 
     * entre el jugador y enemigos, y entre el jugador y power-ups.
     * Aplica los efectos correspondientes a cada interacción, incluyendo
     * reproducción de sonidos, eliminación de objetos y activación de power-ups.
     *
     * @param nave Nave del jugador
     * @param enemigos Lista de enemigos activos en la pantalla
     * @param balas Lista de balas activas en la pantalla
     * @param powerUps Lista de power-ups presentes en la pantalla
     * @param explosionSound Sonido que se reproduce al destruir un enemigo
     * @param juego Instancia de {@link PantallaJuego} para actualizar score y gestionar eventos
     */
    public void manejarColisiones(
            NaveJugador nave,
            ArrayList<NaveEnemiga> enemigos,
            ArrayList<Bullet> balas,
            ArrayList<PowerUp> powerUps,  // <-- agregado
            Sound explosionSound,
            PantallaJuego juego) {

        if (!nave.estaHerido()) {

            // --- Balas vs Enemigos ---
            for (int i = 0; i < balas.size(); i++) {
                Bullet b = balas.get(i);
                for (int j = 0; j < enemigos.size(); j++) {
                    NaveEnemiga enemigo = enemigos.get(j);
                    if (b.colisionaCon(enemigo)) {
                        b.alColisionar(enemigo);
                        if (enemigo.estaDestruido()) {
                            explosionSound.play(0.3f);
                            enemigos.remove(j);
                            j--;
                            juego.incrementarScore(10);
                            
                            juego.soltarPowerUp(enemigo.getX(), enemigo.getY());
                        }
                    }
                }
            }

            // --- Enemigos vs Enemigos ---
            for (int i = 0; i < enemigos.size(); i++) {
                NaveEnemiga e1 = enemigos.get(i);
                for (int j = i + 1; j < enemigos.size(); j++) {
                    NaveEnemiga e2 = enemigos.get(j);
                    if (e1.colisionaCon(e2)) {
                        e1.alColisionar(e2);
                    }
                }
            }

            // --- Jugador vs Enemigos ---
            for (int i = 0; i < enemigos.size(); i++) {
                NaveEnemiga enemigo = enemigos.get(i);
                if (nave.colisionaCon(enemigo)) {
                    nave.alColisionar(enemigo);
                    enemigos.remove(i);
                    i--;
                }
            }
        }

        // --- Jugador vs PowerUps ---
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp p = powerUps.get(i);
            if (p.colisionaCon(nave)) {
                nave.recibirPowerUp(p.getTipo());
                p.alColisionar(nave);
                powerUps.remove(i); // <-- eliminar para que desaparezca
                i--;
            }
        }
    }
}