package monopoly;

import partida.Avatar;
import partida.Jugador;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Propiedad extends Casilla {
    private ArrayList<Edificio> edificios;

    private float valor; //Valor de esta casilla (en la mayoría será valor de compra, en la casilla parking se usará como el bote).
    private float impuesto; //Cantidad a pagar por caer en la casilla: el alquiler en solares/servicios/transportes o impuestos.
    private float rentabilidad = 0;
    private float hipoteca; //Valor otorgado por hipotecar una casilla ------------- No se si es solo de Solar
    protected boolean hipotecada = false;

    public Propiedad(Jugador duenho, String nombre, String tipo, int posicion, float valor, float hipoteca) {
        super(duenho, nombre, tipo, posicion);
        duenho.getPropiedades().add(this);
        setValor(valor);
        setHipoteca(hipoteca);
    }

    // Para bloquear alquileres y edificaciones
    protected boolean puedeCobrarAlquiler = true;

    public boolean perteneceAJugador(Jugador jugador) {
        return jugador == this.getDuenho();
    }

    public abstract boolean alquiler();
    public abstract float valor();

    public boolean evaluarCasilla(Jugador actual) {


        ////////////////    las casillas de impuestos no se deberian poder comprar


        Avatar avatar = actual.getAvatar();
        Casilla casilla = avatar.getCasilla();
        String tipo = casilla.getTipo();

        switch(casilla) {
            case Propiedad p:
                if(p.getDuenho() == MonopolyETSE.menu.getBanca()) {
                    return actual.getFortuna() >= p.getValor(); // Si tiene dinero comprarla = true, else false
                }
                else if(p.getDuenho() != actual) {
                    float impuesto = p.getImpuesto();
                    return actual.getFortuna() >= impuesto; // Si tiene dinero para el alquiler = true, else false
                }
                else if(p.getDuenho() == actual) {
                    return true; //No pasa nada, es su casilla.
                }
                break;

            /*
            case Servicio s:
                if(s.getDuenho() == MonopolyETSE.menu.getBanca()) {
                    return actual.getFortuna() >= s.getValor(); // Si tiene dinero comprarla = true, else false
                }

                // El caso de (duenho != actual) se maneja con la llamada a evaluarCasilla con tirada.

                else if(casilla.getDuenho() == actual) {
                    return true; // No pasa nada, es su casilla.
                }
                break;
            */

            case Impuesto i:   /////////////    *** esto hace que pague el impuesto, pero también permite que compre la casilla ***
                float impuesto = i.getImpuesto();
                return actual.getFortuna() >= impuesto; // Si tiene dinero para el impuesto = true, else false

            case Accion a:
                return true; //No pasa nada, se roba carta.
            case Especial e:
                String nombre = e.getNombre();

                if(nombre.equals("IrCarcel") || nombre.equals("Salida")){
                    return true; //No pasa nada especial al caer en estas casillas.
                }
                else if(nombre.equals("Cárcel")){
                    float salidaCarcel = e.getImpuesto();
                    return actual.getFortuna() >= salidaCarcel; // Si tiene dinero para pagar la cárcel = true, else false
                }
                System.err.println("\nError al evaluarCasilla().\n");
                return false;
            default:
                System.err.println("\nError al evaluarCasilla().\n");
                return false;
        }
        return false;
    }

    public void comprar(Jugador jugador) {
        if (jugador.getAvatar().getLugar() != this) {
            System.out.println("\t" + Valor.RED + "Error: Debes estar sobre la casilla para comprarla." + Valor.RESET);
            return;
        }

        if (!this.getTipo().equals("Solar") && !this.getTipo().equals("Servicios") && !this.getTipo().equals("Transporte")) {
            System.out.println("\t" + Valor.RED + "Error: La casilla no es adquirible." + Valor.RESET);
            return;
        }

        if(evaluarCasilla(jugador)){
            Propiedad propiedad = (Propiedad) jugador.getAvatar().getCasilla();
            if(propiedad.getDuenho() == MonopolyETSE.menu.getBanca()) { //Si la casilla no tiene dueño, se ofrece comprarla.
                float valor = propiedad.getValor();
                jugador.sumarFortuna(-valor); //Se resta el valor de la casilla a la fortuna del jugador.
                jugador.agregarDineroInvertido(valor);
                MonopolyETSE.menu.getBanca().sumarFortuna(valor); //Se añade el valor de la casilla a la fortuna de laMonopolyETSE.menu.getBanca().

                propiedad.setDuenho(jugador); //El dueño de la casilla pasa a ser el Jugador jugador.
                jugador.añadirPropiedad(propiedad); //Se añade la casilla al arraylist de propiedades del jugador.
                MonopolyETSE.menu.getBanca().eliminarPropiedad(propiedad); //Se elimina la casilla del arraylist de propiedades de laMonopolyETSE.menu.getBanca().

                System.out.println("\t" + Valor.GREEN + "¡Compra realizada con éxito!" + Valor.RESET);
            }
            else {
                System.out.println("\t" + Valor.RED + "Error: La casilla ya tiene dueño." + Valor.RESET); //No es necesario, pero se deja para manejar errores.
            }
        }
        else {
            System.out.println("\t" + Valor.RED + "Error: La casilla ya tiene dueño o no tiene suficiente dinero para comprarla." + Valor.RESET);
        }
    }


    public ArrayList<Edificio> getEdificios() {
        return edificios;
    }

    public void setEdificios(ArrayList<Edificio> edificios) {
        this.edificios = edificios;
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
