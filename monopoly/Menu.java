package monopoly;

import java.awt.event.ComponentAdapter;
import java.util.ArrayList;
import java.util.Scanner;

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

    public void setTablero(Tablero tablero) {
        if (tablero == null) {
            tablero = new Tablero();
        }
        this.tablero = tablero;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public ArrayList<Avatar> getAvatares() {
        return avatares;
    }

    public Jugador getBanca() {
        return banca;
    }
    public Menu() {
        this.banca = new Jugador();
        banca.setNombre("Banca");
    }


    // Metodo para iniciar una partida: crea los jugadores y avatares.
    private void iniciarPartida() {
    }

    /*Metodo que interpreta el comando introducido y toma la acción correspondiente.
    * Parámetro: cadena de caracteres (el comando).
    */
    private void analizarComando(String comando) {

        String[] palabras = comando.trim().split("\\s+");  // divide por uno o más espacios
        int numPalabras = (comando.trim().isEmpty()) ? 0 : palabras.length;

        System.out.println("{");

        // crea un jugador nuevo
        if (comando.contains("crear jugador")) {
            if (numPalabras != 4) {
                System.out.println("*** Argumentos incorrectos. ***");
                System.out.println("Uso: crear jugador <Nombre> <Tipo avatar>");
                System.out.println("}\n");
                return;
            }
            String nombre = palabras[2];
            String tipo = palabras[3];
            Jugador jugador = new Jugador(nombre, tipo, tablero.encontrar_casilla("Salida"), MonopolyETSE.menu.getAvatares());
            descJugador(nombre);
        }
        // describe el jugador del turno actual
        else if (comando.equals("jugador")) {
            Jugador jActual = jugadores.get(turno%jugadores.size());
            descJugador(jActual.getNombre());
        }
        // describe el jugador que se indique en la línea de comandos
        else if (comando.contains("describir jugador")) {
            if (numPalabras != 3) {
                System.out.println("*** Argumentos incorrectos. ***");
                System.out.println("Uso: describir jugador <Nombre>");
                System.out.println("}\n");
                return;
            }
            String nombre = palabras[2].trim();
            descJugador(nombre);
        }
        // creo que no hay que hacerlo para la entrega 1 (CREO)
        else if (comando.contains("describir avatar")) {} ///////////////////////////////////
        // describe la casilla indicada en la línea de comandos
        else if (comando.contains("describir")) {
            if (numPalabras != 2) {
                System.out.println("*** Argumentos incorrectos. ***");
                System.out.println("Uso: describir <Nombre casilla>");
                System.out.println("}\n");
                return;
            }
            String nombre = palabras[1].trim();
            descCasilla(nombre);
        }
        // hace una tirada de dados
        else if (comando.contains("lanzar dados")) {
            // tirada aleatoria
            if (numPalabras == 2) lanzarDados(0,0,false);
            // tirada forzada con el valor pasado por la línea de comandos
            else if (numPalabras == 3) {
                String[] dados = palabras[2].split("\\+"); // separar por el '+'
                if (dados.length != 2) {
                    System.out.println("*** Formato incorrecto. Uso: lanzar dados <Dado1>+<Dado2>");
                    return;
                }
                try {
                    int d1 = Integer.parseInt(dados[0].trim()); // primer dado
                    int d2 = Integer.parseInt(dados[1].trim()); // segundo dado
                    lanzarDados(d1, d2, true); // lanzada forzada
                } catch (NumberFormatException e) {
                    System.out.println("*** Valores de dados incorrectos. Deben ser números del 1 al 6.");
                }
            }
            else {
                System.out.println("*** Argumentos incorrectos. ***");
                System.out.println("Uso: lanzar dados <Dado1>+<Dado2>");
                System.out.println("}\n");
                return;
            }
        }
        // compra una casilla
        else if (comando.contains("comprar")) {
            if (numPalabras != 2) {
                System.out.println("*** Argumentos incorrectos. ***");
                System.out.println("Uso: comprar <Nombre casilla>");
                System.out.println("}\n");
                return;
            }
            String nombre = palabras[1].trim();
            comprar(nombre);
        }
        // saca al jugador actual de la carcel si puede
        else if (comando.equals("salir carcel")) salirCarcel();
        // lista las casillas en venta
        else if (comando.equals("listar enventa")) listarVenta();
        // lista los jugadores activos
        else if (comando.equals("listar jugadores")) listarJugadores();
        // lista los avatares de los jugadores activos, tmp hay que hacer para la entrega 1 (CREO)
        else if (comando.equals("listar avatares")) {}
        // finaliza el turno
        else if (comando.equals("acabar turno")) acabarTurno();
        // imprime el tablero
        else if (comando.equals("ver tablero")) {
            System.out.println("Imprimiendo tablero...\n");
            System.out.println(tablero.toString());
        }
        // finaliza el programa
        else if (comando.equals("salir")) System.out.println("Saliendo del programa...");
        else {
            System.out.println("*** Comando no registrado. ***");
        }

        System.out.println("}\n");
    }

    // solución provisional, no sé como acceder a la función siendo privada si no es de esta manera
    public void ejecutarComando(String comando) {
        analizarComando(comando);
    }

    /*Metodo que realiza las acciones asociadas al comando 'describir jugador'.
    * Parámetro: nombre del jugador a describir.
     */
    private void descJugador(String j) {
        Jugador jugador = Jugador.buscarJugador(j);
        if (jugador == null) return;
        String nombre = jugador.getNombre();
        String avatar = jugador.getAvatar().getId();
        String fortuna = String.valueOf(jugador.getFortuna());
        ArrayList<String> propiedades = new ArrayList<>();
        if (jugador.getPropiedades() != null) {
            for (Casilla c : jugador.getPropiedades()) propiedades.add(c.getNombre());
        }
        // hipotecas
        // edificios
        System.out.println("\tNombre: " + nombre);
        System.out.println("\tAvatar: " + avatar);
        System.out.println("\tFortuna: " + fortuna);
        System.out.println("\tPropiedades: " + propiedades);
        // sout(hipotecas)
        // sout(edificios)
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
        Casilla casilla = tablero.encontrar_casilla(nombre);
        if (casilla == null) {
            System.out.println("*** Casilla '" + nombre + "' no encontrada. ***\n");
            return;
        }
        System.out.println(casilla.infoCasilla());
    }

    //Metodo que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
    private void lanzarDados(int dado1Forzado, int dado2Forzado, boolean forzado) {
        if (jugadores == null || jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }
    
        Jugador jugadorActual = jugadores.get(turno);
        Avatar avatarActual = jugadorActual.getAvatar();
        // --- COMPROBAR SI EL JUGADOR ESTÁ EN LA CÁRCEL ---
        if (jugadorActual.isEnCarcel()) {
            System.out.println(jugadorActual.getNombre() + " está en la cárcel.");

            // Llama al método salirCarcel() que maneja toda la lógica de pago o tirar dobles
            salirCarcel();

            // Si después de salirCarcel sigue en la cárcel → termina el turno
            if (jugadorActual.isEnCarcel()) {
                System.out.println(jugadorActual.getNombre() + " no ha podido salir de la cárcel.");
                System.out.println("No puede lanzar los dados hasta su próximo turno.");
                acabarTurno();
                return; // Finaliza el turno
            } else {
                System.out.println(jugadorActual.getNombre() + " ha salido de la cárcel y puede lanzar los dados.");
            }
}

    
        int dado1Val;
        int dado2Val;
        int total;
        boolean volverATirar = true;
    
        while (volverATirar) {
            // Determinar tirada
            if (forzado) {
                dado1Val = dado1Forzado;
                dado2Val = dado2Forzado;
                total = dado1Val + dado2Val;
            } else {
                dado1Val = new Dado().hacerTirada();
                dado2Val = new Dado().hacerTirada();
                total = dado1Val + dado2Val;
            }
    
            System.out.println("Tirada: " + dado1Val + " + " + dado2Val + " = " + total);
    
            // Verificar dobles
            if (dado1Val == dado2Val) {
                jugadorActual.setDoblesConsecutivos(jugadorActual.getDoblesConsecutivos() + 1);
            
                if (jugadorActual.getDoblesConsecutivos() == 3) {
                    System.out.println(jugadorActual.getNombre() + " va a la cárcel.");
                    Casilla carcel = tablero.encontrar_casilla("Cárcel");
                    avatarActual.moverAvatar(tablero.getPosiciones(), carcel.getPosicion() - avatarActual.getCasilla().getPosicion());
                    jugadorActual.setEnCarcel(true);
                    jugadorActual.setDoblesConsecutivos(0);
                    
                    System.out.println(tablero.toString());
                    
                    // Terminar el turno inmediatamente
                    acabarTurno(); // <-- Aquí es clave
                    return;
                } else {
                    System.out.println("¡Dobles! " + jugadorActual.getNombre() + " vuelve a tirar.");
                }
            } else {
                jugadorActual.setDoblesConsecutivos(0); // Reiniciar si no es doble
            }
            
            // Mover avatar
            avatarActual.moverAvatar(tablero.getPosiciones(), total);
            
            // Gestionar IrCarcel y otras casillas
            Casilla destino = avatarActual.getCasilla();
            if (destino.getNombre().equals("IrCarcel")) {
                System.out.println(jugadorActual.getNombre() + " va a la cárcel por caer en IrCarcel.");
                Casilla carcel = tablero.encontrar_casilla("Cárcel");
                avatarActual.moverAvatar(tablero.getPosiciones(), carcel.getPosicion() - destino.getPosicion());
                jugadorActual.setEnCarcel(true);
                jugadorActual.setDoblesConsecutivos(0);
                System.out.println(tablero.toString());
                acabarTurno();
                return; // Terminar turno
            } else {
                // Gestionar pagos normales
                destino.gestionarPago(jugadorActual, tablero.getBanca(), total);
            }
    
            // Mostrar tablero actualizado
            System.out.println(tablero.toString());
    
            if (forzado) break; // solo una tirada si es forzada
            if (dado1Val != dado2Val) volverATirar = false; // No seguir tirando si no es doble
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
        Jugador jugador = jugadores.get(turno % jugadores.size());
        float fortuna = jugador.getFortuna();
        float precio_carcel = 500000f;
        int tiradasCarcel = jugador.getTiradasCarcel();
    
        if (tiradasCarcel >= 3) {
            if(fortuna >= precio_carcel){
                jugador.sumarFortuna(-precio_carcel);
                jugador.setEnCarcel(false);
                jugador.setTiradasCarcel(0);
            } else {
            }
        }
    
        // Preguntar al jugador
        Scanner sc = new Scanner(System.in);
        System.out.println("Desea salir de la cárcel pagando 500.000€? (sí/no)");
        String respuesta = sc.nextLine();
    
        if(respuesta.equalsIgnoreCase("sí")) {
            if(fortuna >= precio_carcel){
                jugador.sumarFortuna(-precio_carcel);
                jugador.setEnCarcel(false);
                jugador.setTiradasCarcel(0);
            } else {
                System.out.println(jugador.getNombre() + " no posee suficinete dinero para pagar la salida. Debe esperar a su proximo turno.\n");
            }
        } else {
            jugador.setTiradasCarcel(tiradasCarcel + 1);
            Dado dado1 = new Dado();
            Dado dado2 = new Dado();
            int valorDado1 = dado1.hacerTirada();
            int valorDado2 = dado2.hacerTirada();
            int valorTirada = valorDado1 + valorDado2;

            if (valorDado1 == valorDado2) {
                System.out.println(jugador.getNombre() + " ha sacado dobles y sale de la cárcel. Avanza " + valorTirada + " casillas.\n");
                jugador.setEnCarcel(false);
                jugador.setTiradasCarcel(0);
                jugador.getAvatar().moverAvatar(tablero.getPosiciones(), valorTirada);
            } else {
                System.out.println(jugador.getNombre() + " no ha sacado dobles y permanece en la cárcel.\n");
            }
        }
    }
    
    

    // Metodo que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
    }

    // Metodo que realiza las acciones asociadas al comando 'listar jugadores'.
    private void listarJugadores() {
        if (jugadores.isEmpty()) {
            System.out.println("*** Ningún jugador registrado. ***\n");
            return;
        }
        for (Jugador jugador : jugadores) {
            String nombre = jugador.getNombre();
            descJugador(nombre);
            System.out.println("},\n{");
        }
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
