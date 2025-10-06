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
    private ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.

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
    



    public ArrayList<Avatar> getAvatares() {
        return this.avatares;
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
    * en caso de no cumplirlas.*/
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        Avatar avatar = actual.getAvatar();
        Casilla casilla = avatar.getCasilla();
        String tipo = casilla.getTipo();


        return true;
    }

    /*Método usado para comprar una casilla determinada. Parámetros:
    * - Jugador que solicita la compra de la casilla.
    * - Banca del monopoly (es el dueño de las casillas no compradas aún).*/
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
    }

    /*Método para añadir valor a una casilla. Utilidad:
    * - Sumar valor a la casilla de parking.
    * - Sumar valor a las casillas de solar al no comprarlas tras cuatro vueltas de todos los jugadores.
    * Este método toma como argumento la cantidad a añadir del valor de la casilla.*/
    public void sumarValor(float suma) {
    }

    /*Método para mostrar información sobre una casilla.
    * Devuelve una cadena con información específica de cada tipo de casilla.*/
    public String infoCasilla() {


        return "";
    }

    /* Método para mostrar información de una casilla en venta.
     * Valor devuelto: texto con esa información.
     */
    public String casEnVenta() {


        return "";
    }

}
