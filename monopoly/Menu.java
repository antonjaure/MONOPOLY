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
    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
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
    public void ejecutarOpcion(String comando) {
        switch (comando.toLowerCase()) {
            case "lanzar dados":
                lanzarDados(0, false);
                break;
    
            case "acabar turno":
                acabarTurno();
                break;
            default:
                System.out.println("Comando no reconocido: " + comando);
                break;
        }
    }
    // Metodo para inciar una partida: crea los jugadores y avatares.

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
            if (numPalabras == 2) lanzarDados(0,false);
            else if (numPalabras == 3) {
                int i = comando.indexOf("+");
                char num1 = comando.charAt(i-1);
                char num2 = comando.charAt(i+1);
                int suma = Character.getNumericValue(num1) + Character.getNumericValue(num2);
                //lanzarDadosForzado(suma);
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
    private int doblesConsecutivos = 0; // variable de clase para contar dobles

    private void lanzarDados(int valorForzado, boolean forzado) {
        if (jugadores == null || jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }
    
        Jugador jugadorActual = jugadores.get(turno);
        Avatar avatarActual = jugadorActual.getAvatar();
    
        int total = 0;
        int dado1Val = 0;
        int dado2Val = 0;
        int doblesContador = 0;
        boolean volverATirar = true;
    
        while (volverATirar) {
            if (forzado) {
                total = valorForzado;
                dado1Val = total / 2; // solo efecto visual
                dado2Val = total - dado1Val;
            } else {
                dado1Val = new Dado().hacerTirada();
                dado2Val = new Dado().hacerTirada();
                total = dado1Val + dado2Val;
            }
    
            System.out.println("Tirada: " + dado1Val + " + " + dado2Val + " = " + total);
    
            // Verificar dobles
            if (dado1Val == dado2Val) {
                doblesContador++;
                if (doblesContador == 3) {
                    System.out.println("¡Tres dobles! " + avatarActual.getJugador().getNombre() + " va a la cárcel.");
                    Casilla carcel = tablero.encontrar_casilla("Cárcel");
                    avatarActual.moverAvatar(tablero.getPosiciones(), carcel.getPosicion() - avatarActual.getCasilla().getPosicion());
                    volverATirar = false;
                    break;
                } else {
                    System.out.println("¡Dobles! " + avatarActual.getJugador().getNombre() + " vuelve a tirar.");
                }
            } else {
                volverATirar = false;
            }
    
            // Mover avatar
            avatarActual.moverAvatar(tablero.getPosiciones(), total);
    
            // Gestionar pagos en la casilla donde cayó
            Casilla destino = avatarActual.getCasilla();
            destino.gestionarPago(jugadorActual, tablero.getBanca(), total);
    
            // Mostrar tablero actualizado
            System.out.println(tablero.toString());
    
            if (forzado) break; // solo una tirada si es forzada
        }
    
        tirado = true; // marcar que el jugador ya tiró
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
        if (jugadores == null || jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }
    
        // Opcional: comprobar si el jugador actual ya tiró
        if (!tirado) {
            System.out.println("El jugador actual debe lanzar los dados antes de acabar su turno.");
            return;
        }
        // Pasar al siguiente jugador
        turno = (turno + 1) % jugadores.size();
    
        // Resetear tirada
        tirado = false;
    
        Jugador jugadorActual = jugadores.get(turno);
        System.out.println("El jugador actual es " + jugadorActual.getNombre() + ".");
    }
}
