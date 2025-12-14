package monopoly;

import partida.Jugador;

import java.util.ArrayList;

public class CasillaSuerte extends Accion{
    private ArrayList<Carta> baraja;

    public CasillaSuerte(Jugador duenho, String nombre, String tipo, int posicion) {
        super(duenho, nombre, tipo, posicion);
        baraja = MonopolyETSE.juego.getBarajaSuerte();
    }

    @Override
    public String toString() {
        return super.toString() +
                "\n\tBaraja: " + baraja.size() + " cartas";
    }

    public ArrayList<Carta> getBaraja() {
        return baraja;
    }

    public void setBaraja(ArrayList<Carta> baraja) {
        this.baraja = baraja;
    }
}
