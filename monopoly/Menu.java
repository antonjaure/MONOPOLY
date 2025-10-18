package monopoly;

import java.util.ArrayList;
import partida.*;

public class Menu {

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private int turno = 0; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    private int lanzamientos; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private Tablero tablero; //Tablero en el que se juega.
    private Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private Jugador banca; //El jugador banca.
    private boolean tirado; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    private boolean solvente; //Booleano para comprobar si el jugador que tiene el turno es solvente, es decir, si ha pagado sus deudas.

    public void setJugador(Jugador jugador) {
        if (jugadores == null) {
            jugadores = new ArrayList<>();
        }
        this.jugadores.add(jugador);
    }

    public void setAvatar(Avatar avatar) {
        if (avatares == null) {
            avatares = new ArrayList<>();
        }
        this.avatares.add(avatar);
    }

    public ArrayList<Avatar> getAvatares() {
        return avatares;
    }

    public Jugador getBanca() {
        return banca;
    }

    public Menu() {
        this.banca = new Jugador();
    }

    // Metodo para iniciar una partida: crea los jugadores y avatares.
    private void iniciarPartida() {
    }

    /*Metodo que interpreta el comando introducido y toma la accion correspondiente.
    * Parámetro: cadena de caracteres (el comando).
    */
    private void analizarComando(String comando) {
        String[] palabras = comando.trim().split("\\s+");  // divide por uno o más espacios
        int numPalabras = (comando.trim().isEmpty()) ? 0 : palabras.length;

        if (comando.contains("describir jugador")) {
            if (numPalabras != 3) {
                System.out.println("*** Argumentos incorrectos. ***\n");
                System.out.println("Uso: describir jugador <Nombre>\n");
                return;
            }
            String nombre = comando.substring(17).trim();
            descJugador(nombre);
        }
        else if (comando.contains("describir avatar")) {}
        else if (comando.contains("describir")) {
            if (numPalabras != 2) {
                System.out.println("*** Argumentos incorrectos. ***\n");
                System.out.println("Uso: describir <Nombre casilla>\n");
                return;
            }
            String nombre = comando.substring(9).trim();
            descCasilla(nombre);
        }
        else if (comando.contains("lanzar dados")) {
            if (numPalabras == 2) lanzarDados();
            else if (numPalabras == 3) {
                int i = comando.indexOf("+");
                char num1 = comando.charAt(i-1);
                char num2 = comando.charAt(i+1);
                int suma = Character.getNumericValue(num1) + Character.getNumericValue(num2);
                lanzarDadosForzado(suma);
            }
            else {
                System.out.println("*** Argumentos incorrectos. ***\n");
                System.out.println("Uso: lanzar dados <Dado1>+<Dado2>\n");
                return;
            }
        }
        else if (comando.contains("comprar")) {
            if (numPalabras != 2) {
                System.out.println("*** Argumentos incorrectos. ***\n");
                System.out.println("Uso: comprar <Nombre casilla>\n");
                return;
            }
            String nombre = comando.substring(7).trim();
            comprar(nombre);
        }
        else if (comando.equals("salir carcel")) salirCarcel();
        else if (comando.equals("listar enventa")) listarVenta();
        else if (comando.equals("listar jugadores")) listarJugadores();
        else if (comando.equals("listar avatares")) {}
        else if (comando.equals("acabar turno")) acabarTurno();
        else {
            System.out.println("*** Comando no registrado. ***\n");
        }
    }

    /*Metodo que realiza las acciones asociadas al comando 'describir jugador'.
    * Parámetro: nombre del jugador a describir.
     */
    private void descJugador(String jugador) {
    }

    /*Metodo que realiza las acciones asociadas al comando 'describir avatar'.
    * Parámetro: id del avatar a describir.
    */
    private void descAvatar(String ID) {
    }

    /* Metodo que realiza las acciones asociadas al comando 'describir nombre_casilla'.
    * Parámetros: nombre de la casilla a describir.
    */
    private void descCasilla(String nombre) {
    }

    //Metodo que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
    private void lanzarDados() {
    }

    private void lanzarDadosForzado(int tirada) {
    }

    /*Metodo que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
    * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) {
    }

    //Metodo que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
    private void salirCarcel() {
        Jugador jugador = jugadores.get(turno%jugadores.size());
        float fortuna = jugador.getFortuna();
        float precio_carcel = 500000f;
        
        if(fortuna >= precio_carcel){
                jugador.sumarFortuna(-precio_carcel);
                System.out.println(jugador.getNombre() + "paga 500.000€ y sale de la cárcel. Puede lanzar los dados.\n");
        }
        else{
            System.out.println(jugador.getNombre() + "no posee suficiente dinero para pagar la salida. Debe lanzar los dados.\n");
        }
    }
    

    // Metodo que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
    }

    // Metodo que realiza las acciones asociadas al comando 'listar jugadores'.
    private void listarJugadores() {
    }

    // Metodo que realiza las acciones asociadas al comando 'listar avatares'.
    private void listarAvatares() {
    }

    // Metodo que realiza las acciones asociadas al comando 'acabar turno'.
    private void acabarTurno() {
    }

}
