package monopoly;

import partida.Avatar;
import partida.Jugador;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Propiedad extends Casilla {

    private float valor; //Valor de esta casilla (en la mayoría será valor de compra, en la casilla parking se usará como el bote).
    private float impuesto; //Cantidad a pagar por caer en la casilla: el alquiler en solares/servicios/transportes o impuestos.
    private Grupo grupo; //Grupo al que pertenece la casilla (si es solar).
    private float rentabilidad = 0;
    private float hipoteca; //Valor otorgado por hipotecar una casilla ------------- No se si es solo de Solar
    protected boolean hipotecada = false;
    // Para bloquear alquileres y edificaciones
    protected boolean puedeCobrarAlquiler = true;

    public Propiedad(Jugador duenho, String nombre, String tipo, int posicion, float valor, float hipoteca) {
        super(duenho, nombre, tipo, posicion);
        duenho.getPropiedades().add(this);
        setValor(valor);
        setHipoteca(hipoteca);
    }

    public String toString() {
        return super.toString() +
                "\n\tValor: " + valor +
                "\n\tAlquiler: " + impuesto;
    }

    public boolean perteneceAJugador(Jugador jugador) {
        return jugador == this.getDuenho();
    }

    public abstract boolean alquiler();
    public abstract float valor();

    public void comprar(Jugador jugador) {
        if (jugador.getAvatar().getLugar() != this) {
            Juego.consola.imprimir("\t" + Valor.RED + "Error: Debes estar sobre la casilla para comprarla." + Valor.RESET);
            return;
        }

        if (!this.getTipo().equals("Solar") && !this.getTipo().equals("Servicios") && !this.getTipo().equals("Transporte")) {
            Juego.consola.imprimir("\t" + Valor.RED + "Error: La casilla no es adquirible." + Valor.RESET);
            return;
        }

        if(evaluarCasilla(jugador)){
            Propiedad propiedad = (Propiedad) jugador.getAvatar().getCasilla();
            if(propiedad.getDuenho() == MonopolyETSE.juego.getBanca()) { //Si la casilla no tiene dueño, se ofrece comprarla.
                float valor = propiedad.getValor();
                jugador.sumarFortuna(-valor); //Se resta el valor de la casilla a la fortuna del jugador.
                jugador.agregarDineroInvertido(valor);
                MonopolyETSE.juego.getBanca().sumarFortuna(valor); //Se añade el valor de la casilla a la fortuna de laMonopolyETSE.menu.getBanca().

                propiedad.setDuenho(jugador); //El dueño de la casilla pasa a ser el Jugador jugador.
                jugador.añadirPropiedad(propiedad); //Se añade la casilla al arraylist de propiedades del jugador.
                MonopolyETSE.juego.getBanca().eliminarPropiedad(propiedad); //Se elimina la casilla del arraylist de propiedades de laMonopolyETSE.menu.getBanca().

                Juego.consola.imprimir("\t" + Valor.GREEN + "¡Compra realizada con éxito!" + Valor.RESET);
            }
            else {
                Juego.consola.imprimir("\t" + Valor.RED + "Error: La casilla ya tiene dueño." + Valor.RESET); //No es necesario, pero se deja para manejar errores.
            }
        }
        else {
            Juego.consola.imprimir("\t" + Valor.RED + "Error: La casilla ya tiene dueño o no tiene suficiente dinero para comprarla." + Valor.RESET);
        }
    }

    /*Método para añadir valor a una casilla. Utilidad:
    * - Sumar valor a la casilla de parking.
    * - Sumar valor a las casillas de solar al no comprarlas tras cuatro vueltas de todos los jugadores.
    * Este método toma como argumento la cantidad a añadir del valor de la casilla.
    ------------ FALTA EL VALOR DEL SOLAR QUE NO SE CUANTO ES */
    public void sumarValor(float suma) {
        this.valor += suma;
    }


    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public float getHipoteca() {
        return hipoteca;
    }

    public void setHipoteca(float hipoteca) {
        this.hipoteca = hipoteca;
    }

    public float getRentabilidad() {
        return rentabilidad;
    }

    public void setRentabilidad(float rentabilidad) {
        this.rentabilidad = rentabilidad;
    }

    public float getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(float impuesto) {
        this.impuesto = impuesto;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public boolean isHipotecada() {
        return hipotecada;
    }
}
