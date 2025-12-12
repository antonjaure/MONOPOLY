package monopoly;

import partida.Jugador;

public class Parking extends Accion {
    private float valor; //Valor de esta casilla (en la mayoría será valor de compra, en la casilla parking se usará como el bote).

    public Parking(Jugador duenho, String nombre, String tipo, int posicion) {
        super(duenho, nombre, tipo, posicion);
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    /*Método para añadir valor a una casilla. Utilidad:
    * - Sumar valor a la casilla de parking.
    * - Sumar valor a las casillas de solar al no comprarlas tras cuatro vueltas de todos los jugadores.
    * Este método toma como argumento la cantidad a añadir del valor de la casilla.
    ------------ FALTA EL VALOR DEL SOLAR QUE NO SE CUANTO ES */
    public void sumarValor(float suma) {
        this.valor += suma;
    }
}
