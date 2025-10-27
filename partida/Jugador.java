package partida;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import monopoly.*;

import static monopoly.MonopolyETSE.menu;
import static monopoly.MonopolyETSE.tablero;


public class Jugador {

    //Atributos:
    private String nombre; //Nombre del jugador
    private Avatar avatar; //Avatar que tiene en la partida.
    private float fortuna; //Dinero que posee.
    private float gastos; //Gastos realizados a lo largo del juego.
    private boolean enCarcel; //Será true si el jugador está en la carcel
    private int tiradasCarcel; //Cuando está en la carcel, contará las tiradas sin éxito que ha hecho allí para intentar salir (se usa para limitar el numero de intentos).
    private int vueltas; //Cuenta las vueltas dadas al tablero.
    private ArrayList<Casilla> propiedades; //Propiedades que posee el jugador.
    private ArrayList<Casilla> hipotecas;
    private int doblesConsecutivos = 0; // variable de clase para contar dobles

    //Constructor vacío. Se usará para crear la banca.
    public Jugador() {
    }

    /*Constructor principal. Requiere parámetros:
    * Nombre del jugador, tipo del avatar que tendrá, casilla en la que empezará y ArrayList de
    * avatares creados (usado para dos propósitos: evitar que dos jugadores tengan el mismo nombre y
    * que dos avatares tengan mismo ID). Desde este constructor también se crea el avatar.
     */
    public Jugador(String nombre, String tipoAvatar, Casilla inicio, ArrayList<Avatar> avCreados) {
        if (avCreados == null) {
            avCreados = new ArrayList<>();
        }

        boolean tipoValido = false;
        for (String tipo : menu.getTipos()) {
            if (tipo.equals(tipoAvatar)) {
                tipoValido = true;
                break;
            }
        }
        if (!tipoValido) {
            System.out.println("\t*** Avatares disponibles: " + menu.getTipos() + " ***");
            return;
        }

        boolean tipo = true;
        boolean nom = true;
        for (Avatar av : avCreados) {
            if (av.getTipo().equals(tipoAvatar)) tipo = false;
            if (av.getJugador().getNombre().equals(nombre)) nom = false;
        }

        if (!tipo && !nom){
            System.out.println("\t*** Jugador ya activo. ***");
            return;
        }

        if (!tipo) {
            System.out.println("\t*** Avatar en uso. ***");
            return;
        }

        if (nom) setNombre(nombre);
        else {
            System.out.println("\t*** Nombre en uso. ***");
            return;
        }

        this.fortuna = Valor.FORTUNA_INICIAL;

        Avatar avatar = new Avatar(tipoAvatar, this, inicio, avCreados);
        inicio.anhadirAvatar(avatar);
        setAvatar(avatar);

        menu.setJugador(this);
        menu.setAvatar(avatar);
    }

    //Otros métodos:
    //Método para añadir una propiedad al jugador. Como parámetro, la casilla a añadir.
    public void añadirPropiedad(Casilla casilla) {
        this.propiedades.add(casilla);
    }

    //Método para eliminar una propiedad del arraylist de propiedades de jugador.
    public void eliminarPropiedad(Casilla casilla) {
        this.propiedades.remove(casilla);
    }

    //Método para añadir fortuna a un jugador
    //Como parámetro se pide el valor a añadir. Si hay que restar fortuna, se pasaría un valor negativo.
    public void sumarFortuna(float valor) {
        this.fortuna += valor;
        if(valor < 0) {
            sumarGastos(-valor);
        }
    }

    //Método para sumar gastos a un jugador.
    //Parámetro: valor a añadir a los gastos del jugador (será el precio de un solar, impuestos pagados...).
    public void sumarGastos(float valor) {
        this.gastos += valor;
    }

    /*Método para establecer al jugador en la cárcel. 
    * Se requiere disponer de las casillas del tablero para ello (por eso se pasan como parámetro).*/
    public void encarcelar() {
        Avatar avatar = this.getAvatar();
        Casilla carcel = tablero.encontrar_casilla("Cárcel");

        // Mover a la cárcel
        avatar.getCasilla().eliminarAvatar(avatar);
        avatar.setLugar(carcel);
        carcel.anhadirAvatar(avatar);

        this.setEnCarcel(true);
        this.setTiradasCarcel(0);

        System.out.println(this.getNombre() + " ha sido enviado a la cárcel.\n");
    }

    // Metodo para encontrar un jugador de la lista de jugadores activos
    // parametro: cadena con el nombre del jugador
    public static Jugador buscarJugador(String jugador) {
        ArrayList<Jugador> jugadores = menu.getJugadores();
        if (jugadores == null || jugadores.isEmpty()) {
            System.out.println("\t*** No hay jugadores en la partida. ***");
            return null;
        }

        for  (Jugador j : jugadores) {
            if (j.getNombre().equals(jugador)) {
                return j;
            }
        }
        System.out.println("\t*** Jugador '" + jugador + "' no registrado. ***");
        return null;
    };



    public String getNombre() {
        return this.nombre;
    }

    public Avatar getAvatar() {
        return this.avatar;
    }

    public float getFortuna() {
        return this.fortuna;
    }

    public ArrayList<Casilla> getPropiedades() {
        if (propiedades == null) propiedades = new ArrayList<>();
        return propiedades;
    }

    public int getTiradasCarcel() {
        return tiradasCarcel;
    }
    public int getDoblesConsecutivos() {
        return doblesConsecutivos;
    }
    public boolean isEnCarcel() { //getter para saber si esta en la carcel
        return this.enCarcel;
    }

    public void setDoblesConsecutivos(int doblesConsecutivos) {
        this.doblesConsecutivos = doblesConsecutivos;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public void setFortuna(float fortuna) {
        this.fortuna = fortuna;
    }

    public void setGastos(float gastos) {
        this.gastos = gastos;
    }

    public void setEnCarcel(boolean enCarcel) {
        this.enCarcel = enCarcel;
    }

    public void setTiradasCarcel(int tiradasCarcel) {
        this.tiradasCarcel = tiradasCarcel;
    }

    public void setVueltas(int vueltas) {
        this.vueltas = vueltas;
    }

    public void setPropiedades(ArrayList<Casilla> propiedades) {
        this.propiedades = propiedades;
    }


}
