package monopoly;

import partida.Jugador;

import java.util.ArrayList;

public class CasillaCom extends Accion{
    private ArrayList<Carta> baraja;

    public CasillaCom(Jugador duenho, String nombre, String tipo, int posicion) {
        super(duenho, nombre, tipo, posicion);
        baraja = MonopolyETSE.juego.getBarajaComunidad();
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
