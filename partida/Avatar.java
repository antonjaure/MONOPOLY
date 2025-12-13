package partida;

import monopoly.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


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
    public Casilla getLugar() {
        return lugar;
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
    ////////////////    cambiar el constructor de Jugador para darle uso a este     ////////////////
    public Avatar(String tipo, Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
        Random random = new Random();

        if (avCreados == null) {
            avCreados = new ArrayList<>();
        }

        char ID = (char) ('A' + random.nextInt(26));
        if (!avCreados.isEmpty()) {
            inicio:
            for (Avatar av : avCreados) {
                if (Objects.equals(av.getId(), String.valueOf(ID))) {
                    ID = (char) ('A' + random.nextInt(26));
                    continue inicio;
                }
            }
        }
        setId(String.valueOf(ID));
        setTipo(tipo);
        setJugador(jugador);
        setLugar(lugar);
    }

    //A continuación, tenemos otros métodos útiles para el desarrollo del juego.
    /*Método que permite mover a un avatar a una casilla concreta. Parámetros:
    * - Un array con las casillas del tablero. Se trata de un arrayList de arrayList de casillas (uno por lado).
    * - Un entero que indica el numero de casillas a moverse (será el valor sacado en la tirada de los dados).
    * EN ESTA VERSIÓN SUPONEMOS QUE valorTirada siempre es positivo.
     */
    public void moverAvatar(int valorTirada) {
        Casilla origen = this.lugar;
        Casilla destino = MonopolyETSE.juego.getTablero().avanzarCasillas(origen, valorTirada);

        int posActual = origen.getPosicion();
        int posDestino = destino.getPosicion();
        int totalCasillas = 40; // número de casillas del tablero
    
        // Comprobar si se pasó por la salida (posición 0)
        if ((posActual + valorTirada) >= totalCasillas) {
            System.out.println("\t" + this.getJugador().getNombre() + " pasa por Salida y recibe 2.000.000€");
            this.getJugador().sumarFortuna(2000000f);
            this.getJugador().agregarPasarPorSalida(2000000f);
            int num_vueltas = this.getJugador().getVueltas();
            this.getJugador().setVueltas(num_vueltas + 1);
        }
        /*if (origen.getPosicion() - valorTirada < 0) {
            System.out.println("\t" + this.getJugador().getNombre() + " pasa por Salida y recibe 2.000.000€");
            this.getJugador().sumarFortuna(2000000f);
            this.getJugador().agregarPasarPorSalida(2000000f);
        }*/
    
        // Mover avatar en las casillas
        origen.eliminarAvatar(this);
        destino.anhadirAvatar(this);
        this.lugar = destino;
    
        // Mostrar movimiento
        System.out.println("\tEl avatar " + this.id + " avanza " + valorTirada +
                " posiciones, desde '" + origen.getNombre() + "' hasta '" + destino.getNombre() + "'.");
    }
    // alternativa para mover un avatar directamente a una casilla, sin tener en cuenta por donde pase
    public void moverAvatar(String nombreCasilla) {
        Casilla origen = this.lugar;
        Casilla destino = MonopolyETSE.juego.getTablero().encontrar_casilla(nombreCasilla);

        if(origen.getPosicion() > destino.getPosicion()) {
            System.out.println("\t" + this.getJugador().getNombre() + " pasa por Salida y recibe 2.000.000€");
            this.getJugador().sumarFortuna(2000000f);
            this.getJugador().agregarPasarPorSalida(2000000f);
            int num_vueltas = this.getJugador().getVueltas();
            this.getJugador().setVueltas(num_vueltas + 1);
        }

        // Mover avatar en las casillas
        origen.eliminarAvatar(this);
        destino.anhadirAvatar(this);
        this.lugar = destino;

        // Mostrar movimiento
        System.out.println("\tEl avatar " + this.id + " avanza hasta '" + destino.getNombre() + "'.");
    }
    
    /*Método que permite generar un ID para un avatar. Sólo lo usamos en esta clase (por ello es privado).
    * El ID generado será una letra mayúscula. Parámetros:
    * - Un arraylist de los avatares ya creados, con el objetivo de evitar que se generen dos ID iguales.
     */
    private void generarId(ArrayList<Avatar> avCreados) {
    }
}
