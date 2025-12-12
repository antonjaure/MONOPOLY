package monopoly;

import partida.Jugador;

public abstract class Carta {
    protected String descripcion;

    public Carta(String descripcion){
        this.descripcion = descripcion;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public abstract void accion(Jugador jugador);
}
