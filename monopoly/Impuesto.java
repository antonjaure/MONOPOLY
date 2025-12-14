package monopoly;

import partida.Jugador;

public class Impuesto extends Casilla {
    private float impuesto;

    public float getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(float impuesto) {
        this.impuesto = impuesto;
    }

    public Impuesto(Jugador duenho, String nombre, String tipo, int posicion, float impuesto) {
        super(duenho, nombre, tipo, posicion);
        this.setImpuesto(impuesto);
    }

    @Override
    public String toString() {
        return super.toString() +
                "\n\tImpuesto: " + impuesto;
    }
}
