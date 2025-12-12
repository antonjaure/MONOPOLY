package monopoly;

import partida.Jugador;

public class Especial extends Casilla {
    private float impuesto;

    public Especial(Jugador duenho, String nombre, String tipo, int posicion) {
        super(duenho, nombre, tipo, posicion);
    }

    public float getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(float impuesto) {
        this.impuesto = impuesto;
    }
}
