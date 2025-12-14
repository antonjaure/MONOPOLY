package monopoly;

import java.awt.event.ComponentAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import javax.smartcardio.ATR;

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
    private ArrayList<Trato> tratos;

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
            tablero = new Tablero(this.banca);
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

    public ArrayList<Carta> getBarajaSuerte() {
        return barajaSuerte;
    }

    public ArrayList<Carta> getBarajaComunidad() {
        return barajaComunidad;
    }

    public Juego() {
        MonopolyETSE.juego = this; 

        this.jugadores = new ArrayList<>();
        this.avatares = new ArrayList<>();
        
        this.banca = new Jugador();
        banca.setNombre("Banca");


        this.tablero = new Tablero(this.banca);

        this.barajaSuerte = new ArrayList<>();
        this.barajaComunidad = new ArrayList<>();
        inicializarBarajas();

        this.tratos = new ArrayList<>();

        this.tablero.inicializar();
    }
    private void inicializarBarajas() {
        barajaSuerte.add(new Suerte("\tDecides hacer un viaje de placer. Avanza hasta Solar19. Si pasas por la casilla de Salida, cobra 2.000.000€.", 0));
        barajaSuerte.add(new Suerte("\tLos acreedores te persiguen por impago. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar los 2.000.000€.", 1));
        barajaSuerte.add(new Suerte("\t¡Has ganado el bote de la lotería! Recibe 1.000.000€.", 2));
        barajaSuerte.add(new Suerte("\tHas sido elegido presidente de la junta directiva. Paga a cada jugador 250.000€.", 3));
        barajaSuerte.add(new Suerte("\t¡Hora punta de tráfico! Retrocede tres casillas.", 4));
        barajaSuerte.add(new Suerte("\tTe multan por usar el móvil mientras conduces. Paga 150.000€.", 5));
        barajaSuerte.add(new Suerte("\tAvanza hasta la casilla de transporte más cercana. Si no tiene dueño, puedes comprarla...", 6));

        barajaComunidad.add(new CajaComunidad("\tPaga 500.000€ por un fin de semana en un balneario de 5 estrellas.", 0));
        barajaComunidad.add(new CajaComunidad("\tTe investigan por fraude de identidad. Ve a la Cárcel. Ve directamente...", 1));
        barajaComunidad.add(new CajaComunidad("\tColócate en la casilla de Salida. Cobra 2.000.000€.", 2));
        barajaComunidad.add(new CajaComunidad("\tDevolución de Hacienda. Cobra 500.000€.", 3));
        barajaComunidad.add(new CajaComunidad("\tRetrocede hasta Solar1 para comprar antigüedades exóticas.", 4));
        barajaComunidad.add(new CajaComunidad("\tVe a Solar20 para disfrutar del San Fermín. Si pasas por la casilla de Salida, cobra 2.000.000€.", 5));
    }


    public void sacarCartaSuerte() {
        if (barajaSuerte.isEmpty()) return;

        // Obtener el jugador actual
        Jugador jugadorActual = jugadores.get(turno);

        // Coger la carta que toca (usando el índice)
        Carta carta = barajaSuerte.get(indiceSuerte);
        
        // Ejecutar la acción 
        carta.accion(jugadorActual);

        //Actualizar el índice para la próxima vez 
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

    private void ejecutarComando(String comando) {
        // Dividir el comando en palabras
        String[] partes = comando.trim().split("\\s+");
        if (partes.length == 0 || comando.trim().isEmpty()) return;

        // Extraer la acción principal (primera palabra)
        String accion = partes[0].toLowerCase();

        consola.imprimir("{"); 

        try {
            switch (accion) {
                case "crear":
                    if (partes.length == 4 && partes[1].equals("jugador")) {
                        // crear jugador <nombre> <avatar>
                        crearJugador(partes[2], partes[3]);
                    } else {
                        consola.imprimir("\t*** Formato incorrecto. ***\n\tUso: crear jugador <Nombre> <Tipo avatar>");
                    }
                    break;

                case "lanzar":
                    if (partes.length >= 2 && partes[1].equals("dados")) {
                        if (partes.length == 2) {
                            // lanzar dados (aleatorio)
                            lanzarDados(); 
                        } else if (partes.length == 3) {
                            String[] dados = partes[2].split("\\+");
                            if (dados.length == 2) {
                                int d1 = Integer.parseInt(dados[0]);
                                int d2 = Integer.parseInt(dados[1]);
                                lanzarDados(d1, d2, true); // Llama a la versión privada forzada
                            } else {
                                consola.imprimir("\t*** Formato incorrecto. ***\n\tUso: lanzar dados <d1>+<d2>");
                            }
                        }
                    } else {
                        consola.imprimir("\t*** Comando desconocido. Uso: lanzar dados");
                    }
                    break;

                case "acabar":
                    if (partes.length == 2 && partes[1].equals("turno")) {
                        acabarTurno();
                    } else {
                        consola.imprimir("\t*** Comando desconocido. Uso: acabar turno");
                    }
                    break;

                case "ver":
                    if (partes.length == 2 && partes[1].equals("tablero")) {
                        verTablero();
                    }
                    break;

                case "comprar":
                    if (partes.length == 2) {
                        comprar(partes[1]);
                    } else {
                        consola.imprimir("\t*** Formato incorrecto. ***\n\tUso: comprar <Nombre casilla>");
                    }
                    break;

                case "edificar":
                    if (partes.length == 2) {
                        edificar(partes[1]);
                    } else {
                        consola.imprimir("\t*** Formato incorrecto. ***\n\tUso: edificar <tipo edificio>");
                    }
                    break;

                case "vender":
                    if (partes.length == 4) {
                        try {
                            int cantidad = Integer.parseInt(partes[3]);
                            vender(partes[1], partes[2], cantidad);
                        } catch (NumberFormatException e) {
                            consola.imprimir("\t*** La cantidad debe ser un número entero. ***");
                        }
                    } else {
                        consola.imprimir("\t*** Formato incorrecto. ***\n\tUso: vender <tipo> <casilla> <cantidad>");
                    }
                    break;

                case "hipotecar":
                    if (partes.length == 2) {
                        hipotecar(partes[1]);
                    } else {
                        consola.imprimir("\t*** Formato incorrecto. ***\n\tUso: hipotecar <nombre casilla>");
                    }
                    break;

                case "deshipotecar":
                    if (partes.length == 2) {
                        deshipotecar(partes[1]);
                    } else {
                        consola.imprimir("\t*** Formato incorrecto. ***\n\tUso: deshipotecar <nombre casilla>");
                    }
                    break;

                case "salir":
                    if (partes.length == 2 && partes[1].equals("carcel")) {
                        salirCarcel();
                    } else if (partes.length == 1) {
                        salir();
                    } else {
                        consola.imprimir("\t*** Comando desconocido. ***");
                    }
                    break;

                case "jugador":
                    if (jugadores.isEmpty()) {
                        consola.imprimir("\t*** No hay jugadores registrados. ***");
                    } else {
                        descJugador(jugadores.get(turno).getNombre());
                    }
                    break;

                case "describir":
                    if (partes.length == 3 && partes[1].equals("jugador")) {
                        descJugador(partes[2]);
                    } 
                    else if (partes.length == 2) {
                        String objetivo = partes[1];
                        if (objetivo.equals("casilla")) {
                            if (!jugadores.isEmpty()) {
                                String nombreCasillaActual = jugadores.get(turno).getAvatar().getCasilla().getNombre();
                                descCasilla(nombreCasillaActual);
                            } else {
                                consola.imprimir("\t*** No hay jugadores para determinar la casilla actual. ***");
                            }
                        } else {
                            descCasilla(objetivo);
                        }
                    } else {
                        consola.imprimir("\t*** Uso: describir <jugador X | casilla | nombre casilla> ***");
                    }
                    break;

                case "listar":
                    if (partes.length >= 2) {
                        switch (partes[1]) {
                            case "enventa":
                            case "venta": // Por si acaso
                                if (partes.length == 3) listarVenta(partes[2]); // Versión con grupo (string)
                                else listarVenta();
                                break;
                            case "jugadores":
                                listarJugadores();
                                break;
                            case "edificios":
                                if (partes.length == 3) listarEdificios(partes[2]); // Versión con grupo
                                else listarEdificios();
                                break;
                            case "avatares":
                                listarAvatares();
                                break;
                            case "tratos":
                                listarTratos();
                                break;
                            default:
                                consola.imprimir("\t*** Opción de listar no reconocida. ***");
                        }
                    } else {
                        consola.imprimir("\t*** Uso: listar <enventa | jugadores | edificios> ***");
                    }
                    break;

                case "estadisticas":
                    if (partes.length == 1) {
                        mostrarEstadisticas(null); // Globales
                    } else {
                        mostrarEstadisticas(partes[1]); // De un jugador
                    }
                    break;

                case "trato":
                    if(partes[2].equals("cambiar") && partes.length == 5 || partes.length == 6) {
                       try{
                            proponerTrato(comando);
                        }catch (Exception error) {
                            consola.imprimir(error.getMessage());
                        }
                    }
                    else{
                        consola.imprimir("\t*** Uso: trato <nombre del jugador que recibe el trato>  cambiar (<trato>) ***");
                    }
                    break;
                case "aceptar":
                    if(partes.length == 2) {
                        try{
                            String idTratoString = partes[1].substring(5);
                            int idTrato = Integer.parseInt(idTratoString);
                            aceptarTrato(idTrato);
                        }catch (Exception error) {
                            consola.imprimir(error.getMessage());
                        }
                    }
                    else{
                        consola.imprimir("\t*** Uso: aceptar <id trato> ***");
                    }
                    break;
                case "eliminar": 
                    if(partes.length == 2) {
                        try{
                            String idTratoString = partes[1].substring(5);
                            int idTrato = Integer.parseInt(idTratoString);
                            eliminarTrato(idTrato);
                        }catch (Exception error) {
                            consola.imprimir(error.getMessage());
                        }
                    }
                    else{
                        consola.imprimir("\t*** Uso: eliminar <id trato> ***");
                    }
                    break;
                

                case "help":
                case "ayuda":
                    ayuda();
                    break;

                default:
                    consola.imprimir("\t*** Comando no registrado. ***\n\tIntroduce 'help' para ver los comandos disponibles.");
            }

        } catch (Exception e) {
            consola.imprimir("\t*** Error procesando el comando: " + e.getMessage() + " ***");
        }

        consola.imprimir("}\n");
    }

    @Override
    public void crearJugador(String nombre, String tipoAvatar) {
        if (jugadores.size() >= 6) { 
            consola.imprimir("Máximo de jugadores alcanzado.\n");
            return;
        }
        // Validar nombre duplicado
        for(Jugador j : jugadores) { 
             if(j.getNombre().equalsIgnoreCase(nombre)) {
                 consola.imprimir("El nombre ya está en uso.\n");
                 return;
             }
        }

        tipoAvatar = tipoAvatar.toLowerCase();
        // Crear jugador, asignarle casilla de salida y lista de avatares
        Jugador jugador = new Jugador(nombre, tipoAvatar, tablero.encontrar_casilla("Salida"), avatares);
        
        this.jugadores.add(jugador);
        this.avatares.add(jugador.getAvatar());
        
        consola.imprimir("Jugador " + nombre + " creado con éxito.\n");
        descJugador(nombre);
    }

    @Override
    public void hipotecar(String nombreCasilla) {
        Casilla casilla = tablero.encontrar_casilla(nombreCasilla);
        
        if (casilla == null) {
            consola.imprimir("\t*** La casilla '" + nombreCasilla + "' no existe. ***");
            return;
        }

        if (casilla instanceof Solar) {
             try {
                // Hacemos casting a Solar
                ((Solar) casilla).hipotecar(jugadores.get(turno)); 
             } catch (Exception e) {
                 consola.imprimir(e.getMessage());
             }
        } else {
            consola.imprimir("\t*** Solo se pueden hipotecar casillas de tipo Solar. ***\n");
        }
    }

    @Override
    public void deshipotecar(String nombreCasilla) {
        Casilla casilla = tablero.encontrar_casilla(nombreCasilla);
        
        if (casilla == null) {
            consola.imprimir("\t*** La casilla '" + nombreCasilla + "' no existe. ***");
            return;
        }


        if (casilla instanceof Solar) {
             try {
                // Hacemos casting a Solar
                ((Solar) casilla).deshipotecar(jugadores.get(turno));
             } catch (Exception e) {
                 consola.imprimir(e.getMessage());
             }
        } else {
            consola.imprimir("\t*** Solo se pueden deshipotecar casillas de tipo Solar. ***\n");
        }
    }
    @Override
    public void verTablero() {
        consola.imprimir(tablero.toString() + "\n");
    }

    @Override
    public void ayuda(){
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


    @Override
    public void mostrarEstadisticas(String nombreJugador) {
        if (nombreJugador == null) {
            tablero.estadisticas();
        } else {
            // Estadísticas de un jugador
             for(Jugador j : jugadores) {
                if(j.getNombre().equalsIgnoreCase(nombreJugador)) {
                    j.mostrarEstadisticas();
                    return;
                }
            }
            consola.imprimir("Jugador no encontrado.\n");
        }
    }

    @Override
    public void salir() {
        consola.imprimir("Fin de la partida.\n");
        System.exit(0);
    }

    @Override
    public void edificar(String tipoEdificio) {
        if (jugadores.isEmpty()) return;
        
        Jugador jugadorActual = jugadores.get(turno);
        Casilla casillaActual = jugadorActual.getAvatar().getCasilla();


        if (casillaActual instanceof Solar) {
            ((Solar) casillaActual).edificar(tipoEdificio);
        } else {
            consola.imprimir("No puedes edificar aquí. No es un solar.\n");
        }
    }

    @Override
    public void vender(String tipoEdificio, String nombreCasilla, int cantidad) {
        Casilla casilla = tablero.encontrar_casilla(nombreCasilla);
        
        if (casilla instanceof Solar) {

            ((Solar) casilla).venderEdificio(tipoEdificio, cantidad);
        } else {
            consola.imprimir("Solo se pueden vender edificios de un solar.\n");
        }
    }

@Override
    public void descJugador(String nombre) {
        // Buscar al jugador en la lista
        Jugador jugador = null;
        for (Jugador j : jugadores) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                jugador = j;
                break;
            }
        }

        // Si no existe, error y salir
        if (jugador == null) {
            consola.imprimir("\t*** El jugador " + nombre + " no existe. ***");
            return;
        }

        // Obtener datos básicos
        String nombreJug = jugador.getNombre();
        String avatarId = jugador.getAvatar().getId();
        String fortuna = String.format("%.0f", jugador.getFortuna()); // Sin decimales feos

        //Recopilar nombres de Propiedades
        ArrayList<String> nombresPropiedades = new ArrayList<>();
        ArrayList<String> nombresEdificios = new ArrayList<>();

        for (Propiedad p : jugador.getPropiedades()) {
            nombresPropiedades.add(p.getNombre());

            // Solo los Solares tienen edificios, así que comprobamos antes
            if (p instanceof Solar) {
                Solar s = (Solar) p;
                if (s.getEdificios() != null && !s.getEdificios().isEmpty()) {
                    for (Edificio ed : s.getEdificios()) {
                        nombresEdificios.add(ed.getNombre());
                    }
                }
            }
        }

        // Imprimir resultado
        consola.imprimir("\tNombre: " + nombreJug);
        consola.imprimir("\tAvatar: " + avatarId);
        consola.imprimir("\tFortuna: " + fortuna);
        consola.imprimir("\tPropiedades: " + nombresPropiedades);
        consola.imprimir("\tEdificios: " + nombresEdificios);
        
    }

    /*Metodo que realiza las acciones asociadas al comando 'describir avatar'.
     * Parámetro: id del avatar a describir.
     */
    private void descAvatar(String ID) {
    }

    /* Metodo que realiza las acciones asociadas al comando 'describir nombre_casilla'.
     * Parámetros: nombre de la casilla a describir.
     */
    @Override
    public void descCasilla(String nombre) {
        //Buscar la casilla en el tablero
        Casilla casilla = tablero.encontrar_casilla(nombre);

        //Si no existe, error y salir
        if (casilla == null) {
            consola.imprimir("\t*** La casilla '" + nombre + "' no existe. ***");
            return;
        }

        //Imprimir la información
        consola.imprimir(casilla.toString());
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
        if (jugadorActual.isEnCarcel()) {
            consola.imprimir("\t*** " + jugadorActual.getNombre() + " está en la cárcel. ***");

            consola.imprimir("\tDebe usar el comando 'salir carcel' para intentar salir (pagando o sacando dobles).");
            return; // Finaliza el turno
        }

        int dado1Val;
        int dado2Val;
        boolean volverATirar = true;

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
                tirado=true;
                return;
            } else {
                consola.imprimir("\t¡Dobles! " + avatarActual.getJugador().getNombre() + " vuelve a tirar.");
                tirado = false;
            }
        } else {
            jugadorActual.setDoblesConsecutivos(0); // Reiniciar si no es doble
            tirado = true;
        }
        // Mover avatar
        avatarActual.moverAvatar(valorTirada);

        if (forzado) return;
        if (dado1Val != dado2Val) volverATirar = false; // No seguir tirando si no es doble
        else {
            try {
                Thread.sleep(1000); // pausa de 1 segundos
            } catch (InterruptedException e) {
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
            tirado = true;
            return; // Terminar turno
        } 
        else if(destino instanceof CasillaCom){
            sacarCartaComunidad();
        }
        else if(destino instanceof CasillaSuerte){
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

        //tirado = true; // marcar que el jugador ya tiró
    }
    @Override
    public void lanzarDados() throws DadosException {
        lanzarDados(0, 0, false);
    }

    /*Metodo que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
     * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    @Override
    public void comprar(String nombreCasilla) {
        if (jugadores.isEmpty()) return;
        
        Casilla casilla = tablero.encontrar_casilla(nombreCasilla);
        if (casilla == null) {
            consola.imprimir("La casilla " + nombreCasilla + " no existe.\n");
            return;
        }

        if (casilla instanceof Propiedad) {
            try {
                ((Propiedad) casilla).comprar(jugadores.get(turno));
            } catch (Exception e) {
                consola.imprimir(e.getMessage() + "\n");
            }
        } else {
            consola.imprimir("Esta casilla no se puede comprar.\n");
        }
    }

    //Metodo que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
    /*CAMBIOS POR HACER:
        * - En la tercera tirada si no sacas dobles te tiene que obligar a pagar y luego salir de la carcel con esa tirada.
     */
    @Override
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
            tirado = true;
        }
    }

    // Metodo que realiza las acciones asociadas al comando 'listar enventa'.
    @Override
    public void listarVenta() {
        ArrayList<Propiedad> sinDuenho = banca.getPropiedades();
        
        for (Propiedad p : sinDuenho) {
            if (p instanceof Solar || p instanceof Transporte || p instanceof Servicio) {
                descCasilla(p.getNombre());
                consola.imprimir("},\n{");
            }
        }
    }

    private void listarVenta(String nombreGrupo) {
        ArrayList<Propiedad> sinDuenho = banca.getPropiedades();
        
        for (Propiedad p : sinDuenho) {
            // Ya sabemos que son propiedades, filtramos las comprables
            if (p instanceof Solar || p instanceof Transporte || p instanceof Servicio) {
                descCasilla(p.getNombre());
                consola.imprimir("},\n{");
            }
        }
    }

    // Metodo que realiza las acciones asociadas al comando 'listar jugadores'.
    @Override
    public void listarJugadores() {
        if (jugadores == null || jugadores.isEmpty()) {
            consola.imprimir("\t*** Ningún jugador registrado. ***");
            return;
        }


        consola.imprimir("{"); 

        for (Jugador jugador : jugadores) {
            String nombre = jugador.getNombre();
            
            descJugador(nombre); 

            consola.imprimir("},\n{");
        }
    }

    // Metodo que realiza las acciones asociadas al comando 'listar avatares'.
    private void listarAvatares() {
    }

    @Override
    public void listarEdificios() {
        ArrayList<Casa> casas = tablero.getCasas();
        ArrayList<Hotel> hoteles = tablero.getHoteles();
        ArrayList<Piscina> piscinas = tablero.getPiscinas();
        ArrayList<PistaDeporte> pistas = tablero.getPistas();

        // Bloque para Casas
        for (Edificio ed : casas) {
            imprimirInfoEdificio(ed);
        }
        // Bloque para Hoteles
        for (Edificio ed : hoteles) {
            imprimirInfoEdificio(ed);
        }
        // Bloque para Piscinas
        for (Edificio ed : piscinas) {
            imprimirInfoEdificio(ed);
        }
        // Bloque para Pistas
        for (Edificio ed : pistas) {
            imprimirInfoEdificio(ed);
        }
    }

    private void imprimirInfoEdificio(Edificio ed) {
        consola.imprimir("\tid: " + ed.getNombre());
        consola.imprimir("\tpropietario: " + ed.getSolar().getDuenho().getNombre());
        consola.imprimir("\tcasilla:  " + ed.getSolar().getNombre());
        consola.imprimir("\tgrupo: " + ed.getSolar().getGrupo().getCodigoColor() + ed.getSolar().getGrupo().getColorGrupo() + Valor.RESET);
        consola.imprimir("\tcoste: " + ed.getSolar().getValor());
        consola.imprimir("}\n{");
    }

    // Versión sobrecargada para listar por grupo
    private void listarEdificios(String nombreGrupo) {
        // Buscar el grupo en el tablero
        Grupo g = tablero.getGrupos().get(nombreGrupo);
        
        if (g == null) {
            consola.imprimir("\t*** El grupo '" + nombreGrupo + "' no existe. ***");
            return;
        }

        if (jugadores == null || jugadores.isEmpty()) { 
            consola.imprimir("\t*** No hay jugadores. ***");
            return;
        }

        // Comprobar si el jugador actual es dueño del grupo
        if (!g.esDuenhoGrupo(jugadores.get(turno))) {
            consola.imprimir("\t*** El jugador actual no posee el grupo completo. ***");
        }

        ArrayList<String> casas = new ArrayList<>();
        ArrayList<String> hoteles = new ArrayList<>();
        ArrayList<String> piscinas = new ArrayList<>();
        ArrayList<String> pistas = new ArrayList<>();
        
        ArrayList<Propiedad> propiedades = g.getMiembros();

        int numCasas = 0;
        int numHoteles = 0;
        int numPiscinas = 0;
        int numPistas = 0;
        int hotelesDisponibles = 0;

        for (Propiedad p : propiedades) {
            // Solo los Solares tienen edificios
            if (p instanceof Solar) {
                Solar s = (Solar) p;
                
                for (Edificio ed : s.getEdificios()) {
                    switch (ed.getTipo()) {
                        case "casa": casas.add(ed.getNombre()); break;
                        case "hotel": hoteles.add(ed.getNombre()); break;
                        case "piscina": piscinas.add(ed.getNombre()); break;
                        case "pista": pistas.add(ed.getNombre()); break;
                    }
                }

                consola.imprimir("\tpropiedad: " + s.getNombre());
                consola.imprimir("\tcasas: " + casas);
                consola.imprimir("\thoteles: " + hoteles);
                consola.imprimir("\tpiscinas: " + piscinas);
                consola.imprimir("\tpistas de deporte: " + pistas);
                consola.imprimir("\talquiler actual: " + s.getImpuesto() + "\n");

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
        }

        // Información de construcción restante
        consola.imprimir("\tAún puedes construir en este grupo:");
        int totalSolares = propiedades.size();
        
        // Cálculos protegidos para evitar negativos visuales
        int casasRestantes = (4 * totalSolares - numCasas) - (4 * numHoteles);
        if (casasRestantes > 0) consola.imprimir("\t\t-> " + casasRestantes + " casas");
        
        int hotelesRestantes = totalSolares - numHoteles;
        if (hotelesRestantes > 0) consola.imprimir("\t\t-> " + hotelesRestantes + " hoteles (" + hotelesDisponibles + " solares listos)");
        
        int piscinasRestantes = totalSolares - numPiscinas;
        if (piscinasRestantes > 0) consola.imprimir("\t\t-> " + piscinasRestantes + " piscinas (necesitas hotel)");
        
        int pistasRestantes = totalSolares - numPistas;
        if (pistasRestantes > 0) consola.imprimir("\t\t-> " + pistasRestantes + " pistas (necesitas piscina)");
        
        consola.imprimir("}\n");
    }

    // Metodo que realiza las acciones asociadas al comando 'acabar turno'.
    @Override
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

    // Método simple para verificar si un String es un número
    private boolean esNumero(String str) {
        if (str == null) return false;
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    @Override
    public void proponerTrato(String comando) throws TratosException {

        Jugador jugadorPropone = jugadores.get(turno);
        Jugador jugadorRecibe = null;
        Propiedad propiedadPropone = null;
        Propiedad propiedadRecibe = null;
        float dineroPropone = 0.0f;
        float dineroRecibe = 0.0f;

        String[] primeraParte = comando.split(":");
        String cabecera = primeraParte[0].trim(); // "trato Maria"
        String nombreJugadorRecibe = cabecera.substring(6).trim(); // Quitamos "trato " (6 chars)

        for (Jugador j : jugadores) {
            if (j.getNombre().equalsIgnoreCase(nombreJugadorRecibe)) {
                if (j == jugadorPropone) {
                    throw new TratosException("No puedes proponerte un trato a ti mismo.");
                }
                else {
                    jugadorRecibe= j;
                }
            }
        }

        int inicioParentesis = comando.indexOf('(');
        int finParentesis = comando.lastIndexOf(')');

        if (inicioParentesis == -1 || finParentesis == -1) {
                System.out.println("Error: Formato incorrecto (faltan paréntesis).");
                return;
            }

        String contenido = comando.substring(inicioParentesis + 1, finParentesis);
        // contenido ahora es ej: "Solar12 y 100000, Solar3"

        // 3. Dividir en dos partes usando la coma (,)
        // parte[0] = Lo que ofrece el jugador que propone (Jugador 1)
        // parte[1] = Lo que pide a cambio (Jugador 2)
        String[] lados = contenido.split(",");

        if (lados.length != 2) {
            System.out.println("Error: El trato debe tener dos partes separadas por coma.");
            return;
        }

        String lado1 = lados[0].trim();

        // Dividimos por " y " (con espacios para evitar romper nombres que contengan 'y')
        // "\\s+y\\s+" busca la letra 'y' rodeada de espacios.
        String[] elementos = lado1.split("\\s+y\\s+");

        for (String elemento : elementos) {
            elemento = elemento.trim();
            
            // Intentamos ver si es dinero
            if (esNumero(elemento)) {
                dineroPropone = Float.parseFloat(elemento);
                if (jugadorPropone.getFortuna() < dineroPropone)
                    throw new TratosException(jugadorPropone.getNombre() + " no tiene suficiente dinero para realizar el trato.");

            } else {
                // Si no es número, asumimos que es una propiedad
                Casilla casilla = tablero.encontrar_casilla(elemento);

                if (casilla == null)
                    throw new TratosException("La casilla '" + elemento + "' no existe.");
                if (!(casilla instanceof Propiedad))
                    throw new TratosException("La casilla '" + casilla.getNombre() + "' no es una propiedad.");

                //Comprobamos que el jugador son dueños de las propiedades
                if(jugadorPropone.getPropiedades().contains(propiedadPropone)) 
                    throw new TratosException(jugadorPropone.getNombre() + " no es dueño de la propiedad '" + propiedadPropone + "'."); 

                propiedadPropone = (Propiedad) casilla;
            }
        }


        //Segunda mitad para jugador Recibe

        String lado2 = lados[1].trim();

        // Dividimos por " y " (con espacios para evitar romper nombres que contengan 'y')
        // "\\s+y\\s+" busca la letra 'y' rodeada de espacios.
        String[] elementos2 = lado2.split("\\s+y\\s+");

         for (String elemento : elementos2) {
            elemento = elemento.trim();
            
            // Intentamos ver si es dinero
            if (esNumero(elemento)) {
                dineroRecibe = Float.parseFloat(elemento);
                //No se comprueba el dinero que tiene el que recibe el trato ya que puede hipotecar o vender edificios en su turno

            } else {
                // Si no es número, asumimos que es una propiedad
                Casilla casilla = tablero.encontrar_casilla(elemento);

                if (casilla == null)
                    throw new TratosException("La casilla '" + propiedadRecibe + "' no existe.");
                if (!(casilla instanceof Propiedad))
                    throw new TratosException("La casilla '" + propiedadRecibe + "' no es una propiedad.");

                //Comprobamos que el jugador son dueños de las propiedades
                if(jugadorRecibe.getPropiedades().contains(propiedadRecibe)) 
                    throw new TratosException(jugadorRecibe.getNombre() + " no es dueño de la propiedad '" + propiedadRecibe + "'."); 

                propiedadRecibe = (Propiedad) casilla;
            }

        }

        Trato trato = new Trato(jugadorPropone, jugadorRecibe, propiedadPropone, propiedadRecibe, dineroPropone, dineroRecibe);
        
        if(this.tratos == null) {
            ArrayList<Trato> tratos = new ArrayList<>();
        }

        this.tratos.add(trato);
    }

    private void imprimirTrato(Trato trato, int idTrato) {


        consola.imprimir("{\n\tid: trato" + idTrato);
        consola.imprimir("\tjugadorPropone: " + trato.jugadorPropone.getNombre());
        String mensaje = "\ttrato: cambiar (";

        

        //LO QUE SE PROPONE 
        if (trato.getPropiedadPropone() != null && trato.getDineroPropone() != 0) {
            mensaje = mensaje + trato.getPropiedadPropone().getNombre() + " y " + trato.getDineroPropone();
            
        }
        else if(trato.getPropiedadPropone() != null){
            mensaje = mensaje + trato.getPropiedadPropone().getNombre();
            
        }
        else if(trato.getDineroPropone() != 0) {
            mensaje = mensaje + trato.getDineroPropone();
        }

        mensaje = mensaje + ", ";

        //LO QUE SE RECIBE
        if(trato.getPropiedadRecibe() != null && trato.getDineroRecibe() != 0) {
            mensaje = mensaje + trato.getPropiedadRecibe().getNombre() + " y " + trato.getDineroRecibe();
        }
        else if(trato.getPropiedadRecibe() != null){
            mensaje = mensaje + trato.getPropiedadRecibe().getNombre();
        }
        else if(trato.getDineroRecibe() != 0) {
            mensaje = mensaje + trato.getDineroRecibe() + "";        
        }

        consola.imprimir(mensaje + ")\n}");
    }

    public void listarTratos() {

        Jugador jugador = jugadores.get(turno);
        String nombreJugador = jugador.getNombre();
        int bandera = 0; //Variable bandera usamos para controlar el jugador tiene por lo menos un trato
        int idTrato = 1; //Se pasa a imprimirTrato para que pueda obtener el id.

        

        for (Trato t : tratos) {
            if(t.getJugadorRecibe().getNombre().equals(nombreJugador)){
                imprimirTrato(t, idTrato);
                bandera = 1;
            }

            idTrato++;
        }

        if(bandera == 0) {
            consola.imprimir("\n" + jugador.getNombre() + " no ha recibido propuestas de tratos.\n");
        }
    }

    public void eliminarTrato(int idTrato) throws TratosException {
        if(idTrato < 1 || idTrato > tratos.size()) {
            consola.imprimir("El trato con id " + idTrato + " no existe.");
            return;
        }

        Trato trato = tratos.get((idTrato - 1));

        if(!(trato.getJugadorPropone().getNombre().equals(jugadores.get(turno).getNombre()))) {
            throw new TratosException("El trato" + idTrato + " no fue propuesto por " + jugadores.get(turno).getNombre() + ".");
        }

        tratos.remove(idTrato - 1);
    }

    public void aceptarTrato(int idTrato) throws TratosException {

        Jugador jugadorActual = jugadores.get(turno);

        if(idTrato < 1 || idTrato > tratos.size()) {
            throw new TratosException("El trato con id " + idTrato + " no existe.");
        }

        Trato trato = tratos.get(idTrato - 1);

        if(!trato.getJugadorRecibe().getNombre().equals(jugadorActual.getNombre())) {
            throw new TratosException("El trato con id " + idTrato + " no está dirigido a " + jugadorActual.getNombre() + ".");
        }
       
        //Propiedades
        if(trato.getPropiedadPropone() != null) {
            trato.getPropiedadPropone().setDuenho(trato.getJugadorRecibe());
            trato.getJugadorRecibe().añadirPropiedad(trato.getPropiedadPropone());
            trato.getJugadorPropone().eliminarPropiedad(trato.getPropiedadPropone());
        }
        if(trato.getPropiedadRecibe() != null) {
            trato.getPropiedadRecibe().setDuenho(trato.getJugadorPropone());
            trato.getJugadorPropone().añadirPropiedad(trato.getPropiedadRecibe());
            trato.getJugadorRecibe().eliminarPropiedad(trato.getPropiedadRecibe());
        }

        //Calculamos el dinero que gana el jugador que recibe el trato y su negativo es el del que pierde
        float costeAceptarTrato = trato.getDineroPropone() - trato.getDineroRecibe();

        if(jugadorActual.getFortuna() < costeAceptarTrato) {
            throw new TratosException(jugadorActual.getNombre() + " no tiene suficiente dinero para aceptar el trato.");
        }
        jugadorActual.sumarFortuna(-costeAceptarTrato);
        trato.getJugadorPropone().sumarFortuna(costeAceptarTrato);
        
    
        //Eliminar el trato de la lista de tratos
        tratos.remove(idTrato - 1);

    }

}