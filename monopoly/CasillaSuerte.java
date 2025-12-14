package monopoly;

import partida.Jugador;

import java.util.ArrayList;

public class CasillaSuerte extends Accion{
    private ArrayList<Carta> baraja;

    public CasillaSuerte(Jugador duenho, String nombre, String tipo, int posicion) {
        super(duenho, nombre, tipo, posicion);
        baraja = MonopolyETSE.juego.getBarajaSuerte();
    }

    public ArrayList<Carta> getBaraja() {
        return baraja;
    }

    public void setBaraja(ArrayList<Carta> baraja) {
        this.baraja = baraja;
    }
}
