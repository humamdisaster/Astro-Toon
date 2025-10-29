package io.github.SpaceNav;

import java.util.ArrayList;
import com.badlogic.gdx.audio.Sound;

public class GestorColisiones {

    public void manejarColisiones(
            NaveJugador nave,
            ArrayList<NaveEnemiga> enemigos,
            ArrayList<Bullet> balas,
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
                            explosionSound.play();
                            enemigos.remove(j);
                            j--;
                            juego.incrementarScore(10); // delega al juego el cambio de score
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
    }
}