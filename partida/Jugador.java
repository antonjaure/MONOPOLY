package partida;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import monopoly.*;

import static monopoly.MonopolyETSE.juego;


public class Jugador {

    //Atributos:
    private String nombre; //Nombre del jugador
    private Avatar avatar; //Avatar que tiene en la partida.
    private float fortuna; //Dinero que posee.
    private float gastos; //Gastos realizados a lo largo del juego.
    private boolean enCarcel; //Será true si el jugador está en la carcel
    private int tiradasCarcel; //Cuando está en la carcel, contará las tiradas sin éxito que ha hecho allí para intentar salir (se usa para limitar el numero de intentos).
    private int vueltas; //Cuenta las vueltas dadas al tablero.
    private ArrayList<Propiedad> propiedades; //Propiedades que posee el jugador.
    private ArrayList<Propiedad> hipotecas;
    private int doblesConsecutivos = 0; // variable de clase para contar dobles
    private int cartaComunidadId = 0; // id de la carta de comunidad actual
    private int cartaSuerteId = 0; // id de la carta de suerte actual
    private ArrayList<Trato> tratos; // lista de tratos que tiene el jugador
    // --- Atributos adicionales para estadísticas ---
    private float dineroInvertido = 0;            // Dinero invertido en compra de propiedades y edificaciones
    private float pagoTasasEImpuestos = 0;      // Dinero pagado en tasas e impuestos
    private float pagoDeAlquileres = 0;         // Dinero pagado en alquileres
    private float cobroDeAlquileres = 0;        // Dinero cobrado por alquileres
    private float pasarPorCasillaDeSalida = 0;  // Dinero recibido al pasar por Salida
    private float premiosInversionesOBote = 0;   // Dinero recibido por premios o el bote de Parking
    private int vecesEnLaCarcel = 0;            // Veces que ha estado en la cárcel

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
        for (String tipo : juego.getTipos()) {
            if (tipo.equals(tipoAvatar)) {
                tipoValido = true;
                break;
            }
        }
        if (!tipoValido) {
            Juego.consola.imprimir("\t*** Avatares disponibles: " + juego.getTipos() + " ***");
            return;
        }

        boolean tipo = true;
        boolean nom = true;
        for (Avatar av : avCreados) {
            if (av.getTipo().equals(tipoAvatar)) tipo = false;
            if (av.getJugador().getNombre().equals(nombre)) nom = false;
        }

        if (!tipo && !nom){
            Juego.consola.imprimir("\t*** Jugador ya activo. ***");
            return;
        }

        if (!tipo) {
            Juego.consola.imprimir("\t*** Avatar en uso. ***");
            return;
        }

        if (nom) setNombre(nombre);
        else {
            Juego.consola.imprimir("\t*** Nombre en uso. ***");
            return;
        }

        this.fortuna = Valor.FORTUNA_INICIAL;

        Avatar avatar = new Avatar(tipoAvatar, this, inicio, avCreados);
        inicio.anhadirAvatar(avatar);
        setAvatar(avatar);

