package monopoly;

import excepciones.*;
import partida.*;

public interface Comando {

    void crearJugador(String nombre, String tipoAvatar);

    void lanzarDados() throws DadosException;

    void acabarTurno() throws DadosSinLanzarException;

    void salirCarcel();

    void comprar(String nombreCasilla);


    void edificar(String tipoEdificio);

    void vender(String tipoEdificio, String nombreCasilla, int cantidad);

    void hipotecar(String nombreCasilla);

    void deshipotecar(String nombreCasilla);

    void proponerTrato(String comando) throws TratosException;

    void listarTratos();

    void eliminarTrato(int idTrato) throws TratosException;

    void aceptarTrato(int idTrato) throws TratosException;



    void verTablero();

    void descJugador(String nombre);

    void descCasilla(String nombre);

    void listarVenta();

    void listarJugadores();

    void listarEdificios();

    void mostrarEstadisticas(String nombreJugador);



    void salir();

    void ayuda();
}
