package monopoly;

import java.awt.event.ComponentAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import partida.*;

public class Menu {

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private final List<String> tipos = new ArrayList<>(List.of("sombrero", "esfinge", "pelota", "coche"));
    private int turno = 0; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    private int valorTirada;
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

    public List<String> getTipos() {
        return tipos;
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
                System.out.println("\t*** Argumentos incorrectos. ***");
                System.out.println("\tUso: crear jugador <Nombre> <Tipo avatar>");
                System.out.println("}\n");
                return;
            }
            String nombre = palabras[2];
            String tipo = palabras[3].toLowerCase(Locale.ROOT);
            int numJ1 = (jugadores == null) ? 0 : jugadores.size();
            Jugador jugador = new Jugador(nombre, tipo, tablero.encontrar_casilla("Salida"), getAvatares());
            // si se pudo crear el jugador => numJ2 sera numJ1 + 1
            int numJ2 = (jugadores == null) ? 0 : jugadores.size();
            if (numJ2 > numJ1) descJugador(nombre);
        }
        // describe el jugador del turno actual
        else if (comando.equals("jugador")) {
            if (jugadores == null || jugadores.isEmpty()) {
                System.out.println("\t*** Ningún jugador registrado. ***");
                System.out.println("}\n");
                return;
            }
            Jugador jActual = jugadores.get(turno % jugadores.size());
            descJugador(jActual.getNombre());
        }
        // describe el jugador que se indique en la línea de comandos
        else if (comando.contains("describir jugador")) {
            if (numPalabras != 3) {
                System.out.println("\t*** Argumentos incorrectos. ***");
                System.out.println("\tUso: describir jugador <Nombre>");
                System.out.println("}\n");
                return;
            }
            String nombre = palabras[2].trim();
            descJugador(nombre);
        }
        // creo que no hay que hacerlo para la entrega 1 (CREO)
        else if (comando.contains("describir avatar")) {
        } ///////////////////////////////////
        // describe la casilla indicada en la línea de comandos
        else if (comando.contains("describir")) {
            if (numPalabras != 2) {
                System.out.println("\t*** Argumentos incorrectos. ***");
                System.out.println("\tUso: describir <Nombre casilla>");
                System.out.println("}\n");
                return;
            }
            String nombre = palabras[1].trim();
            if (nombre.equals("casilla")) {
                nombre = jugadores.get(turno % jugadores.size()).getAvatar().getCasilla().getNombre();
                descCasilla(nombre);
            }
            else{
            descCasilla(nombre);
            }
        }
        // hace una tirada de dados
        else if (comando.contains("lanzar dados")) {
            // tirada aleatoria
            if (numPalabras == 2) lanzarDados(0, 0, false);
                // tirada forzada con el valor pasado por la línea de comandos
            else if (numPalabras == 3) {
                String[] dados = palabras[2].split("\\+"); // separar por el '+'
                if (dados.length != 2) {
                    System.out.println("\t*** Formato incorrecto. Uso: lanzar dados <Dado1>+<Dado2> ***");
                    System.out.println("}\n");
                    return;
                }
                try {
                    int d1 = Integer.parseInt(dados[0].trim()); // primer dado
                    int d2 = Integer.parseInt(dados[1].trim()); // segundo dado
                    lanzarDados(d1, d2, true); // lanzada forzada
                } catch (NumberFormatException e) {
                    System.out.println("\t*** Valores de dados incorrectos. Deben ser números del 1 al 6.");
                    System.out.println("}\n");
                }
            } else {
                System.out.println("\t*** Argumentos incorrectos. ***");
                System.out.println("\tUso: lanzar dados <Dado1>+<Dado2>");
                System.out.println("}\n");
                return;
            }
        }
        // compra una casilla
        else if (comando.contains("comprar")) {
            if (numPalabras != 2) {
                System.out.println("\t*** Argumentos incorrectos. ***");
                System.out.println("\tUso: comprar <Nombre casilla>");
                System.out.println("}\n");
                return;
            }
            String nombre = palabras[1].trim();
            comprar(nombre);
        }
        // construye un edificio
        else if (comando.contains("edificar")) {
            if (numPalabras != 2) {
                System.out.println("\t*** Argumentos incorrectos. ***");
                System.out.println("\tUso: comprar <tipo edificio>");
                System.out.println("}\n");
                return;
            }
            String tipo = palabras[1].trim().toLowerCase(Locale.ROOT);
            if (jugadores == null || jugadores.isEmpty()) {
                System.out.println("\t *** No hay jugadores en la partida. ***");
                System.out.println("}\n");
                return;
            }
            Casilla cas = jugadores.get(turno).getAvatar().getCasilla();
            if (!cas.getTipo().equals("Solar")) {
                System.out.println("\t*** Solo se puede edificar en solares. ***");
                System.out.println("}\n");
                return;
            }
            cas.edificar(tipo);
        }
        // saca al jugador actual de la carcel si puede
        else if (comando.equals("salir cárcel")) salirCarcel();
            // lista las casillas en venta
        else if (comando.contains("listar enventa")) {
            if (numPalabras == 3) {
                Grupo grupo = tablero.getGrupos().get(palabras[2]);
                listarVenta(grupo);
            }
            else if (numPalabras == 2) listarVenta();
            else {
                System.out.println("\t*** Argumentos incorrectos. ***");
                System.out.println("\tUso: listar enventa <color grupo>");
                System.out.println("}\n");
                return;
            }

        }
            // lista los jugadores activos
        else if (comando.equals("listar jugadores")) listarJugadores();
            // lista los avatares de los jugadores activos, tmp hay que hacer para la entrega 1 (CREO)
        else if (comando.equals("listar avatares")) {
        }
        // finaliza el turno
        else if (comando.equals("acabar turno")) acabarTurno();
            // imprime el tablero
        else if (comando.equals("ver tablero")) {
            System.out.println("Imprimiendo tablero...\n");
            System.out.println(tablero.toString());
        }
    
        // finaliza el programa
        else if (comando.equals("salir")) System.out.println("\tSaliendo del programa...");
        else {
            System.out.println("\t*** Comando no registrado. ***");
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
        float fort = jugador.getFortuna();
        String fortuna = String.format("%.1f", fort);
        ArrayList<String> propiedades = new ArrayList<>();
        if (jugador.getPropiedades() != null) {
            for (Casilla c : jugador.getPropiedades()) propiedades.add(c.getNombre());
        }
        ArrayList<String> edificios = new ArrayList<>();
        for (Casilla c : jugador.getPropiedades()) {
            if (c.getEdificios() != null && !c.getEdificios().isEmpty()) {
                for (Edificio ed : c.getEdificios()) {
                    edificios.add(ed.getNombre());
                }
            }
        }

        // hipotecas

        System.out.println("\tNombre: " + nombre);
        System.out.println("\tAvatar: " + avatar);
        System.out.println("\tFortuna: " + fortuna);
        System.out.println("\tPropiedades: " + propiedades);
        System.out.println("\tEdificios: " + edificios);
        // sout(hipotecas)
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
        if (tirado && !forzado) {
            System.out.println("\t*** Solo una tirada por turno. ***");
            return;
        }

        if (jugadores == null || jugadores.isEmpty()) {
            System.out.println("\t*** No hay jugadores en la partida. ***");
            return;
        }

        Jugador jugadorActual = jugadores.get(turno % jugadores.size());
        Avatar avatarActual = jugadorActual.getAvatar();
        // --- COMPROBAR SI EL JUGADOR ESTÁ EN LA CÁRCEL ---
        if (jugadorActual.isEnCarcel()) {
            System.out.println(jugadorActual.getNombre() + " está en la cárcel.");

            System.out.println("Debe usar el comando 'salir carcel' para intentar salir (pagando o sacando dobles).");
            return; // Finaliza el turno
        }

        int dado1Val;
        int dado2Val;
        boolean volverATirar = true;

        while (volverATirar) {
            // Determinar tirada
            if (forzado) {
                dado1Val = dado1Forzado;
                dado2Val = dado2Forzado;
            } else {
                dado1Val = new Dado().hacerTirada();
                dado2Val = new Dado().hacerTirada();
            }
            valorTirada = dado1Val + dado2Val;

            System.out.println("\tTirada: " + dado1Val + " + " + dado2Val + " = " + valorTirada);

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
                    // acabarTurno(); // <-- Aquí es clave
                    return;
                } else {
                    System.out.println("\t¡Dobles! " + avatarActual.getJugador().getNombre() + " vuelve a tirar.");
                }
            } else {
                jugadorActual.setDoblesConsecutivos(0); // Reiniciar si no es doble
            }
            // Mover avatar
            avatarActual.moverAvatar(tablero.getPosiciones(), valorTirada);

            if (forzado) break;
            if (dado1Val != dado2Val) volverATirar = false; // No seguir tirando si no es doble
            else {
                try {
                    Thread.sleep(1000); // pausa de 1 segundos
                } catch (InterruptedException e) {
                }
            }
        }

        // Gestionar IrCarcel y otras casillas
        Casilla destino = avatarActual.getCasilla();
        if (destino.getNombre().equals("IrCarcel")) {
            Casilla carcel = tablero.encontrar_casilla("Cárcel");
            avatarActual.moverAvatar(tablero.getPosiciones(), carcel.getPosicion() - destino.getPosicion());
            jugadorActual.setEnCarcel(true);

            jugadorActual.setDoblesConsecutivos(0);

            System.out.println("\t" + jugadorActual.getNombre() + " va a la cárcel.");
            System.out.println(tablero.toString());
            // acabarTurno();
            return; // Terminar turno
        } else {
            // Gestionar pagos normales
            destino.gestionarPago(jugadorActual, tablero.getBanca(), valorTirada);
        }

        // Mostrar tablero actualizado
        try {
            Thread.sleep(2000); // pausa de 2 segundos
        } catch (InterruptedException e) {
        }
        System.out.println(tablero.toString());

        tirado = true; // marcar que el jugador ya tiró
    }


    /*Metodo que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
     * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) {
        if (jugadores == null) {
            System.out.println("\t*** No hay jugadores en la partida. ***");
            return;
        }
        Casilla c = tablero.encontrar_casilla(nombre);
        Jugador jActual = jugadores.get(turno % jugadores.size());
        c.comprarCasilla(jActual, banca);
    }

    //Metodo que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
    /*CAMBIOS POR HACER:
        * - En la tercera tirada si no sacas dobles te tiene que obligar a pagar y luego salir de la carcel con esa tirada.
     */
    private void salirCarcel() {

        if (jugadores == null || jugadores.isEmpty()) {
            System.out.println("\t*** No hay jugadores en la partida. ***");
            return;
        }

        Jugador jugador = jugadores.get(turno%jugadores.size());
        float fortuna = jugador.getFortuna();
        float precio_carcel = 500000f;
        int tiradasCarcel = jugador.getTiradasCarcel();

        if (!jugador.isEnCarcel()) {
            System.out.println("\t*** No estas en la carcel. ***");
            return;
        }

        if (tiradasCarcel >= 3) {
            if(fortuna >= precio_carcel){
                System.out.println(jugador.getNombre() + " ha superado el límite de tiradas en la cárcel y paga 500.000€ para salir.\n");
                jugador.sumarFortuna(-precio_carcel);
                jugador.setEnCarcel(false);
                jugador.setTiradasCarcel(0);
                return;
            }
        }
        // Preguntar al jugador
        Scanner sc = new Scanner(System.in);
        System.out.println("\tDesea salir de la cárcel pagando 500.000€? (si/no)");
        String respuesta = sc.nextLine();

        if(respuesta.equalsIgnoreCase("si")) {
            if(fortuna >= precio_carcel){
                jugador.sumarFortuna(-precio_carcel);
                jugador.setEnCarcel(false);
                jugador.setTiradasCarcel(0);
            } else {
                System.out.println(jugador.getNombre() + " no posee suficiente dinero para pagar la salida. Debe esperar a su proximo turno.\n");
                acabarTurno();
            }
        } else if (respuesta.equalsIgnoreCase("no")) {
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
                acabarTurno();
            }
        }
        else {
            System.out.println("Respuesta no válida. Debe responder 'si' o 'no'.\n");
            salirCarcel();
        }
    }

    // Metodo que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
        ArrayList<Casilla> sinDuenho = banca.getPropiedades();
        ArrayList<Casilla> enVenta = new ArrayList<>();

        for (Casilla c : sinDuenho) {
            if (c.getTipo().equals("Solar") || c.getTipo().equals("Servicios") || c.getTipo().equals("Transporte")) {
                enVenta.add(c);
            }
        }
        for (Casilla c : enVenta) {
            String nombre = c.getNombre();
            descCasilla(nombre);
            System.out.println("},\n{");
        }
    }

    private void listarVenta(Grupo g) {
        ArrayList<Casilla> sinDuenho = banca.getPropiedades();
        ArrayList<Casilla> enVenta = new ArrayList<>();

        for (Casilla c : sinDuenho) {
            if (c.getTipo().equals("Solar") && c.getGrupo().equals(g)) {
                enVenta.add(c);
            }
        }

        if (enVenta.isEmpty()) {
            System.out.println("\t*** Grupo no encontrado. ***");
            return;
        }

        for (Casilla c : enVenta) {
            String nombre = c.getNombre();
            descCasilla(nombre);
            System.out.println("},\n{");
        }
    }

    // Metodo que realiza las acciones asociadas al comando 'listar jugadores'.
    private void listarJugadores() {
        if (jugadores == null || jugadores.isEmpty()) {
            System.out.println("\t*** Ningún jugador registrado. ***");
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
            System.out.println("\t*** No hay jugadores en la partida. ***");
            return;
        }

        // Opcional: comprobar si el jugador actual ya tiró
        if (!tirado) {
            System.out.println("\t*** El jugador actual debe lanzar los dados antes de acabar su turno. ***");
            return;
        }

        Jugador jugadorAnterior = jugadores.get(turno);
        // Pasar al siguiente jugador
        turno = (turno + 1) % jugadores.size();

        // Resetear tirada
        tirado = false;

        Jugador jugadorActual = jugadores.get(turno);
        System.out.println("\tEl turno pasa de " + jugadorAnterior.getNombre() + " a " + jugadorActual.getNombre() + ".");
    }

    public void analizarArchivo(String[] args) {
        if (args.length != 1) {
            System.out.println("*** Argumentos incorrectos. ***");
            System.out.println("Uso: java <ejecutable> <nombre archivo>");
            return;
        }

        try {
            // Crear un objeto File que apunte al archivo
            File archivo = new File(args[0]);

            // Crear el Scanner asociado al archivo
            Scanner sc = new Scanner(archivo);

            // Bucle que se ejecuta mientras haya más líneas por leer
            while (sc.hasNextLine()) {
                // Leer una línea completa
                String linea = sc.nextLine();
                System.out.println(linea);

                // Procesar o mostrar la línea
                analizarComando(linea);
            }

            // Cerrar el Scanner cuando termines
            sc.close();

        } catch (FileNotFoundException e) {
            System.out.println("*** Error: no se encontró el archivo. ***\n");
        }
    }

}
