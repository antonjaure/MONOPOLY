package monopoly;

import partida.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;


public class Casilla {

    //Atributos:
    private String nombre; //Nombre de la casilla
    private String tipo; //Tipo de casilla (Solar, Especial, Transporte, Servicios, Comunidad, Suerte y Impuesto).
    private float valor; //Valor de esa casilla (en la mayoría será valor de compra, en la casilla parking se usará como el bote).
    private int posicion; //Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
    private Jugador duenho; //Dueño de la casilla (por defecto sería la banca).
    private Grupo grupo; //Grupo al que pertenece la casilla (si es solar).
    private float impuesto; //Cantidad a pagar por caer en la casilla: el alquiler en solares/servicios/transportes o impuestos.
    private float hipoteca; //Valor otorgado por hipotecar una casilla
    private ArrayList<Edificio> edificios;
    private HashMap<String, Float> valores = new HashMap<>();
    private HashMap<String, Float> alquileres = new HashMap<>();
    private ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.
    private float rentabilidad = 0;
    public int frecuencia = 0;
    // Nuevo atributo para la hipoteca
    protected boolean hipotecada = false;

    // Para bloquear alquileres y edificaciones
    protected boolean puedeCobrarAlquiler = true;

    //Constructores:
    public Casilla() {
    }//Parámetros vacíos

    /*Constructor para casillas tipo Solar, Servicios o Transporte:
    * Parámetros: nombre casilla, tipo (debe ser solar, serv. o transporte), posición en el tablero, valor y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, float valor, Jugador duenho) {
        setNombre(nombre);
        setTipo(tipo);
        setPosicion(posicion);
        setValor(valor);

        setDuenho(duenho);
        duenho.getPropiedades().add(this);

        this.avatares = new ArrayList<>();
    }

    /*Constructor utilizado para inicializar las casillas de tipo IMPUESTOS.
    * Parámetros: nombre, posición en el tablero, impuesto establecido y dueño.
     */
    public Casilla(float impuesto, String nombre, String tipo, int posicion, Jugador duenho) {
        setImpuesto(impuesto);
        setNombre(nombre);
        setTipo(tipo);
        setPosicion(posicion);
        setDuenho(duenho);

        this.avatares = new ArrayList<>();
    }

    /*Constructor utilizado para crear las otras casillas (Suerte, Caja de comunidad y Especiales):
    * Parámetros: nombre, tipo de la casilla (será uno de los que queda), posición en el tablero y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, Jugador duenho) {
        setNombre(nombre);
        setTipo(tipo);
        setPosicion(posicion);
        setDuenho(duenho);

        this.avatares = new ArrayList<>();
    }

    //Método utilizado para añadir un avatar al array de avatares en casilla.
    public void anhadirAvatar(Avatar av) {
        avatares.add(av);
    }

    //Método utilizado para eliminar un avatar del array de avatares en casilla.
    public void eliminarAvatar(Avatar av) {
        avatares.remove(av);
    }

    /*Método para evaluar qué hacer en una casilla concreta. Parámetros:
    * - Jugador cuyo avatar está en esa casilla.
    * - La banca (para ciertas comprobaciones).
    * - El valor de la tirada: para determinar impuesto a pagar en casillas de servicios.
    * Valor devuelto: true en caso de ser solvente (es decir, de cumplir las deudas), y false
    * en caso de no cumplirlas.

    Evalúa si se tiene el dinero suficiente para: comprarla, pagar el impuesto o el alquiler. Devuelve TRUE si se tiene el dinero suficiente
    FALSE en caso contrario. La función ya tiene en cuenta si la casilla no tiene dueño (se ofrece comprarla)
     o si el dueño es el propio jugador (no pasa nada).

    FALTA IMPLEMENTAR EL CÓDIGO PARA LAS CASILLAS ESPECIALES (IR A CÁRCEL, PARKING Y SALIDA)*/

