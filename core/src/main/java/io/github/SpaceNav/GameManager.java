package io.github.SpaceNav;

/**
 * [CLASE GM2.1 - PATRÓN SINGLETON]
 * * Esta clase implementa el patrón de diseño Singleton.
 * Se encarga de gestionar el estado global del juego, como el puntaje y las vidas,
 * asegurando que solo exista UNA instancia de este gestor en toda la aplicación.
 * * El acceso se realiza a través del método estático `GameManager.getInstance()`.
 */
public class GameManager {

    // 1. La única instancia (privada y estática)
    // Es 'volatile' para garantizar la visibilidad en todos los hilos (buena práctica).
    private static volatile GameManager instance;

    // 2. Variables de estado (los datos que queremos centralizar)
    private int score;
    private int lives;
    
    // 3. El constructor es PRIVADO
    // Esto evita que cualquier otra clase pueda hacer "new GameManager()".
    private GameManager() {
        // Inicializamos los valores por defecto al crear la instancia
        this.score = 0;
        this.lives = 3; // O las vidas con las que inicie tu jugador
    }

    /**
     * [MÉTODO CLAVE DEL SINGLETON]
     * 4. El método público y estático para obtener la única instancia.
     * Utiliza "Double-Checked Locking" para ser seguro en entornos con múltiples hilos (threads).
     *
     * @return La instancia única de GameManager
     */
    public static GameManager getInstance() {
        // Primer chequeo (sin bloqueo) para eficiencia
        if (instance == null) {
            // Si es nulo, entramos en un bloque sincronizado para evitar
            // que dos hilos creen la instancia al mismo tiempo.
            synchronized (GameManager.class) {
                // Segundo chequeo (dentro del bloqueo)
                if (instance == null) {
                    // Si aún es nulo, creamos la instancia
                    instance = new GameManager();
                }
            }
        }
        // Devuelve la instancia (la nueva o la que ya existía)
        return instance;
    }
    
    // --- Métodos públicos para interactuar con el estado del juego ---
    
    /**
     * Añade puntos al puntaje total.
     * @param points Puntos a sumar
     */
    public void addScore(int points) {
        this.score += points;
        // System.out.println("Score: " + this.score); // Útil para depurar
    }
    
    /**
     * Obtiene el puntaje actual.
     * @return El puntaje total
     */
    public int getScore() {
        return this.score;
    }
    
    /**
     * Resta una vida al jugador.
     */
    public void loseLife() {
        if (this.lives > 0) {
            this.lives -= 1;
            // System.out.println("Vidas: " + this.lives); // Útil para depurar
        }
    }
    
    /**
     * Obtiene las vidas restantes.
     * @return El número de vidas
     */
    public int getLives() {
        return this.lives;
    }
    
    /**
     * Añade una vida al jugador (usado por PowerUp).
     */
    public void addLife(int cantidad) {
        this.lives += 1;
    }

    /**
     * Comprueba si al jugador no le quedan vidas.
     * @return true si las vidas son 0 o menos, false en caso contrario
     */
    public boolean isGameOver() {
        return this.lives <= 0;
    }
    
    /**
     * Reinicia el estado del juego a sus valores iniciales.
     * Esto es crucial para cuando el jugador quiere "Jugar de Nuevo".
     */
    public void resetGame() {
        this.score = 0;
        this.lives = 3;
    }
}