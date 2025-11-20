package io.github.SpaceNav.fabricas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Music;

public interface FabricaNivel {
    Texture crearFondo();
    Texture crearTexturaNaveJugador();
    Texture crearTexturaNaveEnemiga();
    Texture crearTexturaBala();
    Texture crearTexturaVida();
    Texture crearTexturaEscudo();
    Texture crearTexturaDisparoDoble();
    Music crearMusicaFondo();
}