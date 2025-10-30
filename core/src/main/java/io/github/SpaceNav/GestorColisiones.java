package io.github.SpaceNav;

import java.util.ArrayList;
import com.badlogic.gdx.audio.Sound;

//Maneja todo tipo de colisiones
public class GestorColisiones {

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