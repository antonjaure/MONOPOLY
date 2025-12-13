package monopoly;

import partida.*;

import java.util.ArrayList;

public abstract class Casilla {
    //Atributos:
    private String nombre; //Nombre de la casilla
    private String tipo; //Tipo de casilla (Solar, Especial, Transporte, Servicios, Comunidad, Suerte y Impuesto).

    private int posicion; //Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
    private Jugador duenho; //Dueño de la casilla (por defecto sería la banca).

    private ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.
    public int frecuencia = 0;

    public Casilla() {}

    public Casilla(Jugador duenho, String nombre, String tipo, int posicion) {
        this.duenho = duenho;
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;

        this.avatares = new ArrayList<>();
    }

    public boolean estaAvatar(Avatar av) {
        if (avatares == null || avatares.isEmpty()) return false;
        return avatares.contains(av);
    }

    public void incrementarFrecuencia() {
        this.frecuencia++;
    }

    public int frecuenciaVisita() {
        return this.frecuencia;
    }

    public void anhadirAvatar(Avatar av) {
        avatares.add(av);
    }

    //Método utilizado para eliminar un avatar del array de avatares en casilla.
    public void eliminarAvatar(Avatar av) {
        avatares.remove(av);
    }

    public String toString() {
        return "\tnombre: " + this.nombre +
                "\n\ttipo: " + this.tipo +
                "\n\tpropietario: " + this.duenho;
    }

    public void gestionarPago(Jugador jugadorActual, Jugador banca, int tirada) {

        if (this.duenho == jugadorActual) {
            System.out.println("\tEsta casilla te pertenece.");
            return;
        }

        // Casillas con dueño distinto al jugador actual
        switch (this) {
            case Propiedad p -> {
                // Si la propiedad está hipotecada, no se cobra alquiler
                if (!p.puedeCobrarAlquiler) {
                    System.out.println("\tLa propiedad está hipotecada. No se cobra alquiler.");
                    return;
                }

                // Si la propiedad es de la banca significa que no es de nadie
                if (p.getDuenho() == MonopolyETSE.juego.getBanca()) {
                    System.out.println("\tCasilla en venta.");
                    return;
                }

                if (!evaluarCasilla(jugadorActual)) {
                    System.out.println("\t" + jugadorActual.getNombre() + " no tiene dinero suficiente para pagar el alquiler.");
                    return;
                }

                float cantidad = switch (this) {
                    case Solar s -> {
                        boolean duenhoGrupo = s.getGrupo().esDuenhoGrupo(s.getDuenho());

                        yield duenhoGrupo ? s.getImpuesto() * 2 : s.getImpuesto();
                    }
                    case Transporte t -> t.getImpuesto();
                    case Servicio s -> tirada * 4 * s.getImpuesto();
                    default -> 0;
                };

                jugadorActual.sumarFortuna(-cantidad);
                duenho.sumarFortuna(cantidad);
                if(duenho != MonopolyETSE.juego.getBanca()){
                    float newRentabilidad = p.getRentabilidad() + cantidad;
                    p.setRentabilidad(newRentabilidad);
                }
                System.out.println("\t" + jugadorActual.getNombre() + " paga "+ String.format("%.0f", cantidad) + "€ a " + duenho.getNombre());
                // Actualizar estadísticas
                jugadorActual.agregarPagoDeAlquileres(cantidad);
                duenho.agregarCobroDeAlquileres(cantidad);
            }
            // Casillas sin dueño (o de la banca)
            case Impuesto i -> {
                if (!evaluarCasilla(jugadorActual)) {
                    System.out.println("\t" + jugadorActual.getNombre() + " no tiene dinero para pagar el impuesto.");
                    return;
                }
                float cantidad = i.getImpuesto();
                jugadorActual.sumarFortuna(-cantidad);
                Parking parking = (Parking) MonopolyETSE.juego.getTablero().encontrar_casilla("Parking");
                parking.sumarValor(cantidad);
                System.out.println("\t" + jugadorActual.getNombre() + " paga " + cantidad + "€ que se depositan en el Parking");
                // Actualizar estadísticas
                jugadorActual.agregarPagoTasasEImpuestos(cantidad);
            }
            case Parking p -> {
                Juego.consola.imprimir("\t" + jugadorActual.getNombre() + " recibe el bote de " + p.getValor() + "€");
                // FUNCIONES DE ESTADÍSTICAS SOLICITADAS:
                jugadorActual.sumarFortuna(p.getValor());
                jugadorActual.agregarPremiosInversionesOBote(p.getValor());
                p.setValor(0); // Vaciar el bote
            }
            
            case Especial e -> {
                // Aquí diferenciamos por el tipo o nombre de la casilla especial
                if (e.getTipo().equals("Suerte")) {
                    Juego.consola.imprimir("\t" + jugadorActual.getNombre() + " cae en Suerte.");
                    MonopolyETSE.juego.sacarCartaSuerte();
                } 
                else if (e.getTipo().equals("Comunidad")) {
                    Juego.consola.imprimir("\t" + jugadorActual.getNombre() + " cae en Caja de Comunidad.");
                    MonopolyETSE.juego.sacarCartaComunidad();
                }
                else if (e.getNombre().equals("Salida") || this.posicion - tirada < 0) {
                    Juego.consola.imprimir("\t" + jugadorActual.getNombre() + " cayó en la salida.");
                } else if (e.getNombre().equals("IrCarcel")) {
                    jugadorActual.encarcelar();
                    jugadorActual.incrementarVecesEnCarcel();
                }
            }
            default -> System.out.println("\tCasilla de paso. No pasa nada.");
        }
    }

    public boolean evaluarCasilla(Jugador actual) {


        ////////////////    las casillas de impuestos no se deberian poder comprar


        Avatar avatar = actual.getAvatar();
        Casilla casilla = avatar.getCasilla();
        String tipo = casilla.getTipo();

        switch(casilla) {
            case Propiedad p:
                if(p.getDuenho() == MonopolyETSE.juego.getBanca()) {
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



    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public int getPosicion() {
        return posicion;
    }
    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
    public Jugador getDuenho() {
        return duenho;
    }
    public void setDuenho(Jugador duenho) {
        this.duenho = duenho;
    }
    public ArrayList<Avatar> getAvatares() {
        return avatares;
    }
    public void setAvatares(ArrayList<Avatar> avatares) {
        this.avatares = avatares;
    }
    public int getFrecuencia() {
        return frecuencia;
    }
    public void setFrecuencia(int frecuencia) {
        this.frecuencia = frecuencia;
    }
}
