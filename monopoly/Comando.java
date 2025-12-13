package monopoly;

import excepciones.*;

public interface Comando {

    // ==========================================================
    //                   ACCIONES DE JUEGO
    // ==========================================================

    // Necesita nombre y avatar
    void crearJugador(String nombre, String tipoAvatar);

    // No necesita argumentos (usa el turno actual)
    void lanzarDados() throws DadosException;

    // No necesita argumentos
    void acabarTurno() throws DadosSinLanzarException;

    // No necesita argumentos (usa el jugador del turno)
    void salirCarcel();

    // Necesita saber QUÉ casilla comprar
    void comprar(String nombreCasilla);

    // Necesita saber QUÉ construir (casa, hotel...)
    void edificar(String tipoEdificio);

    // Necesita saber QUÉ, DÓNDE y CUÁNTOS vender
    void vender(String tipoEdificio, String nombreCasilla, int cantidad);

    // Necesita saber QUÉ hipotecar
    void hipotecar(String nombreCasilla);

    // Necesita saber QUÉ deshipotecar
    void deshipotecar(String nombreCasilla);


    // ==========================================================
    //                  COMANDOS DE INFORMACIÓN
    // ==========================================================

    void verTablero();

    // Necesita el nombre del jugador a describir
    void describirJugador(String nombre);

    // Necesita el nombre de la casilla
    void describirCasilla(String nombre);

    void listarEnVenta();

    void listarJugadores();

    void listarEdificios();

    // Puede recibir un nombre o null (si son estadísticas globales)
    void mostrarEstadisticas(String nombreJugador);


    // ==========================================================
    //                  GESTIÓN DEL SISTEMA
    // ==========================================================

    void salir();

    void ayuda();
}
