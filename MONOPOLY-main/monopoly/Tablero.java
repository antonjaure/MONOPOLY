package monopoly;

import partida.*;

import java.util.*;


public class Tablero {
    //Atributos.
    private ArrayList<ArrayList<Casilla>> posiciones = null; //Posiciones del tablero: se define como un arraylist de arraylists de casillas (uno por cada lado del tablero).
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    private Jugador banca = new Jugador(); //Un jugador que será la banca.

    public void setGrupos(String color, Grupo grupo) {
        this.grupos.put(color, grupo);
    }

    public Tablero() {}

    //Constructor: únicamente le pasamos el jugador banca (que se creará desde el menú).
    public Tablero(Jugador banca) {
        this.banca = banca;
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

    /////////////////// AÑADIR PRECAUCION SI NO ENCUENTRA LA CASILLA (null)
    private void generarGrupos() {
        if (posiciones == null) generarCasillas();
        if (grupos == null) grupos = new HashMap<>();

        Grupo negro =  new Grupo(encontrar_casilla((String) Solares.get(0).get(0)),
                encontrar_casilla((String) Solares.get(1).get(0)),
                Valor.BLACK);
        setGrupos("negro", negro);
        Grupo cian =  new Grupo(encontrar_casilla((String) Solares.get(2).get(0)),
                encontrar_casilla((String) Solares.get(3).get(0)),
                encontrar_casilla((String) Solares.get(4).get(0)),
                Valor.CYAN);
        setGrupos("cian", cian);
        Grupo morado =  new Grupo(encontrar_casilla((String) Solares.get(5).get(0)),
                encontrar_casilla((String) Solares.get(6).get(0)),
                encontrar_casilla((String) Solares.get(7).get(0)),
                Valor.PURPLE);
        setGrupos("morado", morado);
        Grupo amarillo =  new Grupo(encontrar_casilla((String) Solares.get(8).get(0)),
                encontrar_casilla((String) Solares.get(9).get(0)),
                encontrar_casilla((String) Solares.get(10).get(0)),
                Valor.YELLOW);
        setGrupos("amarillo", amarillo);
        Grupo rojo =  new Grupo(encontrar_casilla((String) Solares.get(11).get(0)),
                encontrar_casilla((String) Solares.get(12).get(0)),
                encontrar_casilla((String) Solares.get(13).get(0)),
                Valor.RED);
        setGrupos("rojo", rojo);
        Grupo blanco =  new Grupo(encontrar_casilla((String) Solares.get(14).get(0)),
                encontrar_casilla((String) Solares.get(15).get(0)),
                encontrar_casilla((String) Solares.get(16).get(0)),
                Valor.WHITE);
        setGrupos("blanco", blanco);
        Grupo verde =  new Grupo(encontrar_casilla((String) Solares.get(17).get(0)),
                encontrar_casilla((String) Solares.get(18).get(0)),
                encontrar_casilla((String) Solares.get(19).get(0)),
                Valor.GREEN);
        setGrupos("verde", verde);
        Grupo azul =  new Grupo(encontrar_casilla((String) Solares.get(20).get(0)),
                encontrar_casilla((String) Solares.get(21).get(0)),
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
                String color = norte.get(j).getGrupo().getColorGrupo();
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
                String color = sur.get(j).getGrupo().getColorGrupo();
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
                String color = oeste.get(j).getGrupo().getColorGrupo();
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
                String color = este.get(j-1).getGrupo().getColorGrupo();
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
                    String[] contenidoCasilla = matrizTablero[i][j].split(";");
                    String calle = contenidoCasilla[0];

                    // Al dividir la cadena hay que volver a formatear el color
                    String color = "";
                    boolean solar = calle.startsWith("\u001B[");
                    if (solar) {
                        color = calle.substring(0, calle.indexOf('m') + 1);
                        calle = calle.substring(calle.indexOf('m') + 1, calle.length() - 4);
                    }

                    String[] lineasDiv = splitByWords(calle, lenCasilla/2);

                    String avatar = "";
                    if (matrizTablero[i][j].contains(";")) {
                        int lenAv = contenidoCasilla[1].length();
                        avatar = "\033[1m" + contenidoCasilla[1] + "\033[22m";

                        // Calcula cuántos espacios poner entre base y el texto añadido
                        int margen = 1;
                        lineasDiv[1] = lineasDiv[1].trim(); // quita espacios sobrantes
                        int espacios = lenCasilla/2 - lineasDiv[1].length() - lenAv - margen;
                        if (espacios < 0) espacios = 0;  // evita números negativos

                        lineasDiv[1] = lineasDiv[1] + " ".repeat(espacios) + avatar + " ".repeat(margen);
                    }

                    if (solar) {
                        lineasDiv[0] = color + lineasDiv[0] + Valor.RESET;
                        lineasDiv[1] = color + lineasDiv[1] + Valor.RESET;
                    }

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
    } ///////////////////////////////////

    //Método para asignar el tipo de casilla
    private Casilla asignarCasilla(int pos) {
        if (SolServTrans.get(0).contains(pos)) { // Solares
            String nombre =  Solares.get(SolServTrans.get(0).indexOf(pos)).get(0).toString();
            String tipo = "Solar";
            float valor = (int) Solares.get(SolServTrans.get(0).indexOf(pos)).get(1);
            return new Casilla(nombre, tipo, pos, valor, banca);
        }
        if (SolServTrans.get(1).contains(pos)) { // Servicios
            String nombre = "Serv" + SolServTrans.get(1).indexOf(pos);
            String tipo = "Servicios";
            float valor = 500000;
            return new Casilla(nombre, tipo, pos, valor, banca);
        }
        if (SolServTrans.get(2).contains(pos)) { // Transporte
            String nombre = "Trans" + (SolServTrans.get(2).indexOf(pos) + 1);
            String tipo = "Transporte";
            float valor = 500000;
            return new Casilla(nombre, tipo, pos, valor, banca);
        }

        if (impuestos.contains(pos)) { // Impuestos
            String nombre = "Imp" + (impuestos.indexOf(pos) + 1);
            return new Casilla(2000000, nombre, "Impuestos", pos, banca);
        }

        if (SuCajEsp.get(0).contains(pos)) { // Suerte
            return new Casilla("Suerte", "Suerte", pos, banca);
        }
        if (SuCajEsp.get(1).contains(pos)) { // Caja
            return new Casilla("Caja", "Comunidad", pos, banca);
        }
        if (SuCajEsp.get(2).contains(pos)) { // Especial
            int n = SuCajEsp.get(2).indexOf(pos);
            String nombre = "";
            switch (n) {
                case 0: nombre = "Salida"; break;
                case 1: nombre = "Cárcel"; break;
                case 2: nombre = "Parking"; break;
                case 3: nombre = "IrCarcel"; break;
            }
            return new Casilla(nombre, "Especial", pos, banca);
        }

        return new Casilla();
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

            int spaceNeeded = (currentLine.length() == 0 ? 0 : 1) + word.length();

            if (currentLine.length() + spaceNeeded <= width) {
                // cabe en la línea actual
                if (currentLine.length() != 0) currentLine.append(" ");
                currentLine.append(word);
            } else if (firstLine) {
                // pasa a la segunda línea
                firstLine = false;
                currentLine = line2;
                if (currentLine.length() != 0) currentLine.append(" ");
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

    private static String padRight(String s, int width) {
        if (s == null) s = "";
        int padding = width - s.length();
        return s + " ".repeat(Math.max(0, padding));
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

    //////////////// CUIDADO CON LOS ESPACIOS, LOS NOMBRES TIENEN QUE SER IGUALES ARRIBA Y ABAJO
    private final List<List<Object>> Solares = Arrays.asList(
            Arrays.asList("RONDA DE VALENCIA", 600000),
            Arrays.asList("PLAZA LAVAPIÉS", 600000),
            Arrays.asList("GLORIETA CUATRO CAMINOS", 1000000),
            Arrays.asList("AVENIDA DE REINA VICTORIA", 1000000),
            Arrays.asList("CALLE BRAVO MURILLO", 1200000),
            Arrays.asList("GLORIETA DE BILBAO", 1400000),
            Arrays.asList("CALLE ALBERTO AGUILERA", 1400000),
            Arrays.asList("CALLE FUENCARRAL", 1600000),
            Arrays.asList("AVENIDA FELIPE II", 1800000),
            Arrays.asList("CALLE VELÁZQUEZ", 1800000),
            Arrays.asList("CALLE SERRANO", 2200000),
            Arrays.asList("AVENIDA DE AMÉRICA", 2200000),
            Arrays.asList("CALLE MARÍA MOLINA", 2200000),
            Arrays.asList("CALLE CEA BERMÚDEZ", 2400000),
            Arrays.asList("AVENIDA DE LOS REYES CATÓLICOS", 2600000),
            Arrays.asList("CALLE BAILÉN", 2600000),
            Arrays.asList("PLAZA DE ESPAÑA", 2800000),
            Arrays.asList("PUERTA DEL SOL", 3000000),
            Arrays.asList("CALLE DE ALCALÁ", 3000000),
            Arrays.asList("GRAN VÍA", 3200000),
            Arrays.asList("PASEO DE LA CASTELLANA", 3500000),
            Arrays.asList("PASEO DEL PRADO", 4000000)
    );

    public ArrayList<ArrayList<Casilla>> getPosiciones() {
        return this.posiciones;
    }
}
