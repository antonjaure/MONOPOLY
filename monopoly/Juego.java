package monopoly;

import java.awt.event.ComponentAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


import excepciones.*;
import partida.*;

public class Juego implements Comando {

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
    private ArrayList<Carta> barajaSuerte;
    private ArrayList<Carta> barajaComunidad;
    private int indiceSuerte = 0;
    private int indiceComunidad = 0;
    public static ConsolaNormal consola = new ConsolaNormal();

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
    public Tablero getTablero() {
        return this.tablero;
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

    public Juego() {
        this.jugadores = new ArrayList<>();
        this.avatares = new ArrayList<>();
        this.banca = new Jugador();
        banca.setNombre("Banca");

        this.barajaSuerte = new ArrayList<>();
        this.barajaComunidad = new ArrayList<>();
        inicializarBarajas();
    }

    private void inicializarBarajas() {
        barajaSuerte.add(new Suerte("Decides hacer un viaje de placer. Avanza hasta Solar19. Si pasas por la casilla de Salida, cobra 2.000.000€.", 0));
        barajaSuerte.add(new Suerte("Los acreedores te persiguen por impago. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar los 2.000.000€.", 1));
        barajaSuerte.add(new Suerte("¡Has ganado el bote de la lotería! Recibe 1.000.000€.", 2));
        barajaSuerte.add(new Suerte("Has sido elegido presidente de la junta directiva. Paga a cada jugador 250.000€.", 3));
        barajaSuerte.add(new Suerte("¡Hora punta de tráfico! Retrocede tres casillas.", 4));
        barajaSuerte.add(new Suerte("Te multan por usar el móvil mientras conduces. Paga 150.000€.", 5));
        barajaSuerte.add(new Suerte("Avanza hasta la casilla de transporte más cercana. Si no tiene dueño, puedes comprarla...", 6));

        barajaComunidad.add(new CajaComunidad("Paga 500.000€ por un fin de semana en un balneario de 5 estrellas.", 0));
        barajaComunidad.add(new CajaComunidad("Te investigan por fraude de identidad. Ve a la Cárcel. Ve directamente...", 1));
        barajaComunidad.add(new CajaComunidad("Colócate en la casilla de Salida. Cobra 2.000.000€.", 2));
        barajaComunidad.add(new CajaComunidad("Devolución de Hacienda. Cobra 500.000€.", 3));
        barajaComunidad.add(new CajaComunidad("Retrocede hasta Solar1 para comprar antigüedades exóticas.", 4));
        barajaComunidad.add(new CajaComunidad("Ve a Solar20 para disfrutar del San Fermín. Si pasas por la casilla de Salida, cobra 2.000.000€.", 5));
    }


    public void sacarCartaSuerte() {
        if (barajaSuerte.isEmpty()) return;

        // 1. Obtener el jugador actual
        Jugador jugadorActual = jugadores.get(turno);

        // 2. Coger la carta que toca (usando el índice)
        Carta carta = barajaSuerte.get(indiceSuerte);
        
        // 3. Ejecutar la acción (Polimorfismo: la carta sabe qué hacer)
        carta.accion(jugadorActual);

        // 4. Actualizar el índice para la próxima vez (Ciclo: 0, 1, 2... 0)
        indiceSuerte = (indiceSuerte + 1) % barajaSuerte.size();
    }

    public void sacarCartaComunidad() {
        if (barajaComunidad.isEmpty()) return;

        Jugador jugadorActual = jugadores.get(turno);

        Carta carta = barajaComunidad.get(indiceComunidad);
        
        carta.accion(jugadorActual);

        indiceComunidad = (indiceComunidad + 1) % barajaComunidad.size();
    }


    public void iniciarPartida() {
        consola.imprimir(Valor.RED + "Monopoly ETSE\n" + Valor.RESET);
        consola.imprimir("Iniciando partida...\n");

        boolean salir = false;
        while (!salir) {
            // El propio Juego pide el comando a la consola
            String comando = consola.leer("Introduce comando: ");
            
            // Y el propio Juego lo procesa
            ejecutarComando(comando);
            
            if (comando.equals("salir")) {
                salir = true;
            }
        }
    }

    public int getTurno() {
        return turno;
    }


    /*Metodo que interpreta el comando introducido y toma la acción correspondiente.
     * Parámetro: cadena de caracteres (el comando).
     */
    private void ejecutarComando(String comando)  {

        String[] palabras = comando.trim().split("\\s+");  // divide por uno o más espacios
        int numPalabras = (comando.trim().isEmpty()) ? 0 : palabras.length;

        if (comando.equals("help")) {
            consola.imprimir("=================================================Lista de Comandos=================================================");
            consola.imprimir("-> Añade un nuevo jugador a la partida:                                   crear jugador <nombre> <tipo avatar>");
            consola.imprimir("-> Hace una tirada de dados:                                              lanzar dados");
            consola.imprimir("-> Finaliza el turno del jugador actual:                                  acabar turno");
            consola.imprimir("-> Muestra el tablero por pantalla:                                       ver tablero");
            consola.imprimir("-> Compra la casilla indicada:                                            comprar <Nombre casilla>");
            consola.imprimir("-> Construye un edificio en la casilla donde se encuentra el jugador:     edificar <tipo edificio>");
            consola.imprimir("-> Vende el numero de edificios deseado de la casilla indicada:           vender <tipo edificio> <casilla> <cantidad>");
            consola.imprimir("-> Saca de la carcel al jugador actual:                                   salir carcel");
            consola.imprimir("-> Fuerza una tirada no aleatoria de dados:                               lanzar dados <Dado1>+<Dado2>");
            consola.imprimir("-> Hipoteca una casilla en propiedad:                                     hipotecar <nombre casilla>");
            consola.imprimir("-> Deshipoteca una casilla en propiedad:                                  deshipotecar <nombre casilla>");
            consola.imprimir("\n-> Muestra los datos del jugador actual:                                  jugador");
            consola.imprimir("-> Muestra los datos del jugador indicado:                                describir jugador <nombre>");
            consola.imprimir("-> Muestra los datos de la casilla actual:                                describir casilla");
            consola.imprimir("-> Muestra los datos de la casilla indicada:                              describir <nombre casilla>");
            consola.imprimir("\n-> Muestra todas las casillas en venta:                                   listar enventa");
            consola.imprimir("-> Muestra todas las casillas en venta de un mismo grupo:                 listar enventa <color grupo>");
            consola.imprimir("-> Muestra los jugadores activos:                                         listar jugadores");
            consola.imprimir("-> Muestra todos los edificios construídos:                               listar edificios");
            consola.imprimir("-> Muestra todos los edificios construídos de un mismo grupo:             listar edificios <color grupo>");
            consola.imprimir("\n-> Muestra las estadísticas globales de la partida:                       estadisticas");
            consola.imprimir("-> Muestra las estadísticas de un jugador activo:                         estadisticas <nombre jugador>");
            consola.imprimir("\n-> Finaliza la partida inmediatamente:                                    salir");
            consola.imprimir("==================================================================================================================\n");
            return;
        }

        consola.imprimir("{");

        // crea un jugador nuevo
        if (comando.startsWith("crear jugador ")) {
            if (numPalabras != 4) {
                consola.imprimir("\t*** Formato incorrecto. ***\n\tUso: crear jugador <Nombre> <Tipo avatar>");
                consola.imprimir("}\n");
                return;
            }
            if (jugadores != null && jugadores.size() >= 4) { // O el límite que quieras (6)
                consola.imprimir("\t*** Máximo de jugadores alcanzado. ***");
                consola.imprimir("}\n");
                return;
            }
            
            String nombre = palabras[2];
            String tipo = palabras[3].toLowerCase(Locale.ROOT);
            Jugador jugador = new Jugador(nombre, tipo, tablero.encontrar_casilla("Salida"), getAvatares());
            
            boolean existe = false;
            for(Jugador j : this.jugadores) {
                if(j.getNombre().equals(nombre)) existe = true;
            }

            if (!existe) {
                this.jugadores.add(jugador);
                this.avatares.add(jugador.getAvatar()); 
                consola.imprimir("\tJugador " + nombre + " creado con éxito.\n");
                descJugador(nombre);
            } else {
                 consola.imprimir("\t*** El jugador ya existe o no se pudo crear. ***\n");
            }
        }
        // hace una tirada de dados
        else if (comando.startsWith("lanzar dados")) {
            // tirada aleatoria
            if (numPalabras == 2) {
                try {
                    lanzarDados(0, 0, false);
                } catch (DadosException e) {
                    consola.imprimir(e.getMessage());
                }
            }

                // tirada forzada con el valor pasado por la línea de comandos
            else if (numPalabras == 3) {
                String[] dados = palabras[2].split("\\+"); // separar por el '+'
                if (dados.length != 2) {
                    consola.imprimir("\t*** Formato incorrecto. ***\n\tUso: lanzar dados <Dado1>+<Dado2>");
                    consola.imprimir("}\n");
                    return;
                }
                try {
                    int d1 = Integer.parseInt(dados[0].trim()); // primer dado
                    int d2 = Integer.parseInt(dados[1].trim()); // segundo dado
                    lanzarDados(d1, d2, true); // lanzada forzada
                } catch (DadosException e) {
                    consola.imprimir(e.getMessage());
                    consola.imprimir("\nEste mensaje no se debería ver nunca");
                }
                
            } else {
                consola.imprimir("\t*** Formato incorrecto. ***\n\tUso: lanzar dados <Dado1>+<Dado2>");
                consola.imprimir("}\n");
                return;
            }
        }
        // finaliza el turno
        else if (comando.equals("acabar turno")) {
            
            try {
                acabarTurno();
            } catch (DadosSinLanzarException e) {
                consola.imprimir(e.getMessage());
            }        
        }

        // imprime el tablero
        else if (comando.equals("ver tablero")) {
            consola.imprimir("Imprimiendo tablero...\n");
            consola.imprimir(tablero.toString());
        }
        // compra una casilla
        else if (comando.startsWith("comprar ")) {
            if (numPalabras != 2) {
                consola.imprimir("\t*** Formato incorrecto. ***\n\tUso: comprar <Nombre casilla>");
                consola.imprimir("}\n");
                return;
            }
            String nombre = palabras[1].trim();
            comprar(nombre);
        }
        // construye un edificio
        else if (comando.startsWith("edificar ")) {
            if (numPalabras != 2) {
                consola.imprimir("\t*** Formato incorrecto. ***\n\tUso: edificar <tipo edificio>");
                consola.imprimir("}\n");
                return;
            }
            String tipo = palabras[1].trim().toLowerCase(Locale.ROOT);
            if (jugadores == null || jugadores.isEmpty()) {
                consola.imprimir("\t *** No hay jugadores en la partida. ***");
                consola.imprimir("}\n");
                return;
            }
            Casilla cas = jugadores.get(turno).getAvatar().getCasilla();
            if (!cas.getTipo().equals("Solar")) {
                consola.imprimir("\t*** Solo se puede edificar en solares. ***");
                consola.imprimir("}\n");
                return;
            }
            cas.edificar(tipo);
        }
        // vende un edificio
        else if (comando.startsWith("vender ")) {
            if (numPalabras != 4) {
                consola.imprimir("\t*** Formato incorrecto. ***\n\tUso: vender <tipo edificio> <casilla> <cantidad>");
                consola.imprimir("}\n");
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
                consola.imprimir("\t*** Formato incorrecto. ***\n\tUso: hipotecar <nombre casilla>");
                consola.imprimir("}\n");
                return;
            }

            Jugador jugadorActual = jugadores.get(turno % jugadores.size());
            String nombreCasilla = palabras[1].trim();
            Casilla casilla = tablero.encontrar_casilla(nombreCasilla);

            if (casilla == null) {
                consola.imprimir("\t*** La casilla " + nombreCasilla + " no existe. ***");
                consola.imprimir("}\n");
                return;
            }

            try{
                casilla.hipotecar(jugadorActual);
            } catch (PropiedadesException e) {
                consola.imprimir(e.getMessage());
                return;
            }
            //casilla.hipotecar(jugadorActual);
        }
        // deshipotecar una propiedad
        else if (comando.startsWith("deshipotecar")) {
            if (numPalabras != 2) {
                consola.imprimir("\t*** Formato incorrecto. ***\n\tUso: deshipotecar <nombre casilla>");
                consola.imprimir("}\n");
                return;
            }

            Jugador jugadorActual = jugadores.get(turno % jugadores.size());
            String nombreCasilla = palabras[1].trim();
            Casilla casilla = tablero.encontrar_casilla(nombreCasilla);

            if (casilla == null) {
                consola.imprimir("\t*** La casilla " + nombreCasilla + " no existe. ***");
                consola.imprimir("}\n");
                return;
            }

            try{
                casilla.deshipotecar(jugadorActual);
            } catch (PropiedadesException e) {
                consola.imprimir(e.getMessage());
                return;
            }
            //casilla.deshipotecar(jugadorActual);
        }

        // describe el jugador del turno actual
        else if (comando.equals("jugador")) {
            if (jugadores == null || jugadores.isEmpty()) {
                consola.imprimir("\t*** Ningún jugador registrado. ***");
                consola.imprimir("}\n");
                return;
            }
            Jugador jActual = jugadores.get(turno % jugadores.size());
            descJugador(jActual.getNombre());
        }
        // describe el jugador que se indique en la línea de comandos
        else if (comando.startsWith("describir jugador ")) {
            if (numPalabras != 3) {
                consola.imprimir("\t*** Formato incorrecto. ***\n\tUso: describir jugador <Nombre>");
                consola.imprimir("}\n");
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
                consola.imprimir("\t*** Formato incorrecto. ***\n\tUso: describir <Nombre casilla>");
                consola.imprimir("}\n");
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
                consola.imprimir("\t*** Formato incorrecto. ***\n\tUso: listar enventa <color grupo>");
                consola.imprimir("}\n");
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
                    consola.imprimir("\t*** Ese grupo no existe. ***");
                    consola.imprimir("}\n");
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
                consola.imprimir("\t*** No hay jugadores activos. ***");
                consola.imprimir("}\n");
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
                consola.imprimir("\t*** No se encontró al jugador " + nombreJugador + ". ***");
                consola.imprimir("}\n");
                return;
            }

            // Mostrar estadísticas del jugador encontrado
            jugadorEncontrado.mostrarEstadisticas();
        }
    
        // finaliza el programa
        else if (comando.equals("salir")) consola.imprimir("\tSaliendo del programa...");
        else {
            consola.imprimir("\t*** Comando no registrado. ***\n\tIntroduce el comando 'help' para ver los comandos disponibles.");
        }

        consola.imprimir("}\n");
    }

    // solución provisional, no sé como acceder a la función siendo privada si no es de esta manera

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

        consola.imprimir("\tNombre: " + nombre);
        consola.imprimir("\tAvatar: " + avatar);
        consola.imprimir("\tFortuna: " + fortuna);
        consola.imprimir("\tPropiedades: " + propiedades);
        consola.imprimir("\tEdificios: " + edificios);
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
            consola.imprimir("*** Casilla '" + nombre + "' no encontrada. ***\n");
            return;
        }
        consola.imprimir(casilla.infoCasilla());
    }

    //Metodo que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
    private void lanzarDados(int dado1Forzado, int dado2Forzado, boolean forzado) throws DadosException {
        if (tirado && !forzado) {
            DadosYaLanzadosException error = new DadosYaLanzadosException();
            throw error;    
        }

        if (jugadores == null || jugadores.isEmpty()) {
            consola.imprimir("\t*** No hay jugadores en la partida. ***");
            return;
        }

        Jugador jugadorActual = jugadores.get(turno % jugadores.size());
        Avatar avatarActual = jugadorActual.getAvatar();
        // --- COMPROBAR SI EL JUGADOR ESTÁ EN LA CÁRCEL ---
        if (jugadorActual.isEnCarcel()) {
            consola.imprimir("\t*** " + jugadorActual.getNombre() + " está en la cárcel. ***");

            consola.imprimir("\tDebe usar el comando 'salir carcel' para intentar salir (pagando o sacando dobles).");
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

            consola.imprimir("\tTirada: " + dado1Val + " + " + dado2Val + " = " + valorTirada);

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
                    consola.imprimir("\t¡Dobles! " + avatarActual.getJugador().getNombre() + " vuelve a tirar.");
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
            sacarCartaComunidad();
        }
        else if(destino.getTipo().equals("Suerte")){
            sacarCartaSuerte();
        }
        else {
            // Gestionar pagos normales
            destino.gestionarPago(jugadorActual, tablero.getBanca(), valorTirada);
        }

        // Mostrar tablero actualizado
        try {
            Thread.sleep(100); // pausa de 2 segundos
        } catch (InterruptedException e) {
        }
        consola.imprimir(tablero.toString());

        tirado = true; // marcar que el jugador ya tiró
    }


    /*Metodo que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
     * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    public void comprar(String nombre) {
        if (jugadores == null) {
            consola.imprimir("\t*** No hay jugadores en la partida. ***");
            return;
        }
        Casilla c = tablero.encontrar_casilla(nombre);
        if(c==null) {
            consola.imprimir("\t*** La casilla " + nombre + " no existe. ***");
            return;
        }
        Jugador jActual = jugadores.get(turno % jugadores.size());

        try{
            c.comprarCasilla(jActual, banca);
        } catch (PropiedadesException e) {
            consola.imprimir(e.getMessage());
            return;
        }
        //c.comprarCasilla(jActual, banca);
    }

    //Metodo que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
    /*CAMBIOS POR HACER:
        * - En la tercera tirada si no sacas dobles te tiene que obligar a pagar y luego salir de la carcel con esa tirada.
     */
    public void salirCarcel() {

        if (jugadores == null || jugadores.isEmpty()) {
            consola.imprimir("\t*** No hay jugadores en la partida. ***");
            return;
        }
        Jugador jugador = jugadores.get(turno%jugadores.size());
        float fortuna = jugador.getFortuna();
        float precio_carcel = 500000f;
        int tiradasCarcel = jugador.getTiradasCarcel();

        if (!jugador.isEnCarcel()) {
            consola.imprimir("\t*** No estás en la carcel. ***");
            return;
        }

        if (tiradasCarcel >= 3) {
            if(fortuna >= precio_carcel){
                consola.imprimir("\t" + jugador.getNombre() + " ha superado el límite de tiradas en la cárcel y paga 500.000€ para salir.\n");
                jugador.sumarFortuna(-precio_carcel);
                jugador.setEnCarcel(false);
                jugador.setTiradasCarcel(0);
                return;
            }
        }
        // Preguntar al jugador
        String respuesta = "";
        while (true) {
            respuesta = consola.leer("¿Desea salir pagando 500.000€? (si/no)");
            if (!respuesta.equals("si") && !respuesta.equals("no")) {
                consola.imprimir("\t*** Respuesta no registrada, debes responder 'si' o 'no'. ***");
                continue;
            }
            break;
        }

        if(respuesta.equalsIgnoreCase("si")) {
            if(fortuna >= precio_carcel){
                consola.imprimir("\t" + jugador.getNombre() + " paga 500.000€ y sale de la cárcel.");
                jugador.sumarFortuna(-precio_carcel);
                jugador.setEnCarcel(false);
                jugador.setTiradasCarcel(0);
                return;
            } else
                consola.imprimir("\t*** " + jugador.getNombre() + " no posee suficiente dinero para pagar la salida. Debe lanzar los dados. ***\n");
        }
        else consola.imprimir("\t" + jugador.getNombre() + " busca sacar dobles...");

        jugador.setTiradasCarcel(tiradasCarcel + 1);
        Dado dado1 = new Dado();
        Dado dado2 = new Dado();
        int valorDado1 = dado1.hacerTirada();
        int valorDado2 = dado2.hacerTirada();
        int valorTirada = valorDado1 + valorDado2;
        tirado = true;

        if (valorDado1 == valorDado2) {
            consola.imprimir("\t" + jugador.getNombre() + " ha sacado dobles " + Valor.GREEN + "(" + valorDado1 + "+" + valorDado2 + ")" + Valor.RESET + " y sale de la cárcel. Avanza " + valorTirada + " casillas.\n");
            jugador.setEnCarcel(false);
            jugador.setTiradasCarcel(0);
            jugador.getAvatar().moverAvatar(valorTirada);
        } else {
            consola.imprimir("\t" + jugador.getNombre() + " no ha sacado dobles " + Valor.RED + "(" + valorDado1 + "+" + valorDado2 + ")" + Valor.RESET + " y permanece en la cárcel.\n");
            try {
                acabarTurno();
            } catch (DadosSinLanzarException e) {
                consola.imprimir(e.getMessage());
            }
        }
    }

    // Metodo que realiza las acciones asociadas al comando 'listar enventa'.
    public void listarVenta() {
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
            consola.imprimir("},\n{");
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
            consola.imprimir("\t*** Grupo no encontrado. ***");
            return;
        }

        for (Casilla c : enVenta) {
            String nombre = c.getNombre();
            descCasilla(nombre);
            consola.imprimir("},\n{");
        }
    }

    // Metodo que realiza las acciones asociadas al comando 'listar jugadores'.
    public void listarJugadores() {
        if (jugadores == null || jugadores.isEmpty()) {
            consola.imprimir("\t*** Ningún jugador registrado. ***");
            return;
        }
        for (Jugador jugador : jugadores) {
            String nombre = jugador.getNombre();
            descJugador(nombre);
            consola.imprimir("},\n{");
        }
    }

    // Metodo que realiza las acciones asociadas al comando 'listar avatares'.
    private void listarAvatares() {
    }

    public void listarEdificios() {
        ArrayList<Edificio> casas = tablero.getCasas();
        ArrayList<Edificio> hoteles = tablero.getHoteles();
        ArrayList<Edificio> piscinas = tablero.getPiscinas();
        ArrayList<Edificio> pistas = tablero.getPistas();

        for (Edificio ed : casas) {
            consola.imprimir("\tid: " + ed.getNombre());
            consola.imprimir("\tpropietario: " + ed.getCasilla().getDuenho().getNombre());
            consola.imprimir("\tcasilla:  " + ed.getCasilla().getNombre());
            consola.imprimir("\tgrupo: " + ed.getCasilla().getGrupo().getCodigoColor() + ed.getCasilla().getGrupo().getColorGrupo() + Valor.RESET);
            consola.imprimir("\tcoste: " + ed.getCasilla().getValor());
            consola.imprimir("}\n{");
        }

        for (Edificio ed : hoteles) {
            consola.imprimir("\tid: " + ed.getNombre());
            consola.imprimir("\tpropietario: " + ed.getCasilla().getDuenho().getNombre());
            consola.imprimir("\tcasilla:  " + ed.getCasilla().getNombre());
            consola.imprimir("\tgrupo: " + ed.getCasilla().getGrupo().getCodigoColor() + ed.getCasilla().getGrupo().getColorGrupo() + Valor.RESET);
            consola.imprimir("\tcoste: " + ed.getCasilla().getValor());
            consola.imprimir("}\n{");
        }

        for (Edificio ed : piscinas) {
            consola.imprimir("\tid: " + ed.getNombre());
            consola.imprimir("\tpropietario: " + ed.getCasilla().getDuenho().getNombre());
            consola.imprimir("\tcasilla:  " + ed.getCasilla().getNombre());
            consola.imprimir("\tgrupo: " + ed.getCasilla().getGrupo().getCodigoColor() + ed.getCasilla().getGrupo().getColorGrupo() + Valor.RESET);
            consola.imprimir("\tcoste: " + ed.getCasilla().getValor());
            consola.imprimir("}\n{");
        }

        for (Edificio ed : pistas) {
            consola.imprimir("\tid: " + ed.getNombre());
            consola.imprimir("\tpropietario: " + ed.getCasilla().getDuenho().getNombre());
            consola.imprimir("\tcasilla:  " + ed.getCasilla().getNombre());
            consola.imprimir("\tgrupo: " + ed.getCasilla().getGrupo().getCodigoColor() + ed.getCasilla().getGrupo().getColorGrupo() + Valor.RESET);
            consola.imprimir("\tcoste: " + ed.getCasilla().getValor());
            consola.imprimir("}\n");
        }
    }

    private void listarEdificios(Grupo g) {
        if (jugadores == null) {
            consola.imprimir("\t*** No hay jugadores registrados. ***");
            consola.imprimir("}\n");
            return;
        }
        if (!g.esDuenhoGrupo(jugadores.get(turno))) {
            consola.imprimir("\t*** Nadie tiene el grupo completo en posesión. ***");
            consola.imprimir("}\n");
            return;
        }

        ArrayList<String > casas = new ArrayList<>();
        ArrayList<String > hoteles = new ArrayList<>();
        ArrayList<String > piscinas = new ArrayList<>();
        ArrayList<String > pistas = new ArrayList<>();
        ArrayList<Casilla> casillas = g.getMiembros();
        int numCasas = 0;
        int numHoteles = 0;
        int numPiscinas = 0;
        int numPistas = 0;
        int hotelesDisponibles = 0;

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
            consola.imprimir("\tpropiedad: " + cas.getNombre());
            consola.imprimir("\tcasas: " + casas);
            consola.imprimir("\thoteles: " + hoteles);
            consola.imprimir("\tpiscinas: " + piscinas);
            consola.imprimir("\tpistas de deporte: " + pistas);
            consola.imprimir("\timpuesto: " + (cas.getImpuesto() + cas.getImpuestoConstrucciones()) + "\n");

            numCasas += casas.size();
            numHoteles += hoteles.size();
            numPiscinas += piscinas.size();
            numPistas += pistas.size();

            if (casas.size() == 4) hotelesDisponibles++;

            consola.imprimir("},\n{");
            casas.clear();
            hoteles.clear();
            piscinas.clear();
            pistas.clear();
        }

        consola.imprimir("\tAun puedes construír:");
        if (numCasas + 4*numHoteles < 4 * g.getMiembros().size()) {
            consola.imprimir("\t\t-> " + ((4 * g.getMiembros().size() - numCasas) - 4*numHoteles) + " casas");
        }
        if (numHoteles < g.getMiembros().size()) {
            consola.imprimir("\t\t-> " + (g.getMiembros().size() - numHoteles) + " hoteles (" + hotelesDisponibles + " disponibles)");
        }
        if (numPiscinas < g.getMiembros().size()) {
            consola.imprimir("\t\t-> " + (g.getMiembros().size() - numPiscinas) + " piscinas (" + numHoteles + " disponibles)");
        }
        if (numPistas < g.getMiembros().size()) {
            consola.imprimir("\t\t-> " + (g.getMiembros().size() - numPistas) + " pistas (" + numPiscinas + " disponibles)");
        }
        consola.imprimir("}\n");
    }

    // Metodo que realiza las acciones asociadas al comando 'acabar turno'.
    public void acabarTurno() throws DadosSinLanzarException {
        if (jugadores == null || jugadores.isEmpty()) {
            consola.imprimir("\t*** No hay jugadores en la partida. ***");
            return;
        }

        // comprobar si el jugador actual ya tiró
        if (!tirado) {
            DadosSinLanzarException error = new DadosSinLanzarException();
            throw error;
        }

        Jugador jugadorAnterior = jugadores.get(turno);
        // Pasar al siguiente jugador
        turno = (turno + 1) % jugadores.size();

        // Resetear tirada
        tirado = false;

        Jugador jugadorActual = jugadores.get(turno);
        consola.imprimir("\tEl turno pasa de " + jugadorAnterior.getNombre() + " a " + jugadorActual.getNombre() + ".");
    }

    public void analizarArchivo(String[] args) {
        if (args.length != 1) {
            consola.imprimir("*** No se ha podido leer el archivo. ***");
            consola.imprimir("Uso: java <ejecutable> <nombre archivo>\n\n");
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
                consola.imprimir(linea + "\n");

                // Procesar o mostrar la línea
                ejecutarComando(linea);
            }

            // Cerrar el Scanner cuando termines
            sc.close();

        } catch (FileNotFoundException e) {
            consola.imprimir("*** Error: no se encontró el archivo. ***\n");
        }
    }
}
