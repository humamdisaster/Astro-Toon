package io.github.SpaceNav;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Clase abstracta que representa una nave genérica en el juego, ya sea del jugador o enemiga.
 * Implementa {@link Colisionable} y {@link Objetivo}, definiendo la lógica común de:
 * - Movimiento
 * - Vida y daño
 * - Estado de invencibilidad tras recibir daño
 * - Colisiones entre naves
 */
public abstract class NaveBase implements Colisionable, Objetivo {
	
	/** Sprite que representa la nave */
    protected Sprite spr;
    
    /** Velocidad horizontal de la nave */
    protected float xVel = 0;
    
    /** Velocidad vertical de la nave */
    protected float yVel = 0;
    
    /** Cantidad de vidas actuales */
    protected int vidas;
    
    /** Indica si la nave está temporalmente herida/invencible */
    protected boolean herido = false;
    
    /** Indica si la nave ha sido destruida */
    protected boolean destruida = false;
    
    /** Contador de duración del estado herido */
    protected int tiempoHerido;
    
    /** Duración máxima del estado herido por defecto */
    protected int tiempoHeridoMax = 50; 

    /**
     * Constructor de la nave.
     * Inicializa el sprite, la posición y la cantidad de vidas.
     *
     * @param tx Textura de la nave
     * @param x Posición horizontal inicial
     * @param y Posición vertical inicial
     * @param vidas Cantidad de vidas iniciales
     */
    public NaveBase(Texture tx, float x, float y, int vidas) {
        this.vidas = vidas;
        spr = new Sprite(tx);
        spr.setBounds(x, y, 90, 90); 
    }

    /**
     * Actualiza la lógica de la nave en cada frame.
     * Método abstracto que debe implementarse en las subclases.
     *
     * @param juego Instancia de {@link PantallaJuego} para acceder al estado del juego
     */
    public abstract void update(PantallaJuego juego);

    /**
     * Dibuja la nave en pantalla.
     * Aplica un efecto de "parpadeo" cuando la nave está herida.
     *
     * @param batch SpriteBatch usado para el renderizado
     */
    public void draw(SpriteBatch batch) {
        if (herido && !destruida) {
            float xOriginal = spr.getX();
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.draw(batch);
            spr.setX(xOriginal); 
        } else if (!destruida) { 
            spr.draw(batch);
        }
    }

    /**
     * Aplica la velocidad actual al sprite para mover la nave.
     */
    protected void mover() {
        spr.setPosition(spr.getX() + xVel, spr.getY() + yVel);
    }
    
    /**
     * Actualiza el estado de herido/invencibilidad decrementando el temporizador.
     */
    protected void actualizarEstadoHerido() {
        if (herido) {
            tiempoHerido--;
            if (tiempoHerido <= 0) {
                herido = false;
            }
        }
    }

    /**
     * Aplica daño a la nave, activando el estado de herido si no estaba previamente.
     * Si las vidas llegan a 0, la nave se marca como destruida.
     *
     * @param dano Cantidad de daño a aplicar
     */
    public void recibirDano(int dano) {
        if (!herido) {
            this.vidas -= dano;
            this.herido = true;
            this.tiempoHerido = this.tiempoHeridoMax;
            
            if (this.vidas <= 0) {
                this.destruida = true;
            }
        }
    }
    
    /**
     * Obtiene el área de colisión de la nave.
     * @return Un {@link Rectangle} que representa la zona de colisión
     */
    @Override
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }
    
    /**
     * Determina si esta nave colisiona con otra nave.
     * @param otro Otro objeto que implementa {@link Colisionable}
     * @return true si hay colisión, false en caso contrario
     */
    @Override
    public boolean colisionaCon(Colisionable otro) {
        if (otro instanceof NaveBase) {
            if (this == otro) {
                return false;
            }
            return this.getArea().overlaps(otro.getArea());
        }
        return false;
    }

    /**
     * Aplica la reacción física ante una colisión con otra nave.
     * Ajusta velocidades para simular rebote.
     *
     * @param otro Otro objeto {@link Colisionable} que colisiona
     */
    @Override
    public void alColisionar(Colisionable otro) {
        if (otro instanceof NaveBase) {
            NaveBase otraNave = (NaveBase) otro; 
            
            if (xVel == 0) xVel += otraNave.getXVel() / 2;
            if (otraNave.getXVel() == 0) otraNave.setXVel(otraNave.getXVel() + xVel);
            xVel = -xVel;
            otraNave.setXVel(-otraNave.getXVel());
            
            if (yVel == 0) yVel += otraNave.getYVel() / 2;
            if (otraNave.getYVel() == 0) otraNave.setYVel(otraNave.getYVel() + yVel);
            yVel = -yVel;
            otraNave.setYVel(-otraNave.getYVel());
        }
    }

    /**
     * Activa el estado de invencibilidad (herido) por una duración específica.
     * Usado, por ejemplo, por el Power-Up de Escudo.
     *
     * @param duracion Número de frames que durará la invencibilidad
     */
    public void activarInvencibilidad(int duracion) {
        this.herido = true;
        this.tiempoHerido = duracion;
    }

    // --- Getters y Setters ---
    
    /**
     * Indica si la nave ha sido destruida.
     * @return true si la nave está destruida, false en caso contrario
     */
    public boolean estaDestruido() {
        return destruida;
    }

    /**
     * Indica si la nave está en estado de herido/invencibilidad.
     * @return true si la nave está herida, false en caso contrario
     */
    public boolean estaHerido() {
        return herido;
    }

    /**
     * Obtiene la cantidad de vidas actuales de la nave.
     * @return Número de vidas restantes
     */
    public int getVidas() {
        return vidas;
    }
    
    /**
     * Establece la cantidad de vidas de la nave.
     * @param vidas Nueva cantidad de vidas
     */
    public void setVidas(int vidas) {
    	this.vidas = vidas;
    }

    /**
     * Obtiene la posición horizontal de la nave.
     * @return Coordenada X del sprite
     */
    public float getX() {
        return spr.getX();
    }

    /**
     * Obtiene la posición vertical de la nave.
     * @return Coordenada Y del sprite
     */
    public float getY() {
        return spr.getY();
    }
    
    /**
     * Obtiene la velocidad horizontal de la nave.
     * @return Velocidad en el eje X
     */
    public float getXVel() {
    	return xVel;
    }
    
    /**
     * Obtiene la velocidad vertical de la nave.
     * @return Velocidad en el eje Y
     */
    public float getYVel() {
    	return yVel;
    }
    
    /**
     * Establece la velocidad horizontal de la nave.
     * @param xVel Nueva velocidad en el eje X
     */
    public void setXVel(float xVel) {
    	this.xVel = xVel;
    }
    
    /**
     * Establece la velocidad vertical de la nave.
     * @param yVel Nueva velocidad en el eje Y
     */
    public void setYVel(float yVel) {
    	this.yVel = yVel;
    }
}