        juego.setJugador(this);
        juego.setAvatar(avatar);
    }

    //Otros métodos:
    //Método para añadir una propiedad al jugador. Como parámetro, la casilla a añadir.
    public void añadirPropiedad(Propiedad p) {
        this.propiedades.add(p);
    }

    //Método para eliminar una propiedad del arraylist de propiedades de jugador.
    public void eliminarPropiedad(Casilla casilla) {
        this.propiedades.remove(casilla);
    }

    //Método para añadir fortuna a un jugador
    //Como parámetro se pide el valor a añadir. Si hay que restar fortuna, se pasaría un valor negativo.

    //////////////  FALTA COMPROBAR CUANDO EL VALOR ES NEGATIVO(hay que pagar) SI EL JUGADOR PUEDE PAGAR Y DARLE LA OPCION DE VENDER, HIPOTECAR, ETC.
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
        Casilla carcel = juego.getTablero().encontrar_casilla("Cárcel");

        // Mover a la cárcel
        avatar.getCasilla().eliminarAvatar(avatar);
        avatar.setLugar(carcel);
        carcel.anhadirAvatar(avatar);

        this.setEnCarcel(true);
        this.setTiradasCarcel(0);

        Juego.consola.imprimir("\t" + this.getNombre() + " ha sido enviado a la cárcel.");
    }

    // Metodo para encontrar un jugador de la lista de jugadores activos
    // parametro: cadena con el nombre del jugador
    public static Jugador buscarJugador(String jugador) {
        ArrayList<Jugador> jugadores = juego.getJugadores();
        if (jugadores == null || jugadores.isEmpty()) {
            Juego.consola.imprimir("\t*** No hay jugadores en la partida. ***");
            return null;
        }

        for  (Jugador j : jugadores) {
            if (j.getNombre().equals(jugador)) {
                return j;
            }
        }
        Juego.consola.imprimir("\t*** Jugador '" + jugador + "' no registrado. ***");
        return null;
    };

    // --- Métodos para actualizar estas estadísticas ---
    public void agregarDineroInvertido(float valor) { dineroInvertido += valor; }
    public void agregarPagoTasasEImpuestos(float valor) { pagoTasasEImpuestos += valor; }
    public void agregarPagoDeAlquileres(float valor) { pagoDeAlquileres += valor; }
    public void agregarCobroDeAlquileres(float valor) { cobroDeAlquileres += valor; }
    public void agregarPasarPorSalida(float valor) { pasarPorCasillaDeSalida += valor; }
    public void agregarPremiosInversionesOBote(float valor) { premiosInversionesOBote += valor; }
    public void incrementarVecesEnCarcel() { vecesEnLaCarcel++; }

    // --- Método para mostrar estadísticas ---
    public void mostrarEstadisticas() {
        Juego.consola.imprimir("\tdineroInvertido: " + (int)dineroInvertido);
        Juego.consola.imprimir("\tpagoTasasEImpuestos: " + (int)pagoTasasEImpuestos);
        Juego.consola.imprimir("\tpagoDeAlquileres: " + (int)pagoDeAlquileres);
        Juego.consola.imprimir("\tcobroDeAlquileres: " + (int)cobroDeAlquileres);
        Juego.consola.imprimir("\tpasarPorCasillaDeSalida: " + (int)pasarPorCasillaDeSalida);
        Juego.consola.imprimir("\tpremiosInversionesOBote: " + (int)premiosInversionesOBote);
        Juego.consola.imprimir("\tvecesEnLaCarcel: " + vecesEnLaCarcel);
    }

    public String getNombre() {
        return this.nombre;
    }

    public Avatar getAvatar() {
        return this.avatar;
    }

    public float getFortuna() {
        return this.fortuna;
    }

    public ArrayList<Propiedad> getPropiedades() {
        if (propiedades == null) propiedades = new ArrayList<>();
        return propiedades;
    }

    public int getTiradasCarcel() {
        return tiradasCarcel;
    }
    public int getDoblesConsecutivos() {
        return doblesConsecutivos;
    }

    public int getCartaComunidadId() {
        return cartaComunidadId;
    }
    public int getCartaSuerteId() {
        return cartaSuerteId;
    }
    public int getVueltas() {
        return vueltas;
    }

    public float getPatrimonio() {
        float patrimonio = this.fortuna;
        for (Propiedad p : this.getPropiedades()) {
            patrimonio += p.getValor();
            if (p instanceof Solar s){
                for (Edificio ed : s.getEdificios()) {
                    patrimonio += ed.getCoste();
                }
            }
        }
        return patrimonio;
    }

    public ArrayList<Trato> getTratos() {
        return tratos;
    }

    public void setTratos(ArrayList<Trato> tratos) {
        this.tratos = tratos;
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

    public void setPropiedades(ArrayList<Propiedad> propiedades) {
        this.propiedades = propiedades;
    }

    public void setCartaComunidadId(int cartaComunidadId) {
        this.cartaComunidadId = cartaComunidadId;
    }

    public void setCartaSuerteId(int cartaSuerteId) {
        this.cartaSuerteId = cartaSuerteId;
    }


}
