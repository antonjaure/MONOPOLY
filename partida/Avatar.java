package partida;

import monopoly.*;

import java.util.ArrayList;


public class Avatar {

    //Atributos
    private String id; //Identificador: una letra generada aleatoriamente.
    private String tipo; //Sombrero, Esfinge, Pelota, Coche
    private Jugador jugador; //Un jugador al que pertenece ese avatar.
    private Casilla lugar; //Los avatares se sitúan en casillas del tablero.

    public Casilla getCasilla() {
        return lugar;
    }

    public String getTipo() {
        return tipo;
    }

    public String getId() {
        return id;
    }

    public Jugador getJugador() {
        return jugador; 
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }
    public void setLugar(Casilla lugar) {
        this.lugar = lugar;
    }

    //Constructor vacío
    public Avatar() {
    }

    /*Constructor principal. Requiere estos parámetros:
    * Tipo del avatar, jugador al que pertenece, lugar en el que estará ubicado, y un arraylist con los
    * avatares creados (usado para crear un ID distinto del de los demás avatares).
     */
    public Avatar(String tipo, Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
    }

    //A continuación, tenemos otros métodos útiles para el desarrollo del juego.
    /*Método que permite mover a un avatar a una casilla concreta. Parámetros:
    * - Un array con las casillas del tablero. Se trata de un arrayList de arrayList de casillas (uno por lado).
    * - Un entero que indica el numero de casillas a moverse (será el valor sacado en la tirada de los dados).
    * EN ESTA VERSIÓN SUPONEMOS QUE valorTirada siempre es positivo.
     */
    public void moverAvatar(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
        Casilla origen = this.lugar;
        Casilla destino = MonopolyETSE.tablero.avanzarCasillas(origen, valorTirada);
    
        // Mover avatar en las casillas
        origen.eliminarAvatar(this);
        destino.anhadirAvatar(this);
        this.lugar = destino;
    
        // Mostrar movimiento
        System.out.println("El avatar " + this.id + " avanza " + valorTirada +
                " posiciones, desde " + origen.getNombre() + " hasta " + destino.getNombre() + ".");
    }
    
    /*Método que permite generar un ID para un avatar. Sólo lo usamos en esta clase (por ello es privado).
    * El ID generado será una letra mayúscula. Parámetros:
    * - Un arraylist de los avatares ya creados, con el objetivo de evitar que se generen dos ID iguales.
     */
    private void generarId(ArrayList<Avatar> avCreados) {
    }
}
