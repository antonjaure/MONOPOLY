package monopoly;

import partida.Jugador;

public class Especial extends Casilla {
    private float impuesto; // coste para salir de la carcel, 0 en el resto

    public Especial(Jugador duenho, String nombre, String tipo, int posicion) {
        super(duenho, nombre, tipo, posicion);
    }

    @Override
    public String toString() {
        if (this.getNombre().equals("Cárcel")) {
            return super.toString() +
                    "\n\tFianza: " + impuesto;
        }
        else return super.toString();
    }

    public float getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(float impuesto) {
        this.impuesto = impuesto;
    }
}