    public boolean evaluarCasilla(Jugador actual, Jugador banca) {


        ////////////////    las casillas de impuestos no se deberian poder comprar


        Avatar avatar = actual.getAvatar();
        Casilla casilla = avatar.getCasilla();
        String tipo = casilla.getTipo();

        switch(tipo) {
            case "Solar":
            case "Transporte":
                if(casilla.getDuenho() == banca) {
                    return actual.getFortuna() >= casilla.getValor(); // Si tiene dinero comprarla = true, else false
                }
                else if(casilla.getDuenho() != actual) {
                    float impuesto = casilla.getImpuesto();
                    return actual.getFortuna() >= impuesto; // Si tiene dinero para el alquiler = true, else false
                }
                else if(casilla.getDuenho() == actual) {
                    return true; //No pasa nada, es su casilla.
                }
                break;
            case "Servicios":
                if(casilla.getDuenho() == banca) {
                    return actual.getFortuna() >= casilla.getValor(); // Si tiene dinero comprarla = true, else false
                }

                    // El caso de (duenho != actual) se maneja con la llamada a evaluarCasilla con tirada.

                else if(casilla.getDuenho() == actual) {
                    return true; // No pasa nada, es su casilla.
                }
                break;


            case "Impuestos":   /////////////    *** esto hace que pague el impuesto, pero también permite que compre la casilla ***
                float impuesto = casilla.getImpuesto();
                return actual.getFortuna() >= impuesto; // Si tiene dinero para el impuesto = true, else false

            case "Comunidad", "Suerte":
                return true; //No pasa nada, se roba carta.
            case "Especial":
                String nombre = casilla.getNombre();

                if(nombre.equals("IrCarcel") || nombre.equals("Parking") || nombre.equals("Salida")){
                    return true; //No pasa nada especial al caer en estas casillas.
                }
                else if(nombre.equals("Cárcel")){
                    float salidaCarcel = casilla.getImpuesto();
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

    /*Método usado para comprar una casilla determinada. Parámetros:
    * - Jugador que solicita la compra de la casilla.
    * - Banca del monopoly (es el dueño de las casillas no compradas aún).
    * - Se añade tirada para poder pasarla como parámetro a evaluar casilla.*/
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        if (solicitante.getAvatar().getLugar() != this) {
            System.out.println("\t" + Valor.RED + "Error: Debes estar sobre la casilla para comprarla." + Valor.RESET);
            return;
        }

        if (!this.tipo.equals("Solar") && !this.tipo.equals("Servicios") && !this.tipo.equals("Transporte")) {
            System.out.println("\t" + Valor.RED + "Error: La casilla no es adquirible." + Valor.RESET);
            return;
        }

        if(evaluarCasilla(solicitante, banca)){
            Casilla casilla = solicitante.getAvatar().getCasilla();
            if(casilla.getDuenho() == banca) { //Si la casilla no tiene dueño, se ofrece comprarla.
                float valor = casilla.getValor();
                solicitante.sumarFortuna(-valor); //Se resta el valor de la casilla a la fortuna del jugador.
                solicitante.agregarDineroInvertido(valor);
                banca.sumarFortuna(valor); //Se añade el valor de la casilla a la fortuna de la banca.

                casilla.setDuenho(solicitante); //El dueño de la casilla pasa a ser el jugador solicitante.
                solicitante.añadirPropiedad(casilla); //Se añade la casilla al arraylist de propiedades del jugador.
                banca.eliminarPropiedad(casilla); //Se elimina la casilla del arraylist de propiedades de la banca.

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

    /*Método para añadir valor a una casilla. Utilidad:
    * - Sumar valor a la casilla de parking.
    * - Sumar valor a las casillas de solar al no comprarlas tras cuatro vueltas de todos los jugadores.
    * Este método toma como argumento la cantidad a añadir del valor de la casilla.
    
    
    FALTA EL VALOR DEL SOLAR QUE NO SE CUANTO ES*/
    public void sumarValor(float suma) {
        this.valor += suma;
    }

    /*Método para mostrar información sobre una casilla.
    * Devuelve una cadena con información específica de cada tipo de casilla.
    

    NO SE QUE HAY QUE PONER EN LA DESCRIPCIÓN DE PARKING
    */
    public String infoCasilla() {

        String tipo = this.getTipo();

        switch(tipo){

            case "Solar":
                ArrayList<String> edif = (edificios == null) ? new ArrayList<>() : new ArrayList<>(edificios.stream().map(Edificio::getNombre).toList());
                return "\ttipo: " + this.tipo + "\n" +
                "\tgrupo: " + this.grupo.getCodigoColor() + this.grupo.getColorGrupo() + Valor.RESET + "\n" +
                "\tpropietario: " + (this.duenho.getNombre()) + "\n" +
                "\tvalor: " + this.valor + "€\n" +
                "\talquiler: " + this.impuesto + "€\n" +
                "\thipoteca: " + this.hipoteca + "€\n" +
                "\tedificios: " + edif + "\n" +
                "\tvalor casa: " + this.valores.get("casa") + "€\n" +
                "\tvalor hotel: " + this.valores.get("hotel") + "€\n" +
                "\tvalor piscina: " + this.valores.get("piscina") + "€\n" +
                "\tvalor pista: " + this.valores.get("pista") + "€\n" +
                "\talquiler casa: " + this.alquileres.get("casa") + "€\n" +
                "\talquiler hotel: " + this.alquileres.get("hotel") + "€\n" +
                "\talquiler piscina: " + this.alquileres.get("piscina") + "€\n" +
                "\talquiler pista: " + this.alquileres.get("pista") + "€";


            case "Transporte":
            case "Servicios":
                return "\ttipo: " + this.tipo + "\n" +
                "\tpropietario: " + (this.duenho.getNombre()) + "\n" +
                "\tvalor: " + this.valor + "€\n" +
                "\talquiler: " + this.impuesto + "€";
            

            case "Impuestos":
                return "\ttipo: " + this.tipo + "\n" +
                "\timpuesto: " + this.impuesto + "€";

            case "Comunidad":
            case "Suerte":
                return "\tNo hay información adicional para esta casilla.";
            
            case "Especial":
                if(this.nombre.equals("Cárcel")){
                    ArrayList<Avatar> avatares = this.getAvatares();
                    StringBuilder jugadoresEnCarcel = new StringBuilder();

                    for(Avatar av : avatares) {
                        jugadoresEnCarcel.append(av.getJugador().getNombre()).append(", ");
                    }

                    return "\ttipo: " + this.tipo + "\n" +
                    "\tsalida carcel: " + this.impuesto + "€\n" +
                    "\tJugadores en carcel: " + jugadoresEnCarcel;
                }
                else if(this.nombre.equals("Parking")){

                    //EN EL PDF PONE QUE HAY QUE MOSTRAR JUGADORES EN EL PARKING,
                    //PERO NO SE SI SON LOS QUE PAGARON IMPUESTOS O LOS QUE ESTÁN EN EL PARKING.

                    return "\ttipo: " + this.tipo + "\n" +
                    "\tbote actual: " + this.valor + "€";
                }
                else if(this.nombre.equals("Salida")){
                    return "\ttipo: " + this.tipo + "\n" +
                    "\tpremio por pasar: " + Valor.SUMA_VUELTA + "€";
                }
                else{
                    return "\tNo hay información adicional para esta casilla.";
                }
            default:
                return "\tError al mostrar infoCasilla().";
        }
    }

    /* Método para mostrar información de una casilla en venta.
     * Valor devuelto: texto con esa información.
     */
    ///////////////     HACE LO MISMO QUE descCasilla() de Menu     /////////////
    public String casEnVenta() {

        if (this.getDuenho().getNombre().equals("Banca")) {

            if(this.getTipo().equals("Solar")) {
                return "\nTipo" + ": " + this.getTipo() + "\n" +
                "Grupo" + ": " + this.getGrupo().getColorGrupo() + "\n" +
                "Valor" + ": " + this.getValor() + "€\n";
            } else if (this.getTipo().equals("Transporte") || this.getTipo().equals("Servicios")) {
                return "\nTipo" + ": " + this.getTipo() + "\n" +
                "Valor" + ": " + this.getValor() + "€\n" +
                "Impuesto" + ": " + this.getImpuesto() + "€\n";
            }
            else{
                return "\nLa casilla no está en venta.\n";
            }
        }
        else{
            return "\nLa casilla no está en venta.\n";
        }
    }

    public void incrementarFrecuencia() {
        this.frecuencia++;
    }

    /**
     * Método que gestiona los pagos al caer en esta casilla.
     * Parámetros:
     * - jugadorActual: jugador cuyo avatar ha caído en esta casilla.
     * - banca: jugador banca.
     * - tirada: valor de los dados (para calcular servicios).
     */
    public void gestionarPago(Jugador jugadorActual, Jugador banca, int tirada) {
        boolean solvente = evaluarCasilla(jugadorActual, banca);
    
        if (!solvente) {
            System.out.println("\t" + jugadorActual.getNombre() + " no tiene dinero suficiente para pagar esta casilla.");
            // Avisar que debe hipotecar o declararse en bancarrota
            return;
        }
        if (this.duenho == jugadorActual) {
            System.out.println("\tEsta casilla te pertenece.");
            return;
        }
    
        // Casillas con dueño distinto al jugador actual
        switch (tipo) {
            case "Solar", "Transporte", "Servicios" -> {
                // Si la propiedad está hipotecada, no se cobra alquiler
                if (!puedeCobrarAlquiler) {
                    System.out.println("\tLa propiedad está hipotecada. No se cobra alquiler.");
                    return;
                }
                
                if (this.duenho == MonopolyETSE.menu.getBanca()) {
                    System.out.println("\tCasilla en venta.");
                    return;
                }

                float cantidad = switch (tipo) {
                    case "Solar" -> {
                        boolean duenhoGrupo = this.grupo.esDuenhoGrupo(this.duenho);

                        yield duenhoGrupo ? impuesto * 2 : impuesto;
                    }
                    case "Transporte" -> this.impuesto;
                    case "Servicios" -> tirada * 4 * this.impuesto;
                    default -> 0;
                };

                jugadorActual.sumarFortuna(-cantidad);
                duenho.sumarFortuna(cantidad);
                if(duenho != MonopolyETSE.menu.getBanca()){
                    rentabilidad += cantidad;
                }
                System.out.println("\t" + jugadorActual.getNombre() + " paga "+ String.format("%.0f", cantidad) + "€ a " + duenho.getNombre());
                // Actualizar estadísticas
                jugadorActual.agregarPagoDeAlquileres(cantidad);
                duenho.agregarCobroDeAlquileres(cantidad);
            }
            // Casillas sin dueño (o de la banca)
            case "Impuestos" -> {
                float cantidad = this.impuesto;
                jugadorActual.sumarFortuna(-cantidad);
                Casilla parking = MonopolyETSE.tablero.encontrar_casilla("Parking");
                parking.sumarValor(cantidad);
                System.out.println("\t" + jugadorActual.getNombre() + " paga " + cantidad + "€ que se depositan en el Parking");
                // Actualizar estadísticas
                jugadorActual.agregarPagoTasasEImpuestos(cantidad);
            }
            case "Comunidad", "Suerte" -> System.out.println("\t" + jugadorActual.getNombre() + " roba una carta.");
            case "Especial" -> {
                if (nombre.equals("Parking")) {
                    System.out.println("\t" + jugadorActual.getNombre() + " recibe el bote de " + valor + "€");
                    jugadorActual.sumarFortuna(valor);
                    jugadorActual.agregarPremiosInversionesOBote(valor);
                    valor = 0; // Vaciar el bote
                } else if (nombre.equals("Salida") || this.posicion - tirada < 0) {
                    System.out.println("\t" + jugadorActual.getNombre() + " cayó en la salida.");
                } else if (nombre.equals("IrCarcel")) {
                    jugadorActual.encarcelar();
                    jugadorActual.incrementarVecesEnCarcel();
                }
            }
            default -> System.out.println("\tCasilla de paso. No pasa nada.");
        }
    }

    public void venderEdificio(String tipoEd, int n) {
        if (this.getDuenho() != MonopolyETSE.menu.getJugadores().get(MonopolyETSE.menu.getTurno())) {
            System.out.println("\t*** No puedes vender. La casilla '" + this.getNombre() + "' no es tuya. ***");
            System.out.println("}\n");
            return;
        }
        if (n < 1) {
            System.out.println("\t*** Debes vender al menos 1 propiedad. ***");
            System.out.println("}\n");
            return;
        }
        Jugador j = MonopolyETSE.menu.getJugadores().get(MonopolyETSE.menu.getTurno());
        if (edificios == null || edificios.isEmpty()) edificios = new ArrayList<>();
        int count = 0;
        switch (tipoEd) {
            case "casa":
                for (Edificio ed : this.getEdificios()) {
                    if (ed.getTipo().equals("casa")) count++;
                }
                if (count < n) {
                    System.out.println("\t*** No puedes vender " + n + " casas. Solo tienes " + count + ". ***");
                    System.out.println("}\n");
                    return;
                }
                int i = 0;
                Iterator<Edificio> it = this.getEdificios().iterator();
                while (it.hasNext()) {
                    Edificio ed = it.next();
                    if (ed.getTipo().equals("casa")) {
                        if (i >= count - n) {
                            it.remove();
                            MonopolyETSE.tablero.getCasas().remove(ed);
                            this.impuesto -= getAlquilerCasa();
                            j.sumarFortuna(getValorCasa());
                        }
                        i++;
                    }
                }
                System.out.println("\t" + j.getNombre() + " ha vendido " + n + " casas en " + this.getNombre() + ", recibiendo " + (n*getValorCasa())
                        + "€. En la propiedad queda(n) " + (count - n) + " casas.");
                break;
            case "hotel":
                for (Edificio ed : this.getEdificios()) {
                    if (ed.getTipo().equals("hotel")) count++;
                }
                if (count == 0) {
                    System.out.println("\t*** No tienes ningún hotel construído. ***");
                    System.out.println("}\n");
                    return;
                }
                Iterator<Edificio> it2 = this.getEdificios().iterator();
                while (it2.hasNext()) {
                    Edificio ed = it2.next();
                    if (ed.getTipo().equals("hotel")) {
                        it2.remove();
                        MonopolyETSE.tablero.getHoteles().remove(ed);
                        this.impuesto -= getAlquilerHotel();
                        j.sumarFortuna(getValorHotel());
                        break;
                    }
                }
                String print = ", recibiendo " + getValorHotel() + "€.";
                if (n > 1) {
                    print = "\tSolo se puede vender un hotel" + print;
                }
                else print = "\t" + j.getNombre() + " ha vendido 1 hotel en " + this.getNombre() + print;
                System.out.println(print);
                break;
            case "piscina":
                for (Edificio ed : this.getEdificios()) {
                    if (ed.getTipo().equals("piscina")) count++;
                }
                if (count == 0) {
                    System.out.println("\t*** No tienes ninguna piscina construída. ***");
                    System.out.println("}\n");
                    return;
                }
                Iterator<Edificio> it3 = this.getEdificios().iterator();
                while (it3.hasNext()) {
                    Edificio ed = it3.next();
                    if (ed.getTipo().equals("piscina")) {
                        it3.remove();
                        MonopolyETSE.tablero.getPiscinas().remove(ed);
                        this.impuesto -= getAlquilerPiscina();
                        j.sumarFortuna(getValorPiscina());
                        break;
                    }
                }
                String print2 = ", recibiendo " + getValorPiscina() + "€.";
                if (n > 1) {
                    print2 = "\tSolo se puede vender una piscina" + print2;
                }
                else print2 = "\t" + j.getNombre() + " ha vendido 1 piscina en " + this.getNombre() + print2;
                System.out.println(print2);
                break;
            case "pista":
                for (Edificio ed : this.getEdificios()) {
                    if (ed.getTipo().equals("pista")) count++;
                }
                if (count == 0) {
                    System.out.println("\t*** No tienes ninguna pista construída. ***");
                    System.out.println("}\n");
                    return;
                }
                Iterator<Edificio> it4 = this.getEdificios().iterator();
                while (it4.hasNext()) {
                    Edificio ed = it4.next();
                    if (ed.getTipo().equals("pista")) {
                        it4.remove();
                        MonopolyETSE.tablero.getPistas().remove(ed);
                        this.impuesto -= getAlquilerPista();
                        j.sumarFortuna(getValorPista());
                        break;
                    }
                }
                String print3 = ", recibiendo " + getValorPista() + "€.";
                if (n > 1) {
                    print3 = "\tSolo se puede vender una pista" + print3;
                }
                else print3 = "\t" + j.getNombre() + " ha vendido 1 pista en " + this.getNombre() + print3;
                System.out.println(print3);
                break;
            default:
                System.out.println("\t*** No puedes vender. Tipo de edificio no registrado. ***");
                System.out.println("}\n");
                return;
        }
    }

    public void edificar(String tipo) {
        Jugador propietario = this.duenho;
        int tamGrupo = grupo.numCasillas();
        ArrayList<Casilla> prop = propietario.getPropiedades();
        int count = 0;
        // se comprueba si todo el grupo es del mismo jugador
        for (Casilla casilla : prop) {
            if (casilla.getGrupo() == grupo) {
                count += 1;
            }
        }
        if (count < tamGrupo) {
            System.out.println("\t*** No se puede edificar. Necesitas adquirir todo el grupo primero. ***");
            return;
        }
        if (edificios == null) edificios = new ArrayList<>();
        // se distingue el tipo de edificio
        switch (tipo) {
            case "casa":
                if (this.valores.get("casa") > propietario.getFortuna()) {
                    System.out.println("\t*** No tienes suficiente liquidez. Coste por casa: " + valores.get("casa") + " ***");
                    return;
                }
                if (!construirCasa()) return;
                String Cnom = "casa-" + (MonopolyETSE.tablero.getCasas().size()+1); // id de la casa
                Edificio casa = new Edificio(Cnom, this, tipo, getValorCasa(), getAlquilerCasa());
                Jugador actual1 = this.avatares.getLast().getJugador();
                // se paga la casa
                actual1.sumarFortuna(-getValorCasa());
                actual1.agregarDineroInvertido(getValorCasa());
                // se contruye la casa
                edificios.add(casa);
                this.impuesto += getAlquilerCasa();
                MonopolyETSE.tablero.getCasas().add(casa);
                break;
            case "hotel":
                if (this.valores.get("hotel") > propietario.getFortuna()) {
                    System.out.println("\t*** No tienes suficiente liquidez. Coste por hotel: " + valores.get("hotel") + " ***");
                    return;
                }
                if (!construirHotel()) return;
                String Hnom = "hotel-" + (MonopolyETSE.tablero.getHoteles().size()+1); // id del hotel
                Edificio hotel = new Edificio(Hnom, this, tipo, getValorHotel(), getAlquilerHotel());
                Jugador actual2 = this.avatares.getLast().getJugador();
                // paga el hotel
                actual2.sumarFortuna(-getValorHotel());
                actual2.agregarDineroInvertido(getValorHotel());

                // se sustituyen las casas por el hotel
                Iterator<Edificio> it = edificios.iterator();
                while (it.hasNext()) {
                    Edificio ed = it.next();
                    if (ed.getTipo().equals("casa")) {
                        it.remove();
                        MonopolyETSE.tablero.getCasas().remove(ed);
                    }
                }
                this.impuesto -= getAlquilerCasa() * 4;
                // se construye el hotel
                edificios.add(hotel);
                MonopolyETSE.tablero.getHoteles().add(hotel);
                this.impuesto += getAlquilerHotel();
                break;
            case "piscina":
                if (this.valores.get("piscina") > propietario.getFortuna()) {
                    System.out.println("\t*** No tienes suficiente liquidez. Coste por piscina:  " + valores.get("piscina") + " ***");
                    return;
                }
                if (!construirPiscina()) return;
                String Pnom = "piscina-" + (MonopolyETSE.tablero.getPiscinas().size()+1); // id de la piscina
                Edificio piscina = new Edificio(Pnom, this, tipo, getValorPiscina(), getAlquilerPiscina());
                Jugador actual3 = this.avatares.getLast().getJugador();
                // se paga la piscina
                actual3.sumarFortuna(-getValorPiscina());
                actual3.agregarDineroInvertido(getValorPiscina());
                // se construye la piscina
                edificios.add(piscina);
                this.impuesto += getAlquilerPiscina();
                MonopolyETSE.tablero.getPiscinas().add(piscina);
                break;
            case "pista":
                if (this.valores.get("pista") > propietario.getFortuna()) {
                    System.out.println("\t*** No tienes suficiente liquidez. Coste por pista: " + valores.get("pista") + " ***");
                    return;
                }
                if (!construirPista()) return;
                String Pinom = "pista-" + (MonopolyETSE.tablero.getPistas().size()+1); // id de la pista
                Edificio pista = new Edificio(Pinom, this, tipo, getValorPista(), getAlquilerPista());
                Jugador actual4 = this.avatares.getLast().getJugador();
                // se paga la pista
                actual4.sumarFortuna(-getValorPista());
                actual4.agregarDineroInvertido(getValorPista());
                // se construye la pista
                edificios.add(pista);
                this.impuesto += getAlquilerPista();
                MonopolyETSE.tablero.getPistas().add(pista);
                break;
            default:
                System.out.println("\t*** Tipo de edificio no registrado. ***");
                return;
        }
        System.out.println("\t" + Valor.GREEN + "Edificio (" + tipo + ") construido en " + this.nombre + Valor.RESET);
    }

    private boolean construirCasa() {
        int count = 0;
        for (Edificio ed : this.edificios) {
            if (ed.getTipo().equals("casa")) count += 1;
        }
        if (count == 4) {
            System.out.println("\t*** Máximo de 4 casas alcanzado. ***");
            return false;
        }
        System.out.println("\tSe pueden construir " + (4 - count - 1) + " casas más.");
        return true;
    }

    private boolean construirHotel() {
        int count = 0;
        for (Edificio ed : this.edificios) {
            if (ed.getTipo().equals("hotel")) {
                System.out.println("\t" + Valor.RED + "Ya hay un hotel construido." + Valor.RESET);
                return false;
            }
            if (ed.getTipo().equals("casa")) count += 1;
        }
        if (count != 4) {
            System.out.println("\t*** Se necesitan 4 casas para construir un hotel. ***");
            return false;
        }
        return true;
    }

    private boolean construirPiscina() {
        boolean hayHotel = false;
        for (Edificio ed : this.edificios) {
            if (ed.getTipo().equals("piscina")) {
                System.out.println("\t" + Valor.RED + "Ya hay una piscina construida." + Valor.RESET);
                return false;
            }
            if (ed.getTipo().equals("hotel")) {
                hayHotel = true;
            }
        }
        if (!hayHotel) {
            System.out.println("\t*** Se necesita un hotel para construir una piscina. ***");
            return false;
        }
        return true;
    }


    private boolean construirPista() {
        boolean hayPiscina = false;
        for (Edificio ed : this.edificios) {
            if (ed.getTipo().equals("pista")) {
                System.out.println("\t" + Valor.RED + "Ya hay una pista construida." + Valor.RESET);
                return false;
            }
            if (ed.getTipo().equals("piscina")) {
                hayPiscina = true;
            }
        }
        if (!hayPiscina) {
            System.out.println("\t*** Se necesita una piscina para construir una pista de deporte. ***");
            return false;
        }
        return true;
    }

    public void hipotecar(Jugador jugadorActual) {
        // Verificar dueño
        if (this.duenho != jugadorActual) {
            System.out.println("\t" + Valor.RED + "Error: Solo el dueño puede hipotecar esta casilla." + Valor.RESET);
            return;
        }
    
        // Verificar si ya está hipotecada
        if (this.hipotecada) {
            System.out.println("\t" + Valor.RED + "Error: La propiedad ya está hipotecada." + Valor.RESET);
            return;
        }
    
        // Verificar si hay edificios
        if (this.edificios != null && !this.edificios.isEmpty()) {
            System.out.println("\t" + Valor.YELLOW + "El dueño tiene edificios en esta casilla. Debe venderlos antes de hipotecar." + Valor.RESET);
            Scanner sc = new Scanner(System.in);
            System.out.println("\t¿Quieres vender los edificios ahora? (s/n)");
            String respuesta = sc.nextLine();
        
            if (respuesta.equalsIgnoreCase("s")) {
        
                while (!this.edificios.isEmpty()) {
                    System.out.println("\t¿Qué edificio quieres vender? (casa / hotel / piscina / pista)");
                    String tipo = sc.nextLine().toLowerCase();
        
                    System.out.println("\t¿Cuántos quieres vender?");
                    int cantidad = Integer.parseInt(sc.nextLine());
        
                    this.venderEdificio(tipo, cantidad);
                }
        
            } else {
                System.out.println("\t" + Valor.RED + "No se han vendido los edificios. No se puede hipotecar la casilla." + Valor.RESET);
                return;
            }
        }
        
    
        // Hipotecar
        this.hipotecada = true; // Marca la casilla como hipotecada
        this.puedeCobrarAlquiler = false; // Bloquear alquileres
        jugadorActual.sumarFortuna(this.hipoteca/2); // Entregar dinero
        System.out.println("\t" + Valor.GREEN + "Casilla hipotecada con éxito. Se han recibido " + this.hipoteca + "€." + Valor.RESET);
    }

    public void deshipotecar(Jugador jugadorActual){
        // Verificar dueño
        if (this.duenho != jugadorActual) {
            System.out.println("\t" + Valor.RED + "Error: Solo el dueño puede deshipotecar esta casilla." + Valor.RESET);
            return;
        }
    
        // Verificar si está hipotecada
        if (!this.hipotecada) {
            System.out.println("\t" + Valor.RED + "Error: La propiedad no está hipotecada." + Valor.RESET);
            return;
        }
    
        // Verificar si el jugador tiene suficiente dinero
        float costoDeshipotecar = this.hipoteca ;
        if (jugadorActual.getFortuna() < costoDeshipotecar) {
            System.out.println("\t" + Valor.RED + "Error: No tienes suficiente dinero para deshipotecar esta casilla." + Valor.RESET);
            return;
        }
    
        // Deshipotecar
        this.hipotecada = false; // Marca la casilla como no hipotecada
        this.puedeCobrarAlquiler = true; // Permitir alquileres
        jugadorActual.sumarFortuna(-costoDeshipotecar/2); // Cobrar al jugador
        System.out.println("\t" + Valor.GREEN + "Casilla deshipotecada con éxito. Se han pagado " + costoDeshipotecar + "€." + Valor.RESET);
    }
    


    public String getNombre() {
        return this.nombre;
    }
    public String getTipo() {
        return this.tipo;
    }
    public float getValor() {
        return this.valor;
    }
    public int getPosicion() {
        return this.posicion;
    }
    public Jugador getDuenho() {
        return this.duenho;
    }
    public float getRentabilidad() {
        return this.rentabilidad;
    }
    public Grupo getGrupo() {
        return this.grupo;
    }
    public float getImpuesto() {
        return this.impuesto;
    }
    public float getHipoteca() {
        return this.hipoteca;
    }
    public ArrayList<Avatar> getAvatares() {
        return this.avatares;
    }
    public float getValorCasa() {
        return valores.get("casa");
    }
    public float getValorHotel() {
        return valores.get("hotel");
    }
    public float getValorPiscina() {
        return valores.get("piscina");
    }
    public float getValorPista() {
        return valores.get("pista");
    }
    public float getAlquilerCasa() {
        return alquileres.get("casa");
    }
    public float getAlquilerHotel() {
        return alquileres.get("hotel");
    }
    public float getAlquilerPiscina() {
        return alquileres.get("piscina");
    }
    public float getAlquilerPista() {
        return alquileres.get("pista");
    }
    public int getFrecuencia() {
        return frecuencia;
    }
    public ArrayList<Edificio> getEdificios() {
        if (edificios == null) edificios = new ArrayList<>();
        return edificios;
    }
    public HashMap<String, Float> getValores() {
        return valores;
    }
    public HashMap<String, Float> getAlquileres() {
        return alquileres;
    }



    public void setRentabilidad(float rentabilidad) {
        this.rentabilidad = rentabilidad;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public void setValor(float valor) {
        this.valor = valor;
    }
    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
    public void setDuenho(Jugador duenho) {
        this.duenho = duenho;
    }
    public void setGrupo(Grupo grupo){
        this.grupo = grupo;
    }
    public void setImpuesto(float impuesto){
        this.impuesto = impuesto;
    }
    public void setFrecuencia(int frecuencia){
        this.frecuencia = frecuencia;
    }
    public void setHipoteca(float hipoteca){
        this.hipoteca = hipoteca;
    }
    public void setAvatares(ArrayList<Avatar> avatares){
        this.avatares = avatares;
    }
    public void setValorCasa(float valorCasa) {
        this.valores.put("casa", valorCasa);
    }
    public void setValorHotel(float valorHotel) {
        this.valores.put("hotel", valorHotel);
    }
    public void setValorPiscina(float valorPiscina) {
        this.valores.put("piscina", valorPiscina);
    }
    public void setValorPista(float valorPista) {
        this.valores.put("pista", valorPista);
    }
    public void setAlquilerCasa(float alquilerCasa) {
        this.alquileres.put("casa", alquilerCasa);
    }
    public void setAlquilerHotel(float alquilerHotel) {
        this.alquileres.put("hotel", alquilerHotel);
    }
    public void setAlquilerPiscina(float alquilerPiscina) {
        this.alquileres.put("piscina", alquilerPiscina);
    }
    public void setAlquilerPista(float alquilerPista) {
        this.alquileres.put("pista", alquilerPista);
    }
}
