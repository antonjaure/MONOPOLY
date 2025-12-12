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

    public String toString() {
        return "nombre: " + this.nombre +
                "\ntipo: " + this.tipo +
                "\npropietario: " + this.duenho;
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
