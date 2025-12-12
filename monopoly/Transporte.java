package monopoly;

import partida.Jugador;

public class Transporte extends Propiedad {
    public Transporte(Jugador duenho, String nombre, String tipo, int posicion, float valor, float hipoteca) {
        super(duenho, nombre, tipo, posicion, valor, hipoteca);
    }

    public boolean alquiler() {
        if (this.getDuenho() != MonopolyETSE.menu.getBanca() && !estaHipotecada())
            return true;
        return false;
    }
    public float valor() {
        return this.getValor();
    }
    public boolean estaHipotecada() {
        return this.hipotecada;
    }
}
