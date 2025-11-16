package monopoly;

import partida.*;

import static monopoly.MonopolyETSE.tablero;

import java.util.*;


public class Tablero {
    //Atributos.
    private ArrayList<ArrayList<Casilla>> posiciones; //Posiciones del tablero: se define como un arraylist de arraylists de casillas (uno por cada lado del tablero).
    private ArrayList<Edificio> casas;
    private ArrayList<Edificio> hoteles;
    private ArrayList<Edificio> piscinas;
    private ArrayList<Edificio> pistas;
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    private Jugador banca; //Un jugador que será la banca.


    public ArrayList<Edificio> getCasas() {
        if (casas == null) casas = new ArrayList<>();
        return casas;
    }

    public ArrayList<Edificio> getHoteles() {
        if (hoteles == null) hoteles = new ArrayList<>();
        return hoteles;
    }

    public ArrayList<Edificio> getPiscinas() {
        if (piscinas == null) piscinas = new ArrayList<>();
        return piscinas;
    }

    public ArrayList<Edificio> getPistas() {
        if (pistas == null) pistas = new ArrayList<>();
        return pistas;
    }

    public void setGrupos(String color, Grupo grupo) {
        this.grupos.put(color, grupo);
    }
    public Jugador getBanca() {
        return this.banca;
    }
    public ArrayList<ArrayList<Casilla>> getPosiciones() {
        return this.posiciones;
    }
    public HashMap<String, Grupo> getGrupos() {
        return grupos;
    }



    public Tablero() {}

    //Constructor: únicamente le pasamos el jugador banca (que se creará desde el menú).
    public Tablero(Jugador banca) {
        this.banca = banca;

        MonopolyETSE.menu.setTablero(this);
    }


    //Método para crear todas las casillas del tablero. Formado a su vez por cuatro métodos (1/lado).
    private void generarCasillas() {
        posiciones = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            posiciones.add(new ArrayList<>());
        }

        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();

