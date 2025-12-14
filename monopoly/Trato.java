package monopoly;

import partida.*;

/*Los atributos referidos a Propone son de los que PROPONE EL TRATO y los atributos con Recibe se refieren
al que RECIBE el trato. */

public class Trato {
    Jugador jugadorPropone = null;
    Jugador jugadorRecibe = null;
    Propiedad propiedadPropone = null;
    Propiedad propiedadRecibe = null;
    float dineroPropone = 0;
    float dineroRecibe = 0;


    public Trato(Jugador jugadorPropone, Jugador jugadorRecibe, Propiedad propiedadPropone, Propiedad propiedadRecibe, float dineroPropone, float dineroRecibe) {
        this.jugadorPropone = jugadorPropone;
        this.jugadorRecibe = jugadorRecibe;
        this.propiedadPropone = propiedadPropone;
        this.propiedadRecibe = propiedadRecibe;
        this.dineroPropone = dineroPropone;
        this.dineroRecibe = dineroRecibe;
        
    }

    public Jugador getJugadorPropone() {
        return jugadorPropone;
    }

    public Jugador getJugadorRecibe() {
        return jugadorRecibe;
    }

    

    public void setJugadorPropone(Jugador jugadorPropone) {
        this.jugadorPropone = jugadorPropone;
    }

    public void setJugadorRecibe(Jugador jugadorRecibe) {
        this.jugadorRecibe = jugadorRecibe;
    }

    

}


