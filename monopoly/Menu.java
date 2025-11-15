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
    public CartaComunidad cartasComunidad;
    public CartaSuerte cartasSuerte;

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

        this.cartasComunidad = new CartaComunidad();
        this.cartasSuerte = new CartaSuerte();
    }

    public int getTurno() {
        return turno;
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

        if (comando.equals("help")) {
            System.out.println("=================================================Lista de Comandos=================================================");
            System.out.println("-> Añade un nuevo jugador a la partida:                                   crear jugador <nombre> <tipo avatar>");
            System.out.println("-> Hace una tirada de dados:                                              lanzar dados");
            System.out.println("-> Finaliza el turno del jugador actual:                                  acabar turno");
            System.out.println("-> Muestra el tablero por pantalla:                                       ver tablero");
            System.out.println("-> Compra la casilla indicada:                                            comprar <Nombre casilla>");
            System.out.println("-> Construye un edificio en la casilla donde se encuentra el jugador:     edificar <tipo edificio>");
            System.out.println("-> Vende el numero de edificios deseado de la casilla indicada:           vender <tipo edificio> <casilla> <cantidad>");
            System.out.println("-> Saca de la carcel al jugador actual:                                   salir carcel");
            System.out.println("-> Fuerza una tirada no aleatoria de dados:                               lanzar dados <Dado1>+<Dado2>");
            System.out.println("-> Hipoteca una casilla en propiedad:                                     hipotecar <nombre casilla>");
            System.out.println("-> Deshipoteca una casilla en propiedad:                                  deshipotecar <nombre casilla>");
            System.out.println("\n-> Muestra los datos del jugador actual:                                  jugador");
            System.out.println("-> Muestra los datos del jugador indicado:                                describir jugador <nombre>");
            System.out.println("-> Muestra los datos de la casilla actual:                                describir casilla");
            System.out.println("-> Muestra los datos de la casilla indicada:                              describir <nombre casilla>");
            System.out.println("\n-> Muestra todas las casillas en venta:                                   listar enventa");
            System.out.println("-> Muestra todas las casillas en venta de un mismo grupo:                 listar enventa <color grupo>");
            System.out.println("-> Muestra los jugadores activos:                                         listar jugadores");
            System.out.println("-> Muestra todos los edificios construídos:                               listar edificios");
            System.out.println("-> Muestra todos los edificios construídos de un mismo grupo:             listar edificios <color grupo>");
            System.out.println("\n-> Muestra las estadísticas globales de la partida:                       estadisticas");
            System.out.println("-> Muestra las estadísticas de un jugador activo:                         estadisticas <nombre jugador>");
            System.out.println("\n-> Finaliza la partida inmediatamente:                                    salir");
            System.out.println("==================================================================================================================\n");
            return;
        }

        System.out.println("{");

        // crea un jugador nuevo
        if (comando.startsWith("crear jugador ")) {
            if (numPalabras != 4) {
                System.out.println("\t*** Formato incorrecto. ***\n\tUso: crear jugador <Nombre> <Tipo avatar>");
                System.out.println("}\n");
                return;
            }
            if (jugadores != null && jugadores.size() >= 4) {
                System.out.println("\t*** Máximo de 4 jugadores alcanzado. ***");
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
        // hace una tirada de dados
        else if (comando.startsWith("lanzar dados")) {
            // tirada aleatoria
            if (numPalabras == 2) lanzarDados(0, 0, false);

                // tirada forzada con el valor pasado por la línea de comandos
            else if (numPalabras == 3) {
                String[] dados = palabras[2].split("\\+"); // separar por el '+'
                if (dados.length != 2) {
                    System.out.println("\t*** Formato incorrecto. ***\n\tUso: lanzar dados <Dado1>+<Dado2>");
                    System.out.println("}\n");
                    return;
                }
                try {
                    int d1 = Integer.parseInt(dados[0].trim()); // primer dado
                    int d2 = Integer.parseInt(dados[1].trim()); // segundo dado
                    lanzarDados(d1, d2, true); // lanzada forzada
                } catch (NumberFormatException e) {
                    System.out.println("\t*** Valores de dados incorrectos. ***\n\tDeben ser números del 1 al 6.");
                    System.out.println("}\n");
                }
            } else {
                System.out.println("\t*** Formato incorrecto. ***\n\tUso: lanzar dados <Dado1>+<Dado2>");
                System.out.println("}\n");
                return;
            }
        }
        // finaliza el turno
        else if (comando.equals("acabar turno")) acabarTurno();

        // imprime el tablero
        else if (comando.equals("ver tablero")) {
            System.out.println("Imprimiendo tablero...\n");
            System.out.println(tablero.toString());
        }
        // compra una casilla
        else if (comando.startsWith("comprar ")) {
            if (numPalabras != 2) {
                System.out.println("\t*** Formato incorrecto. ***\n\tUso: comprar <Nombre casilla>");
                System.out.println("}\n");
                return;
            }
            String nombre = palabras[1].trim();
            comprar(nombre);
        }
        // construye un edificio
        else if (comando.startsWith("edificar ")) {
            if (numPalabras != 2) {
                System.out.println("\t*** Formato incorrecto. ***\n\tUso: edificar <tipo edificio>");
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
        // vende un edificio
        else if (comando.startsWith("vender ")) {
            if (numPalabras != 4) {
                System.out.println("\t*** Formato incorrecto. ***\n\tUso: vender <tipo edificio> <casilla> <cantidad>");
                System.out.println("}\n");
                return;
            }
            String tipo = palabras[1].trim();
            int n = Integer.parseInt(palabras[3].trim());
            Casilla cas = tablero.encontrar_casilla(palabras[2].trim());
            cas.venderEdificio(tipo, n);
        }
        // saca al jugador actual de la carcel si puede
        else if (comando.equals("salir carcel")) salirCarcel();
        // hipotecar una propiedad
        else if (comando.startsWith("hipotecar")) {
            if (numPalabras != 2) {
                System.out.println("\t*** Formato incorrecto. ***\n\tUso: hipotecar <nombre casilla>");
                System.out.println("}\n");
                return;
            }

            Jugador jugadorActual = jugadores.get(turno % jugadores.size());
            String nombreCasilla = palabras[1].trim();
            Casilla casilla = tablero.encontrar_casilla(nombreCasilla);

            if (casilla == null) {
                System.out.println("\t*** La casilla " + nombreCasilla + " no existe. ***");
                System.out.println("}\n");
                return;
            }

            casilla.hipotecar(jugadorActual);
        }
        // deshipotecar una propiedad
        else if (comando.startsWith("deshipotecar")) {
            if (numPalabras != 2) {
                System.out.println("\t*** Formato incorrecto. ***\n\tUso: deshipotecar <nombre casilla>");
                System.out.println("}\n");
                return;
            }

            Jugador jugadorActual = jugadores.get(turno % jugadores.size());
            String nombreCasilla = palabras[1].trim();
            Casilla casilla = tablero.encontrar_casilla(nombreCasilla);

            if (casilla == null) {
                System.out.println("\t*** La casilla " + nombreCasilla + " no existe. ***");
                System.out.println("}\n");
                return;
            }

            casilla.deshipotecar(jugadorActual);
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
        else if (comando.startsWith("describir jugador ")) {
            if (numPalabras != 3) {
                System.out.println("\t*** Formato incorrecto. ***\n\tUso: describir jugador <Nombre>");
                System.out.println("}\n");
                return;
            }
            String nombre = palabras[2].trim();
            descJugador(nombre);
        }

        // creo que no hay que hacerlo para la entrega 1 (CREO)
        // else if (comando.startsWith("describir avatar")) {
        // }

        // describe la casilla indicada en la línea de comandos
        else if (comando.startsWith("describir ")) {
            if (numPalabras != 2) {
                System.out.println("\t*** Formato incorrecto. ***\n\tUso: describir <Nombre casilla>");
                System.out.println("}\n");
                return;
            }
            String nombre = palabras[1].trim();
            // describe la casilla en la que se encuentra el jugador
            if (nombre.equals("casilla")) {
                nombre = jugadores.get(turno % jugadores.size()).getAvatar().getCasilla().getNombre();
                descCasilla(nombre);
            }
            // describe la casilla con el nombre pasado por linea de comandos
            else{
                descCasilla(nombre);
            }
        }
        // lista las casillas en venta
        else if (comando.startsWith("listar enventa")) {
            if (numPalabras == 3) {
                Grupo grupo = tablero.getGrupos().get(palabras[2]);
                listarVenta(grupo);
            }
            else if (numPalabras == 2) listarVenta();
            else {
                System.out.println("\t*** Formato incorrecto. ***\n\tUso: listar enventa <color grupo>");
                System.out.println("}\n");
                return;
            }

        }
        // lista los jugadores activos
        else if (comando.equals("listar jugadores")) listarJugadores();

        // lista los avatares de los jugadores activos, tmp hay que hacer para la entrega 1 (CREO)
        // else if (comando.equals("listar avatares")) {
        // }

        // lista los edificios construidos
        else if (comando.startsWith("listar edificios")) {
            if (numPalabras == 3) {
                Grupo grupo = tablero.getGrupos().get(palabras[2]);
                if (grupo == null) {
                    System.out.println("\t*** Ese grupo no existe. ***");
                    System.out.println("}\n");
                    return;
                }
                listarEdificios(grupo);
                return;
            }
            listarEdificios();
        }
        // mostrar las estadísticas del jugador actual
        else if (comando.startsWith("estadisticas")) {
            if (jugadores == null || jugadores.isEmpty()) {
                System.out.println("\t*** No hay jugadores activos. ***");
                System.out.println("}\n");
                return;
            }

            // Si el comando solo tiene "estadisticas", sin nombre de jugador
            if (numPalabras == 1) {
                tablero.estadisticas();
                return;
            }

            // Tomamos el nombre del jugador
            String nombreJugador = palabras[1].trim();
            Jugador jugadorEncontrado = null;

            // Buscar al jugador por nombre (ignora mayúsculas/minúsculas)
            for (Jugador j : jugadores) {
                if (j.getNombre().equalsIgnoreCase(nombreJugador)) {
                    jugadorEncontrado = j;
                    break;
                }
            }

            if (jugadorEncontrado == null) {
                System.out.println("\t*** No se encontró al jugador " + nombreJugador + ". ***");
                System.out.println("}\n");
                return;
            }

            // Mostrar estadísticas del jugador encontrado
            jugadorEncontrado.mostrarEstadisticas();
        }
    
        // finaliza el programa
        else if (comando.equals("salir")) System.out.println("\tSaliendo del programa...");
        else {
            System.out.println("\t*** Comando no registrado. ***\n\tIntroduce el comando 'help' para ver los comandos disponibles.");
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
            System.out.println("\t*** " + jugadorActual.getNombre() + " está en la cárcel. ***");

            System.out.println("\tDebe usar el comando 'salir carcel' para intentar salir (pagando o sacando dobles).");
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

                // tres dobles = carcel
                if (jugadorActual.getDoblesConsecutivos() == 3) {
                    jugadorActual.encarcelar();
                    jugadorActual.setDoblesConsecutivos(0);
                    jugadorActual.incrementarVecesEnCarcel();
                    // acabarTurno();
                    return;
                } else {
                    System.out.println("\t¡Dobles! " + avatarActual.getJugador().getNombre() + " vuelve a tirar.");
                }
            } else {
                jugadorActual.setDoblesConsecutivos(0); // Reiniciar si no es doble
            }
            // Mover avatar
            avatarActual.moverAvatar(valorTirada);

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
        destino.incrementarFrecuencia();
        if (destino.getNombre().equals("IrCarcel")) {
            jugadorActual.encarcelar();
            jugadorActual.setDoblesConsecutivos(0);
            jugadorActual.incrementarVecesEnCarcel();
            // acabarTurno();
            return; // Terminar turno
        } 
        else if(destino.getTipo().equals("Comunidad")){
            cartasComunidad.sacarCartaComunidad();
        }
        else if(destino.getTipo().equals("Suerte")){
            cartasSuerte.sacarCartaSuerte();
        }
        else {
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
            System.out.println("\t*** No estás en la carcel. ***");
            return;
        }

        if (tiradasCarcel >= 3) {
            if(fortuna >= precio_carcel){
                System.out.println("\t" + jugador.getNombre() + " ha superado el límite de tiradas en la cárcel y paga 500.000€ para salir.\n");
                jugador.sumarFortuna(-precio_carcel);
                jugador.setEnCarcel(false);
                jugador.setTiradasCarcel(0);
                return;
            }
        }
        // Preguntar al jugador
        String respuesta = "";
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("\tDesea salir de la cárcel pagando 500.000€? (si/no)");
            respuesta = sc.nextLine();
            if (!respuesta.equals("si") && !respuesta.equals("no")) {
                System.out.println("\t*** Respuesta no registrada, debes responder 'si' o 'no'. ***");
                continue;
            }
            break;
        }

        if(respuesta.equalsIgnoreCase("si")) {
            if(fortuna >= precio_carcel){
                System.out.println("\t" + jugador.getNombre() + " paga 500.000€ y sale de la cárcel.");
                jugador.sumarFortuna(-precio_carcel);
                jugador.setEnCarcel(false);
                jugador.setTiradasCarcel(0);
                return;
            } else
                System.out.println("\t*** " + jugador.getNombre() + " no posee suficiente dinero para pagar la salida. Debe lanzar los dados. ***\n");
        }
        else System.out.println("\t" + jugador.getNombre() + " busca sacar dobles...");

        jugador.setTiradasCarcel(tiradasCarcel + 1);
        Dado dado1 = new Dado();
        Dado dado2 = new Dado();
        int valorDado1 = dado1.hacerTirada();
        int valorDado2 = dado2.hacerTirada();
        int valorTirada = valorDado1 + valorDado2;
        tirado = true;

        if (valorDado1 == valorDado2) {
            System.out.println("\t" + jugador.getNombre() + " ha sacado dobles " + Valor.GREEN + "(" + valorDado1 + "+" + valorDado2 + ")" + Valor.RESET + " y sale de la cárcel. Avanza " + valorTirada + " casillas.\n");
            jugador.setEnCarcel(false);
            jugador.setTiradasCarcel(0);
            jugador.getAvatar().moverAvatar(valorTirada);
        } else {
            System.out.println("\t" + jugador.getNombre() + " no ha sacado dobles " + Valor.RED + "(" + valorDado1 + "+" + valorDado2 + ")" + Valor.RESET + " y permanece en la cárcel.\n");
            acabarTurno();
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

    private void listarEdificios() {
        ArrayList<Edificio> casas = tablero.getCasas();
        ArrayList<Edificio> hoteles = tablero.getHoteles();
        ArrayList<Edificio> piscinas = tablero.getPiscinas();
        ArrayList<Edificio> pistas = tablero.getPistas();

        for (Edificio ed : casas) {
            System.out.println("\tid: " + ed.getNombre());
            System.out.println("\tpropietario: " + ed.getCasilla().getDuenho().getNombre());
            System.out.println("\tcasilla:  " + ed.getCasilla().getNombre());
            System.out.println("\tgrupo: " + ed.getCasilla().getGrupo().getCodigoColor() + ed.getCasilla().getGrupo().getColorGrupo() + Valor.RESET);
            System.out.println("\tcoste: " + ed.getCoste());
            System.out.println("}\n{");
        }

        for (Edificio ed : hoteles) {
            System.out.println("\tid: " + ed.getNombre());
            System.out.println("\tpropietario: " + ed.getCasilla().getDuenho().getNombre());
            System.out.println("\tcasilla:  " + ed.getCasilla().getNombre());
            System.out.println("\tgrupo: " + ed.getCasilla().getGrupo().getCodigoColor() + ed.getCasilla().getGrupo().getColorGrupo() + Valor.RESET);
            System.out.println("\tcoste: " + ed.getCoste());
            System.out.println("}\n{");
        }

        for (Edificio ed : piscinas) {
            System.out.println("\tid: " + ed.getNombre());
            System.out.println("\tpropietario: " + ed.getCasilla().getDuenho().getNombre());
            System.out.println("\tcasilla:  " + ed.getCasilla().getNombre());
            System.out.println("\tgrupo: " + ed.getCasilla().getGrupo().getCodigoColor() + ed.getCasilla().getGrupo().getColorGrupo() + Valor.RESET);
            System.out.println("\tcoste: " + ed.getCoste());
            System.out.println("}\n{");
        }

        for (Edificio ed : pistas) {
            System.out.println("\tid: " + ed.getNombre());
            System.out.println("\tpropietario: " + ed.getCasilla().getDuenho().getNombre());
            System.out.println("\tcasilla:  " + ed.getCasilla().getNombre());
            System.out.println("\tgrupo: " + ed.getCasilla().getGrupo().getCodigoColor() + ed.getCasilla().getGrupo().getColorGrupo() + Valor.RESET);
            System.out.println("\tcoste: " + ed.getCoste());
            System.out.println("}\n{");
        }
    }

    private void listarEdificios(Grupo g) {
        ArrayList<String > casas = new ArrayList<>();
        ArrayList<String > hoteles = new ArrayList<>();
        ArrayList<String > piscinas = new ArrayList<>();
        ArrayList<String > pistas = new ArrayList<>();
        ArrayList<Casilla> casillas = g.getMiembros();

        for (Casilla cas : casillas) {
            for (Edificio ed : cas.getEdificios()) {
                switch (ed.getTipo()) {
                    case "casa":
                        casas.add(ed.getNombre());
                        break;
                    case "hotel":
                        hoteles.add(ed.getNombre());
                        break;
                    case "piscina":
                        piscinas.add(ed.getNombre());
                        break;
                    case "pista":
                        pistas.add(ed.getNombre());
                }
            }
            System.out.println("\tpropiedad: " + cas.getNombre());
            System.out.println("\tcasas: " + casas);
            System.out.println("\thoteles: " + hoteles);
            System.out.println("\tpiscinas: " + piscinas);
            System.out.println("\tpistas de deporte: " + pistas);
            System.out.println("\timpuesto: " + cas.getImpuesto());

            int numCasas = casas.size();
            int numHoteles = hoteles.size();
            int numPiscinas = piscinas.size();
            int numPistas = pistas.size();

            if (numPistas > 0) {
                System.out.println("\tYa no se pueden construir más edificios.");
            }
            else if (numPiscinas > 0) {
                System.out.println("\tAún se puede construir 1 pista de deporte. Ya no se pueden construir más casas, hoteles o piscinas.");
            }
            else if (numHoteles > 0) {
                System.out.println("\tAún se pueden construir 1 piscina y 1 pista de deporte. Ya no se pueden construir más casas ni hoteles.");
            }
            else {
                if (numCasas == 4) {
                    System.out.println("\tAún se pueden construir 1 hotel, 1 piscina y 1 pista de deporte. Ya no se pueden construir más casas.");
                } else {
                    System.out.println("\tAún se pueden construir " + (4 - numCasas) + " casas.");
                }
            }

            System.out.println("}\n{");
            casas.clear();
            hoteles.clear();
            piscinas.clear();
            pistas.clear();
        }
    }

    // Metodo que realiza las acciones asociadas al comando 'acabar turno'.
    private void acabarTurno() {
        if (jugadores == null || jugadores.isEmpty()) {
            System.out.println("\t*** No hay jugadores en la partida. ***");
            return;
        }

        // comprobar si el jugador actual ya tiró
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
            System.out.println("*** No se ha podido leer el archivo. ***");
            System.out.println("Uso: java <ejecutable> <nombre archivo>\n\n");
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
