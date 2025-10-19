package monopoly;

import partida.*;
import java.util.ArrayList;


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
    private float valorCasa; //Valor de las casas en caso de ser una casilla solar.
    private float valorHotel; //Valor de los hoteles en caso de ser una casilla solar.  
    private float valorPiscina; //Valor de las piscinas en caso de ser una casilla solar.
    private float valorPista; //Valor de las pistas de tenis en caso de ser una casilla solar.
    private float alquilerCasa; //Alquiler adicional por cada casa en caso de ser una casilla solar.
    private float alquilerHotel; //Alquiler adicional por cada hotel en caso de ser una casilla solar.
    private float alquilerPiscina; //Alquiler adicional por cada piscina en caso de ser una
    private float alquilerPista; //Alquiler adicional por cada pista en caso de ser una casilla solar.
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
    */

    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        Avatar avatar = actual.getAvatar();
        Casilla casilla = avatar.getCasilla();
        String tipo = casilla.getTipo();

        switch(tipo) {
            case "Impuesto":
                if(casilla.getDuenho() != banca && casilla.getDuenho() != actual) {
                    float impuesto = casilla.getImpuesto() * tirada;
                    if(actual.getFortuna() >= impuesto) {
                        return true; //Tiene dinero para pagar el impuesto.
                    } else {
                        return false; //No tiene dinero para pagar el impuesto.
                    }
                }
                else{
                    return evaluarCasilla(actual, banca);
                }

            default:
                return evaluarCasilla(actual, banca);
        }          
    }

    public boolean evaluarCasilla(Jugador actual, Jugador banca) {
        Avatar avatar = actual.getAvatar();
        Casilla casilla = avatar.getCasilla();
        String tipo = casilla.getTipo();

        switch(tipo) {
            case "Solar": 
            case "Transporte":
                if(casilla.getDuenho() == banca) { //Si la casilla no tiene dueño, se ofrece comprarla.
                    if(actual.getFortuna() >= casilla.getValor()) {
                        return true;
                    } else {
                        return false; //No tiene dinero para comprarla.
                    }
                }else if(casilla.getDuenho() != actual) {
                    float impuesto = casilla.getImpuesto();
                    if(actual.getFortuna() >= impuesto) {
                        return true; //Tiene dinero para pagar el alquiler.
                    } else {
                        return false; //No tiene dinero para pagar el alquiler.
                    }
                }
                else if(casilla.getDuenho() == actual) {
                    return true; //No pasa nada, es su casilla.
                }
                break;
            case "Servicios":
                if(casilla.getDuenho() == banca) { //Si la casilla no tiene dueño, se ofrece comprarla.
                    if(actual.getFortuna() >= casilla.getValor()) {
                        return true;
                    } else {
                        return false; //No tiene dinero para comprarla.
                    }
                }

                    //El caso de duenho != actual se maneja con la llamada a evaluarCasilla con tirada.


                else if(casilla.getDuenho() == actual) {
                    return true; //No pasa nada, es su casilla.
                }
                break;

            case "Impuesto":
                float impuesto = casilla.getImpuesto();
                if(actual.getFortuna() >= impuesto) {
                    return true; //Tiene dinero para pagar el impuesto.
                } else {
                    return false; //No tiene dinero para pagar el impuesto.
                }
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
                    if(actual.getFortuna() >= salidaCarcel) {
                        return true; //Tiene dinero para pagar la salida de la cárcel.
                    } else {
                        return false; //No tiene dinero para pagar la salida de la cárcel.
                    }
                }
                System.err.println("\nError al evaluarCasilla().\n");
                return true;
            default:
                System.err.println("\nError al evaluarCasilla().\n");
                return true;
        }          
        return true;   
    }

    /*Método usado para comprar una casilla determinada. Parámetros:
    * - Jugador que solicita la compra de la casilla.
    * - Banca del monopoly (es el dueño de las casillas no compradas aún).
    * - Se añade tirada para poder pasarla como parametro a evaluar casilla.*/
    public void comprarCasilla(Jugador solicitante, Jugador banca, int tirada) {
        if(evaluarCasilla(solicitante, banca, tirada)){
            Casilla casilla = solicitante.getAvatar().getCasilla();
            if(casilla.getDuenho() == banca) { //Si la casilla no tiene dueño, se ofrece comprarla.
                float valor = casilla.getValor();
                solicitante.sumarFortuna(-valor); //Se resta el valor de la casilla a la fortuna del jugador.
                banca.sumarFortuna(valor); //Se añade el valor de la casilla a la fortuna de la banca.
                casilla.setDuenho(solicitante); //El dueño de la casilla pasa a ser el jugador solicitante.
                solicitante.añadirPropiedad(casilla); //Se añade la casilla al arraylist de propiedades del jugador.
                System.out.println("\n" + Valor.GREEN + "¡Compra realizada con éxito!" + Valor.RESET);
            } else {
                System.out.println("\n" + Valor.RED + "Error: La casilla ya tiene dueño." + Valor.RESET); //No es necesario, pero se deja para manejar errores.
            }
        } else {
            System.out.println("\n" + Valor.RED + "Error: La casilla ya tiene dueño o no tiene suficiente dinero para comprarla." + Valor.RESET);
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
                return "\ttipo: " + this.tipo + "\n" +
                "\tgrupo: " + this.grupo.getCodigoColor() + this.grupo.getColorGrupo() + Valor.RESET + "\n" +
                "\tpropietario: " + (this.duenho.getNombre()) + "\n" +
                "\tvalor: " + this.valor + "€\n" +
                "\talquiler: " + this.impuesto + "€\n" +
                "\thipoteca: " + this.hipoteca + "€\n" +
                "\tvalor casa: " + this.valorCasa + "€\n" +
                "\tvalor hotel: " + this.valorHotel + "€\n" +
                "\tvalor piscina: " + this.valorPiscina + "€\n" +
                "\tvalor pista: " + this.valorPista + "€\n" +
                "\talquiler casa: " + this.alquilerCasa + "€\n" +
                "\talquiler hotel: " + this.alquilerHotel + "€\n" +
                "\talquiler piscina: " + this.alquilerPiscina + "€\n" +
                "\talquiler pista: " + this.alquilerPista + "€";

            case "Transporte":
            case "Servicios":
                return "\ttipo: " + this.tipo + "\n" +
                "\tpropietario: " + (this.duenho.getNombre()) + "\n" +
                "\tvalor: " + this.valor + "€\n" +
                "\talquiler: " + this.impuesto + "€";
            

            case "Impuesto":
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
    
        boolean solvente = evaluarCasilla(jugadorActual, banca, tirada);
    
        if (!solvente) {
            System.out.println("\t" + jugadorActual.getNombre() + " no tiene dinero suficiente para pagar esta casilla.");
            // Avisar que debe hipotecar o declararse en bancarrota
            return;
        }
    
        // Casillas con dueño distinto al jugador actual
        if (tipo.equals("Solar") || tipo.equals("Transporte") || tipo.equals("Servicios")) {

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
        } else if (tipo.equals("Impuesto")) {
            float cantidad = this.impuesto;
            jugadorActual.sumarFortuna(-cantidad);
            Casilla parking = MonopolyETSE.tablero.encontrar_casilla("Parking");
            parking.valor += cantidad;
            System.out.println("\t" + jugadorActual.getNombre() + " paga " + cantidad + "€ que se depositan en el Parking");
        } 
        else if (tipo.equals("Comunidad") || tipo.equals("Suerte")) {
            System.out.println("\t" + jugadorActual.getNombre() + " roba una carta.");
        }
        else if (tipo.equals("Especial")) {
            if(nombre.equals("Parking")) {
                System.out.println("\t" + jugadorActual.getNombre() + " recibe el bote de " + valor + "€");
                jugadorActual.sumarFortuna(valor);
                valor = 0; // Vaciar el bote
            }
            else if (nombre.equals("Salida") || this.posicion - tirada < 0) {
                System.out.println("\t" + jugadorActual.getNombre() + " pasa por Salida y recibe 2.000.000€");
                jugadorActual.sumarFortuna(2000000f);
            }
            else if (nombre.equals("IrCarcel")) {
                jugadorActual.encarcelar();
            }
        }
        else {
            System.out.println("\tCasilla de paso. No pasa nada.");
        }
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
        return valorCasa;
    }
    public float getValorHotel() {
        return valorHotel;
    }
    public float getValorPiscina() {
        return valorPiscina;
    }
    public float getValorPista() {
        return valorPista;
    }
    public float getAlquilerCasa() {
        return alquilerCasa;
    }
    public float getAlquilerHotel() {
        return alquilerHotel;
    }
    public float getAlquilerPiscina() {
        return alquilerPiscina;
    }
    public float getAlquilerPista() {
        return alquilerPista;
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
        this.valorCasa = valorCasa;
    }
    public void setValorHotel(float valorHotel) {
        this.valorHotel = valorHotel;
    }
    public void setValorPiscina(float valorPiscina) {
        this.valorPiscina = valorPiscina;
    }
    public void setValorPista(float valorPista) {
        this.valorPista = valorPista;
    }
    public void setAlquilerCasa(float alquilerCasa) {
        this.alquilerCasa = alquilerCasa;
    }
    public void setAlquilerHotel(float alquilerHotel) {
        this.alquilerHotel = alquilerHotel;
    }
    public void setAlquilerPiscina(float alquilerPiscina) {
        this.alquilerPiscina = alquilerPiscina;
    }
    public void setAlquilerPista(float alquilerPista) {
        this.alquilerPista = alquilerPista;
    }
}
