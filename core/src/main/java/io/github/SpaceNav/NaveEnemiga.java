package io.github.SpaceNav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class NaveEnemiga extends NaveBase {

    // Puedes añadir atributos específicos del enemigo (p.ej. puntos que da)
    // private int scoreValue = 100;

    public NaveEnemiga(Texture tx, float x, float y, float xVel, float yVel, int vidas) {
        // Llamar al constructor del padre (NaveBase)
        super(tx, x, y, vidas);
        
        // Asignar velocidad inicial
        this.xVel = xVel;
        this.yVel = yVel;
    }

    @Override
    public void update(PantallaJuego juego) {
        // 1. Actualizar estado de "herido" (lógica del padre)
        actualizarEstadoHerido();
        
        // 2. Lógica de IA (Inteligencia Artificial)
        // Por ahora, solo es la lógica de movimiento de Ball2 (rebotar)
        
        float x = spr.getX();
        float y = spr.getY();

        if (x + xVel < 0 || x + xVel + spr.getWidth() > Gdx.graphics.getWidth())
            xVel *= -1;
        if (y + yVel < 0 || y + yVel + spr.getHeight() > Gdx.graphics.getHeight())
            yVel *= -1;

        // 3. Mover la nave (lógica del padre)
        mover();
        
        // FUTURO: Aquí podrías añadir lógica de disparo enemigo
        // if (MathUtils.random(100) < 1) {
        //    juego.agregarBalaEnemiga( ... );
        // }
    }
    
    // La lógica de checkCollision(Ball2 b2) de tu Ball2.java (enemigo vs enemigo)
    // puedes moverla aquí si quieres que los enemigos reboten entre sí.
    public void checkCollision(NaveEnemiga otraNave) {
        if(this.getArea().overlaps(otraNave.getArea())){
        	// Rebote (lógica de Ball2)
            if (xVel ==0) xVel += otraNave.getXVel()/2;
            if (otraNave.getXVel() ==0) otraNave.setXVel(otraNave.getXVel() + xVel/2);
            xVel = - xVel;
            otraNave.setXVel(-otraNave.getXVel());
            
            if (yVel ==0) yVel += otraNave.getYVel()/2;
            if (otraNave.getYVel() ==0) otraNave.setYVel(otraNave.getYVel() + yVel/2);
            yVel = - yVel;
            otraNave.setYVel(- otraNave.getYVel()); 
        }
    }
}