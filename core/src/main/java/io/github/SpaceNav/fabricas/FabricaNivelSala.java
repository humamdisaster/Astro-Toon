package io.github.SpaceNav.fabricas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;

public class FabricaNivelSala implements FabricaNivel {

    @Override
    public Texture crearFondo() {
        return new Texture(Gdx.files.internal("sala.png"));
    }

    @Override
    public Texture crearTexturaNaveJugador() {
        return new Texture(Gdx.files.internal("gato1.png"));
    }

    @Override
    public Texture crearTexturaNaveEnemiga() {
        return new Texture(Gdx.files.internal("secador.png"));
    }

    @Override
    public Texture crearTexturaBala() {
        return new Texture(Gdx.files.internal("bolaPelo.png"));
    }
    
    @Override
    public Texture crearTexturaVida() {
        return new Texture(Gdx.files.internal("pezVida.png"));
    }

    @Override
    public Texture crearTexturaEscudo() {
        return new Texture(Gdx.files.internal("cajaEscudo.png"));
    }

    @Override
    public Texture crearTexturaDisparoDoble() {
        return new Texture(Gdx.files.internal("bolaPelo.png"));
    }

    @Override
    public Music crearMusicaFondo() {
        return Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
    }
}