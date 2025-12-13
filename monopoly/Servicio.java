package monopoly;

import partida.Jugador;

public class Servicio extends Propiedad {
    public Servicio(Jugador duenho, String nombre, String tipo, int posicion, float valor, float hipoteca) {
        super(duenho, nombre, tipo, posicion, valor, hipoteca);
    }

    public boolean alquiler() {
        if (this.getDuenho() != MonopolyETSE.juego.getBanca() && !estaHipotecada())
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
