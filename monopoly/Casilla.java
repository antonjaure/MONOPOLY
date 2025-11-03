package monopoly;

import partida.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


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

    FALTA IMPLEMENTAR EL CÓDIGO PARA LAS CASILLAS ESPECIALES (IR A CÁRCEL, PARKING Y SALIDA)


    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        Avatar avatar = actual.getAvatar();
        Casilla casilla = avatar.getCasilla();
        String tipo = casilla.getTipo();

        if (tipo.equals("Impuesto")) {
            if (casilla.getDuenho() != banca && casilla.getDuenho() != actual) {
                float impuesto = casilla.getImpuesto() * tirada;
                return actual.getFortuna() >= impuesto; // Si tiene dinero para el impuesto = true, else false
            } else {
                return evaluarCasilla(actual, banca);
            }
        }
        return evaluarCasilla(actual, banca);
    }
    */


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
            case "Comunidad":
            case "Suerte":
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

        if(evaluarCasilla(solicitante, banca)){
            Casilla casilla = solicitante.getAvatar().getCasilla();
            if(casilla.getDuenho() == banca) { //Si la casilla no tiene dueño, se ofrece comprarla.
                float valor = casilla.getValor();
                solicitante.sumarFortuna(-valor); //Se resta el valor de la casilla a la fortuna del jugador.
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

    /**
     * Método que gestiona los pagos al caer en esta casilla.
     * Parámetros:
     * - jugadorActual: jugador cuyo avatar ha caído en esta casilla.
     * - banca: jugador banca.
     * - tirada: valor de los dados (para calcular servicios).
     */
    public void gestionarPago(Jugador jugadorActual, Jugador banca, int tirada) {
        if (this.posicion - tirada < 0) {
            System.out.println("\t" + jugadorActual.getNombre() + " pasa por Salida y recibe 2.000.000€");
            jugadorActual.sumarFortuna(2000000f);
        }
    
        boolean solvente = evaluarCasilla(jugadorActual, banca);
    
        if (!solvente) {
            System.out.println("\t" + jugadorActual.getNombre() + " no tiene dinero suficiente para pagar esta casilla.");
            // Avisar que debe hipotecar o declararse en bancarrota
            return;
        }
    
        // Casillas con dueño distinto al jugador actual
        switch (tipo) {
            case "Solar", "Transporte", "Servicios" -> {

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
                System.out.println("\t" + jugadorActual.getNombre() + " paga " + cantidad + "€ a " + duenho.getNombre());

                // Casillas sin dueño (o de la banca)
            }
            case "Impuestos" -> {
                float cantidad = this.impuesto;
                jugadorActual.sumarFortuna(-cantidad);
                Casilla parking = MonopolyETSE.tablero.encontrar_casilla("Parking");
                parking.sumarValor(cantidad);
                System.out.println("\t" + jugadorActual.getNombre() + " paga " + cantidad + "€ que se depositan en el Parking");
            }
            case "Comunidad", "Suerte" -> System.out.println("\t" + jugadorActual.getNombre() + " roba una carta.");
            case "Especial" -> {
                if (nombre.equals("Parking")) {
                    System.out.println("\t" + jugadorActual.getNombre() + " recibe el bote de " + valor + "€");
                    jugadorActual.sumarFortuna(valor);
                    valor = 0; // Vaciar el bote
                } else if (nombre.equals("Salida") || this.posicion - tirada < 0) {
                    System.out.println("\t" + jugadorActual.getNombre() + " cayó en la salida.");
                    // jugadorActual.sumarFortuna(2000000f);
                } else if (nombre.equals("IrCarcel")) {
                    jugadorActual.encarcelar();
                }
            }
            default -> System.out.println("\tCasilla de paso. No pasa nada.");
        }
    }
    public void edificar(String tipo) {
        Jugador propietario = this.duenho;
        int tamGrupo = grupo.numCasillas();
        ArrayList<Casilla> prop = propietario.getPropiedades();
        int count = 0;
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

        switch (tipo) {
            case "casa":
                if (this.valores.get("casa") > propietario.getFortuna()) {
                    System.out.println("\t*** No tienes suficiente liquidez. Coste por casa: " + valores.get("casa") + " ***");
                    return;
                }
                if (!construirCasa()) return;
                String Cnom = "casa-" + (MonopolyETSE.tablero.getCasas().size()+1);
                Edificio casa = new Edificio(Cnom, this, tipo);
                Jugador actual1 = this.avatares.getLast().getJugador();
                actual1.sumarFortuna(-getValorCasa());

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
                String Hnom = "hotel-" + (MonopolyETSE.tablero.getHoteles().size()+1);
                Edificio hotel = new Edificio(Hnom, this, tipo);
                Jugador actual2 = this.avatares.getLast().getJugador();
                actual2.sumarFortuna(-getValorHotel());

                // se sustituyen las casas por el hotel
                edificios.add(hotel);
                this.impuesto +=  getAlquilerHotel();
                MonopolyETSE.tablero.getHoteles().add(hotel);
                Iterator<Edificio> it = edificios.iterator();
                while (it.hasNext()) {
                    Edificio ed = it.next();
                    if (ed.getTipo().equals("casa")) {
                        it.remove();
                        MonopolyETSE.tablero.getCasas().remove(ed);
                    }
                }

                break;
            case "piscina":
                if (this.valores.get("piscina") > propietario.getFortuna()) {
                    System.out.println("\t*** No tienes suficiente liquidez. Coste por piscina:  " + valores.get("piscina") + " ***");
                    return;
                }
                if (!construirPiscina()) return;
                String Pnom = "piscina-" + (MonopolyETSE.tablero.getPiscinas().size()+1);
                Edificio piscina = new Edificio(Pnom, this, tipo);
                Jugador actual3 = this.avatares.getLast().getJugador();
                actual3.sumarFortuna(-getValorPiscina());

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
                String Pinom = "pista-" + (MonopolyETSE.tablero.getPistas().size()+1);
                Edificio pista = new Edificio(Pinom, this, tipo);
                Jugador actual4 = this.avatares.getLast().getJugador();
                actual4.sumarFortuna(-getValorPista());

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