        generarGrupos();
    }
    private void generarGrupos() {
        if (posiciones == null) generarCasillas();
        if (grupos == null) grupos = new HashMap<>();

        Grupo negro =  new Grupo(encontrar_casilla((String) Solares.getFirst().getFirst()),
                encontrar_casilla((String) Solares.get(1).getFirst()),
                "negro",
                Valor.GREY);
        setGrupos("negro", negro);
        Grupo cian =  new Grupo(encontrar_casilla((String) Solares.get(2).getFirst()),
                encontrar_casilla((String) Solares.get(3).getFirst()),
                encontrar_casilla((String) Solares.get(4).getFirst()),
                "cian",
                Valor.CYAN);
        setGrupos("cian", cian);
        Grupo morado =  new Grupo(encontrar_casilla((String) Solares.get(5).getFirst()),
                encontrar_casilla((String) Solares.get(6).getFirst()),
                encontrar_casilla((String) Solares.get(7).getFirst()),
                "morado",
                Valor.PURPLE);
        setGrupos("morado", morado);
        Grupo amarillo =  new Grupo(encontrar_casilla((String) Solares.get(8).getFirst()),
                encontrar_casilla((String) Solares.get(9).getFirst()),
                encontrar_casilla((String) Solares.get(10).getFirst()),
                "amarillo",
                Valor.YELLOW);
        setGrupos("amarillo", amarillo);
        Grupo rojo =  new Grupo(encontrar_casilla((String) Solares.get(11).getFirst()),
                encontrar_casilla((String) Solares.get(12).getFirst()),
                encontrar_casilla((String) Solares.get(13).getFirst()),
                "rojo",
                Valor.RED);
        setGrupos("rojo", rojo);
        Grupo blanco =  new Grupo(encontrar_casilla((String) Solares.get(14).getFirst()),
                encontrar_casilla((String) Solares.get(15).getFirst()),
                encontrar_casilla((String) Solares.get(16).getFirst()),
                "blanco",
                Valor.WHITE);
        setGrupos("blanco", blanco);
        Grupo verde =  new Grupo(encontrar_casilla((String) Solares.get(17).getFirst()),
                encontrar_casilla((String) Solares.get(18).getFirst()),
                encontrar_casilla((String) Solares.get(19).getFirst()),
                "verde",
                Valor.GREEN);
        setGrupos("verde", verde);
        Grupo azul =  new Grupo(encontrar_casilla((String) Solares.get(20).getFirst()),
                encontrar_casilla((String) Solares.get(21).getFirst()),
                "azul",
                Valor.BLUE);
        setGrupos("azul", azul);
    }
    
    //Método para insertar las casillas del lado norte.
    private void insertarLadoNorte() {
        ArrayList<Casilla> ladoNorte = new ArrayList<>();
        for (int i = 20; i < 31; i++) {
            ladoNorte.add(asignarCasilla(i));
        }
        posiciones.add(ladoNorte);
    }
    //Método para insertar las casillas del lado sur.
    private void insertarLadoSur() {
        ArrayList<Casilla> ladoSur = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            ladoSur.add(asignarCasilla(i));
        }
        posiciones.add(ladoSur);
    }
    //Método que inserta casillas del lado oeste.
    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = new ArrayList<>();
        for (int i = 11; i < 20; i++) {
            ladoOeste.add(asignarCasilla(i));
        }
        posiciones.add(ladoOeste);
    }
    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = new ArrayList<>();
        for (int i = 31; i < 40; i++) {
            ladoEste.add(asignarCasilla(i));
        }
        posiciones.add(ladoEste);
    }

    //Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {
        if (posiciones == null) generarCasillas();

        // Dimensiones del tablero
        int filas = 11;  // Total de filas necesarias
        int columnas = 11; // Total de columnas necesarias
        int lenCasilla = 40;

        byte iSur = 4;
        byte iOeste = 5;
        byte iNorte = 6;
        byte iEste = 7;

        ////////////////// Crear matriz para organizar el tablero
        String[][] matrizTablero = new String[filas][columnas];
        String vacio = " ".repeat(lenCasilla);

        // Inicializar con espacios vacíos
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matrizTablero[i][j] = vacio; // 12 espacios para cada celda
            }
        }

        // Llenar lado NORTE (fila 0)
        ArrayList<Casilla> norte = posiciones.get(iNorte);
        for (int j = 0; j < posiciones.get(iNorte).size(); j++) {
            ArrayList<Avatar> avatares = norte.get(j).getAvatares();
            String contenidoTexto = "";
            String nombre = norte.get(j).getNombre();

            if (norte.get(j).getTipo().equals("Solar")) {
                String color = norte.get(j).getGrupo().getCodigoColor();
                nombre = color + nombre + Valor.RESET;
            }
            contenidoTexto = nombre;
            if (!avatares.isEmpty()) contenidoTexto = nombre + ';' + avataresString(avatares);

            matrizTablero[0][j] = contenidoTexto;
        }

        // Llenar lado SUR (fila 10)
        ArrayList<Casilla> sur = posiciones.get(iSur);
        int tam = posiciones.get(iSur).size();
        for (int j = tam - 1; j > -1; j--) {
            ArrayList<Avatar> avatares = sur.get(j).getAvatares();
            String contenidoTexto = "";
            String nombre = sur.get(j).getNombre();

            if (sur.get(j).getTipo().equals("Solar")) {
                String color = sur.get(j).getGrupo().getCodigoColor();
                nombre = color + nombre + Valor.RESET;
            }
            contenidoTexto = nombre;
            if (!avatares.isEmpty()) contenidoTexto = nombre + ';' + avataresString(avatares);

            matrizTablero[10][tam - j - 1] = contenidoTexto;
        }

        // Llenar lado OESTE (columna 0)
        ArrayList<Casilla> oeste = posiciones.get(iOeste);
        tam = posiciones.get(iOeste).size();
        for (int j = tam - 1; j > -1; j--) {
            ArrayList<Avatar> avatares = oeste.get(j).getAvatares();
            String  contenidoTexto = "";
            String nombre = oeste.get(j).getNombre();

            if (oeste.get(j).getTipo().equals("Solar")) {
                String color = oeste.get(j).getGrupo().getCodigoColor();
                nombre = color + nombre + Valor.RESET;
            }
            contenidoTexto = nombre;
            if (!avatares.isEmpty()) contenidoTexto = nombre + ';' + avataresString(avatares);

            matrizTablero[tam - j][0] = contenidoTexto;
        }

        // Llenar lado ESTE (columna 10)
        ArrayList<Casilla> este = posiciones.get(iEste);
        for (int j = 1; j < 10; j++) {
            ArrayList<Avatar> avatares = este.get(j-1).getAvatares();
            String contenidoTexto = "";
            String nombre = este.get(j-1).getNombre();

            if (este.get(j-1).getTipo().equals("Solar")) {
                String color = este.get(j-1).getGrupo().getCodigoColor();
                nombre = color + nombre + Valor.RESET;
            }
            contenidoTexto = nombre;
            if (!avatares.isEmpty()) contenidoTexto = nombre + ';' + avataresString(avatares);

            matrizTablero[j][10] = contenidoTexto;
        }


        ///////////////// Construir String tablero
        StringBuilder tablero = new StringBuilder();
        String bordeCasilla = "-".repeat((lenCasilla/2) + 1);
        String separacion = (" " + bordeCasilla).repeat(columnas);
        tablero.append(separacion).append("\n");

        for (int i = 0; i < filas; i++) {
            for (int k = 0; k < 2; k++) { // Líneas que ocupa cada casilla
                String linea = "";
                String lineaVacia = " ".repeat(lenCasilla/2);
                for (int j = 0; j < columnas; j++) {
                    String[] contenidoCasilla = matrizTablero[i][j].split(";"); // separamos el avatar

                    String calle = contenidoCasilla[0];

                    // Al dividir la cadena hay que volver a formatear el color
                    String color = "";
                    boolean solar = calle.contains("\u001B[");
                    if (solar) {
                        color = calle.substring(0, calle.indexOf('m') + 1);
                        calle = calle.substring(calle.indexOf('m') + 1, calle.length() - 4);
                    }
                    // añadimos la posicion al nombre
                    if (!calle.equals(vacio)) {
                        int posCasilla = encontrar_casilla(calle).getPosicion();
                        calle = "(" + posCasilla + ") " + calle;
                    }

                    // dividimos en dos líneas
                    String[] lineasDiv = splitByWords(calle, lenCasilla/2);

                    String avatar = "";
                    if (matrizTablero[i][j].contains(";")) {
                        int lenAv = contenidoCasilla[1].length();
                        avatar = "\033[1m" + contenidoCasilla[1] + "\033[22m";

                        // Calcula cuántos espacios poner entre base y los avatares
                        int margen = 1;
                        lineasDiv[1] = lineasDiv[1].trim(); // quita espacios sobrantes
                        int espacios = lenCasilla/2 - lineasDiv[1].length() - lenAv - margen;
                        if (espacios < 0) espacios = 0;  // evita números negativos

                        lineasDiv[1] = lineasDiv[1] + " ".repeat(espacios) + avatar + " ".repeat(margen);
                    }
                    // añadimos los colores con las líneas ya separadas
                    if (solar) {
                        lineasDiv[0] = color + lineasDiv[0] + Valor.RESET;
                        lineasDiv[1] = color + lineasDiv[1] + Valor.RESET;
                    }
                    // diferenciamos entre las dos líneas de una misma casilla
                    if (k == 0) linea = lineasDiv[0];
                    else linea =  lineasDiv[1];

                    if (!calle.equals(vacio)) {
                        if (j == 0) tablero.append("| ").append(linea).append("| ");
                        else if (!matrizTablero[i][j-1].equals(vacio)) tablero.append(linea).append("| ");
                        else tablero.append(" ".repeat(lenCasilla/2 - 4)).append("| ").append(linea).append("| ");
                    }
                    else tablero.append(lineaVacia);
                }
                if (k == 0) tablero.append("\n");
            }

            if (i == 0 || i == 9 || i == 10) tablero.append("\n").append(separacion).append("\n");
            else tablero.append("\n").append(" ").append(bordeCasilla).append(" ".repeat(198)).append(" ").append(bordeCasilla).append("\n");
        }

        return tablero.toString();
    }


    //Método para asignar el tipo de casilla
    private Casilla asignarCasilla(int pos) {
        if (SolServTrans.getFirst().contains(pos)) { // Solares
            String nombre =  Solares.get(SolServTrans.getFirst().indexOf(pos)).getFirst().toString();
            String tipo = "Solar";
            float valor = (int) Solares.get(SolServTrans.getFirst().indexOf(pos)).get(1);

            Casilla casilla = new Casilla(nombre, tipo, pos, valor, banca);
            float hipoteca = (int) Solares.get(SolServTrans.getFirst().indexOf(pos)).get(2);

            casilla.setHipoteca(hipoteca);
            asignarPrecioEdificios(casilla, pos);
            asignarAlquileres(casilla, pos);

            return casilla;
        }
        if (SolServTrans.get(1).contains(pos)) { // Servicios
            String nombre = "Serv" + (SolServTrans.get(1).indexOf(pos) + 1);
            String tipo = "Servicios";
            float valor = 500000;
            Casilla casilla = new Casilla(nombre, tipo, pos, valor, banca);
            casilla.setImpuesto(50000);
            return casilla;
        }
        if (SolServTrans.get(2).contains(pos)) { // Transporte
            String nombre = "Trans" + (SolServTrans.get(2).indexOf(pos) + 1);
            String tipo = "Transporte";
            float valor = 500000;
            Casilla casilla = new Casilla(nombre, tipo, pos, valor, banca);
            casilla.setImpuesto(250000);
            return casilla;
        }

        if (impuestos.contains(pos)) { // Impuestos
            String nombre = "Imp" + (impuestos.indexOf(pos) + 1);
            return new Casilla(2000000, nombre, "Impuestos", pos, banca);
        }

        if (SuCajEsp.getFirst().contains(pos)) { // Suerte
            return new Casilla("Suerte", "Suerte", pos, banca);
        }
        if (SuCajEsp.get(1).contains(pos)) { // Caja
            return new Casilla("Caja", "Comunidad", pos, banca);
        }
        if (SuCajEsp.get(2).contains(pos)) { // Especial
            int n = SuCajEsp.get(2).indexOf(pos);
            String nombre = switch (n) {
                case 0 -> "Salida";
                case 1 -> "Cárcel";
                case 2 -> "Parking";
                case 3 -> "IrCarcel";
                default -> "";
            };
            Casilla casilla = new Casilla(nombre, "Especial", pos, banca);
            if (n == 1) casilla.setImpuesto(500000);
            else if (n == 2) casilla.setValor(0);
            return casilla;
        }

        return new Casilla();
    }
    private void asignarPrecioEdificios(Casilla c, int pos) {
        float valorCasa =  (int) PreciosConstruccion.get(SolServTrans.getFirst().indexOf(pos)).get(1);
        float valorHotel =  (int) PreciosConstruccion.get(SolServTrans.getFirst().indexOf(pos)).get(2);
        float valorPiscina =   (int) PreciosConstruccion.get(SolServTrans.getFirst().indexOf(pos)).get(3);
        float valorPista = (int) PreciosConstruccion.get(SolServTrans.getFirst().indexOf(pos)).get(4);

        c.setValorCasa(valorCasa);
        c.setValorHotel(valorHotel);
        c.setValorPiscina(valorPiscina);
        c.setValorPista(valorPista);
    }
    private void asignarAlquileres(Casilla c, int pos) {
        float impuesto = (int) Alquileres.get(SolServTrans.getFirst().indexOf(pos)).get(1);
        float alquilerCasa =  (int) Alquileres.get(SolServTrans.getFirst().indexOf(pos)).get(2);
        float alquilerHotel =  (int) Alquileres.get(SolServTrans.getFirst().indexOf(pos)).get(3);
        float alquilerPiscina =   (int) Alquileres.get(SolServTrans.getFirst().indexOf(pos)).get(4);
        float alquilerPista = (int) Alquileres.get(SolServTrans.getFirst().indexOf(pos)).get(5);

        c.setImpuesto(impuesto);
        c.setAlquilerCasa(alquilerCasa);
        c.setAlquilerHotel(alquilerHotel);
        c.setAlquilerPiscina(alquilerPiscina);
        c.setAlquilerPista(alquilerPista);
    }


    private String avataresString(ArrayList<Avatar> avatares){
        StringBuilder avs = new StringBuilder();
        for (Avatar av : avatares){
            String ID = av.getId();
            avs.append("&").append(ID);
        }
        return avs.toString();
    }
    private static String[] splitByWords(String text, int width) {
        if (text == null) text = "";

        String[] words = text.split(" ");
        StringBuilder line1 = new StringBuilder();
        StringBuilder line2 = new StringBuilder();
        boolean firstLine = true;

        for (String word : words) {
            StringBuilder currentLine = firstLine ? line1 : line2;

            int spaceNeeded = (currentLine.isEmpty() ? 0 : 1) + word.length();

            if (currentLine.length() + spaceNeeded <= width) {
                // cabe en la línea actual
                if (!currentLine.isEmpty()) currentLine.append(" ");
                currentLine.append(word);
            } else if (firstLine) {
                // pasa a la segunda línea
                firstLine = false;
                currentLine = line2;
                if (!currentLine.isEmpty()) currentLine.append(" ");
                currentLine.append(word);
            } else {
                // segunda línea ya llena, recortar si sobra
                int remaining = width - currentLine.length();
                if (remaining > 0) {
                    currentLine.append(word, 0, Math.min(word.length(), remaining));
                }
            }
        }

        String linea1 = String.format("%-20s", line1.toString());
        String linea2 = String.format("%-20s", line2.toString());
        return new String[]{linea1, linea2};
    }


    //Método usado para buscar la casilla con el nombre pasado como argumento:
    public Casilla encontrar_casilla(String nombre){
        if (posiciones == null) generarCasillas();

        for (ArrayList<Casilla> calles : posiciones) {
            for (Casilla c : calles) {
                if (c.getNombre().equals(nombre)) return c;
            }
        }

        return null;
    }

    public Casilla avanzarCasillas(Casilla origen, int pasos) {
        // Asegurar que las posiciones están generadas
        if (posiciones == null) generarCasillas();

        int posActual = origen.getPosicion(); // asumo que es 0..39
        // cálculo circular en base 0:
        int nuevaPos = (posActual + pasos) % 40; // resultado 0..39

        // Buscar la casilla con esa posición dentro de "posiciones"
        for (ArrayList<Casilla> fila : posiciones) {
            for (Casilla c : fila) {
                if (c.getPosicion() == nuevaPos) {
                    return c;
                }
            }
        }

        // Si no encuentra (fallback)
        return origen;
    }

    public void estadisticas() {


        Casilla casillaMasRentable = encontrar_casilla("Solar1");
        Casilla casillaMasFrecuentada = encontrar_casilla("Solar1");
        Grupo grupoMasRentable = null;
        Jugador jugadorMasVueltas = null;
        Jugador jugadorEnCabeza = null;

        for (ArrayList<Casilla> fila : posiciones) {
            for (Casilla c : fila) {
                if (c.getTipo().equals("Solar")) {

                    if (c.getRentabilidad() > casillaMasRentable.getRentabilidad()) {
                        casillaMasRentable = c;
                    }
                    else if(c.getFrecuencia() > casillaMasFrecuentada.getFrecuencia()){
                        casillaMasFrecuentada = c;
                    }
                }

            }
        }

        for (Grupo grupo : grupos.values()) {
            for (Casilla c : grupo.getMiembros()) {
                grupo.setRentabilidad(grupo.getRentabilidad() + c.getRentabilidad());
            }
            if(grupoMasRentable == null) {
                //Inicializamos las variables en la primera iteración para poder usar los metodos sin errores.
                grupoMasRentable = grupo;
            }
            if (grupo.getRentabilidad() > grupoMasRentable.getRentabilidad()) {
                grupoMasRentable = grupo;
            }
        }

        for (Jugador jugador : MonopolyETSE.menu.getJugadores()) {

            if(jugadorMasVueltas == null || jugadorEnCabeza == null) {
                //Inicializamos las variables en la primera iteración para poder usar los metodos sin errores.
                jugadorMasVueltas = jugador;
                jugadorEnCabeza = jugador;
            }
            
            if(jugador.getVueltas() > jugadorMasVueltas.getVueltas()) {
                jugadorMasVueltas = jugador;
            }
            if(jugador.getPatrimonio() > jugadorEnCabeza.getPatrimonio()) {
                jugadorEnCabeza = jugador;
            }
        }

        System.out.println("\tcasillaMasRentable: " + casillaMasRentable.getNombre());
        System.out.println("\tgrupoMasRentable: " + grupoMasRentable.getColorGrupo());
        System.out.println("\tcasillaMasFrecuentada: " + casillaMasFrecuentada.getNombre());
        System.out.println("\tjugadorMasVueltas: " + jugadorMasVueltas.getNombre());
        System.out.println("\tjugadorEnCabeza: " + jugadorEnCabeza.getNombre() + "\n");
        System.out.println("}");
        
        return;
    }



    private final List<List<Integer>> SolServTrans = Arrays.asList(
            Arrays.asList(1,3,6,8,9,11,13,14,16,18,19,21,23,24,26,27,29,31,32,34,37,39), // Posición Solares
            Arrays.asList(12,28), // Posición Servicios
            Arrays.asList(5,15,25,35) // Posición Transporte
    );
    private final List<Integer> impuestos = Arrays.asList(4,38);
    private final List<List<Integer>> SuCajEsp = Arrays.asList(
            Arrays.asList(7,22,36),
            Arrays.asList(2,17,33),
            Arrays.asList(0,10,20,30)
    );

    // Lista con Solar, Precio e Hipoteca
    private final List<List<Object>> Solares = Arrays.asList(
            Arrays.asList("Solar1", 600000, 300000),
            Arrays.asList("Solar2", 600000, 300000),
            Arrays.asList("Solar3", 1000000, 500000),
            Arrays.asList("Solar4", 1000000, 500000),
            Arrays.asList("Solar5", 1200000, 600000),
            Arrays.asList("Solar6", 1400000, 700000),
            Arrays.asList("Solar7", 1400000, 700000),
            Arrays.asList("Solar8", 1600000, 800000),
            Arrays.asList("Solar9", 1800000, 900000),
            Arrays.asList("Solar10", 1800000, 900000),
            Arrays.asList("Solar11", 2200000, 1100000),
            Arrays.asList("Solar12", 2200000, 1100000),
            Arrays.asList("Solar13", 2200000, 1100000),
            Arrays.asList("Solar14", 2400000, 1200000),
            Arrays.asList("Solar15", 2600000, 1300000),
            Arrays.asList("Solar16", 2600000, 1300000),
            Arrays.asList("Solar17", 2800000, 1400000),
            Arrays.asList("Solar18", 3000000, 1500000),
            Arrays.asList("Solar19", 3000000, 1500000),
            Arrays.asList("Solar20", 3200000, 1600000),
            Arrays.asList("Solar21", 3500000, 1750000),
            Arrays.asList("Solar22", 4000000, 2000000)
    );

    // Lista con Precio casa, Precio hotel, Precio de piscina, Precio de pista de deporte
    private final List<List<Object>> PreciosConstruccion = Arrays.asList(
            Arrays.asList("Solar1", 500000, 500000, 100000, 200000),
            Arrays.asList("Solar2", 500000, 500000, 100000, 200000),
            Arrays.asList("Solar3", 500000, 500000, 100000, 200000),
            Arrays.asList("Solar4", 500000, 500000, 100000, 200000),
            Arrays.asList("Solar5", 500000, 500000, 100000, 200000),
            Arrays.asList("Solar6", 1000000, 1000000, 200000, 400000),
            Arrays.asList("Solar7", 1000000, 1000000, 200000, 400000),
            Arrays.asList("Solar8", 1000000, 1000000, 200000, 400000),
            Arrays.asList("Solar9", 1000000, 1000000, 200000, 400000),
            Arrays.asList("Solar10", 1000000, 1000000, 200000, 400000),
            Arrays.asList("Solar11", 1000000, 1000000, 200000, 400000),
            Arrays.asList("Solar12", 1500000, 1500000, 300000, 600000),
            Arrays.asList("Solar13", 1500000, 1500000, 300000, 600000),
            Arrays.asList("Solar14", 1500000, 1500000, 300000, 600000),
            Arrays.asList("Solar15", 1500000, 1500000, 300000, 600000),
            Arrays.asList("Solar16", 1500000, 1500000, 300000, 600000),
            Arrays.asList("Solar17", 1500000, 1500000, 300000, 600000),
            Arrays.asList("Solar18", 2000000, 2000000, 400000, 800000),
            Arrays.asList("Solar19", 2000000, 2000000, 400000, 800000),
            Arrays.asList("Solar20", 2000000, 2000000, 400000, 800000),
            Arrays.asList("Solar21", 2000000, 2000000, 400000, 800000),
            Arrays.asList("Solar22", 2000000, 2000000, 400000, 800000)
    );

    // Lista con todos los alquileres
    private final List<List<Object>> Alquileres = Arrays.asList(
            Arrays.asList("Solar1", 20000, 400000, 2500000, 500000, 500000),
            Arrays.asList("Solar2", 40000, 800000, 4500000, 900000, 900000),
            Arrays.asList("Solar3", 60000, 1000000, 5500000, 1100000, 1100000),
            Arrays.asList("Solar4", 60000, 1000000, 5500000, 1100000, 1100000),
            Arrays.asList("Solar5", 80000, 1250000, 6000000, 1200000, 1200000),
            Arrays.asList("Solar6", 100000, 1500000, 7500000, 1500000, 1500000),
            Arrays.asList("Solar7", 100000, 1500000, 7500000, 1500000, 1500000),
            Arrays.asList("Solar8", 120000, 1750000, 9000000, 1800000, 1800000),
            Arrays.asList("Solar9", 140000, 1850000, 9500000, 1900000, 1900000),
            Arrays.asList("Solar10", 140000, 1850000, 9500000, 1900000, 1900000),
            Arrays.asList("Solar11", 160000, 2000000, 10000000, 2000000, 2000000),
            Arrays.asList("Solar12", 180000, 2200000, 10500000, 2100000, 2100000),
            Arrays.asList("Solar13", 180000, 2200000, 10500000, 2100000, 2100000),
            Arrays.asList("Solar14", 200000, 2325000, 11000000, 2200000, 2200000),
            Arrays.asList("Solar15", 220000, 2450000, 11500000, 2300000, 2300000),
            Arrays.asList("Solar16", 220000, 2450000, 11500000, 2300000, 2300000),
            Arrays.asList("Solar17", 240000, 2600000, 12000000, 2400000, 2400000),
            Arrays.asList("Solar18", 260000, 2750000, 12750000, 2550000, 2550000),
            Arrays.asList("Solar19", 260000, 2750000, 12750000, 2550000, 2550000),
            Arrays.asList("Solar20", 280000, 3000000, 14000000, 2800000, 2800000),
            Arrays.asList("Solar21", 350000, 3250000, 17000000, 3400000, 3400000),
            Arrays.asList("Solar22", 500000, 4250000, 20000000, 4000000, 4000000)
    );
}
