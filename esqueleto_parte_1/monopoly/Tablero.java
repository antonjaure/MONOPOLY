package monopoly;

import partida.*;

import java.util.*;


public class Tablero {
    //Atributos.
    private ArrayList<ArrayList<Casilla>> posiciones; //Posiciones del tablero: se define como un arraylist de arraylists de casillas (uno por cada lado del tablero).
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    private Jugador banca = new Jugador(); //Un jugador que será la banca.

    public Tablero() {}

    //Constructor: únicamente le pasamos el jugador banca (que se creará desde el menú).
    public Tablero(Jugador banca) {
        this.banca = banca;
    }
    
    //Método para crear todas las casillas del tablero. Formado a su vez por cuatro métodos (1/lado).
    private void generarCasillas() {
        if (posiciones == null){
            posiciones = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                posiciones.add(new ArrayList<>());
            }
        }

        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();

        generarGrupos();
    }

    private void generarGrupos() {
        if (posiciones == null) generarCasillas();
        Grupo negro =  new Grupo(encontrar_casilla("RONDA DE VALENCIA"),
                encontrar_casilla("PLAZA LAVAPIÉS"),
                Valor.BLACK);
        Grupo cian =  new Grupo(encontrar_casilla("GLORIETA CUATRO CAMINOS"),
                encontrar_casilla("AVENIDA DE REINA VICTORIA"),
                encontrar_casilla("CALLE BRAVO MURILLO"),
                Valor.CYAN);
        Grupo morado =  new Grupo(encontrar_casilla("GLORIETA DE BILBAO"),
                encontrar_casilla("CALLE ALBERTO AGUILERA"),
                encontrar_casilla("CALLE FUENCARRAL"),
                Valor.PURPLE);
        Grupo amarillo =  new Grupo(encontrar_casilla("AVENIDA FELIPE II"),
                encontrar_casilla("CALLE VELÁZQUEZ"),
                encontrar_casilla("CALLE SERRANO"),
                Valor.YELLOW);
        Grupo rojo =  new Grupo(encontrar_casilla("AVENIDA DE AMÉRICA"),
                encontrar_casilla("CALLE MARÍA DE MOLINA"),
                encontrar_casilla("CALLE CEA BERMÚDEZ"),
                Valor.RED);
        Grupo blanco =  new Grupo(encontrar_casilla("AVENIDA DE LOS REYES CATÓLICOS"),
                encontrar_casilla("CALLE BAILÉN"),
                encontrar_casilla("PLAZA DE ESPAÑA"),
                Valor.WHITE);
        Grupo verde =  new Grupo(encontrar_casilla("PUERTA DEL SOL"),
                encontrar_casilla("CALLE DE ALCALÁ"),
                encontrar_casilla("GRAN VÍA "),
                Valor.GREEN);
        Grupo azul =  new Grupo(encontrar_casilla("PASEO DE LA CASTELLANA"),
                encontrar_casilla("PASEO DEL PRADO"),
                Valor.BLUE);
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

    //Método para asignar el tipo de casilla
    private Casilla asignarCasilla(int pos) {
        if (SolServTrans.get(0).contains(pos)) { // Solares
            String nombre =  Solares.get(SolServTrans.get(0).indexOf(pos)).getFirst().toString();
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

    //Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {
        generarCasillas();

        // Dimensiones del tablero
        int filas = 11;  // Total de filas necesarias
        int columnas = 11; // Total de columnas necesarias

        byte iSur = 4;
        byte iOeste = 5;
        byte iNorte = 6;
        byte iEste = 7;

        // Crear matriz para organizar el tablero
        String[][] matrizTablero = new String[filas][columnas];

        // Inicializar con espacios vacíos
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matrizTablero[i][j] = "          "; // 8 espacios para cada celda
            }
        }

        // Llenar lado NORTE (fila 0)
        ArrayList<Casilla> norte = posiciones.get(iNorte);
        for (int j = 0; j < posiciones.get(iNorte).size(); j++) {
            ArrayList<Avatar> avatares = norte.get(j).getAvatares();
            if (norte.get(j).getTipo().equals("Solar")) {
                String color = norte.get(j).getGrupo().getColorGrupo();
                String nombre = norte.get(j).getTipo();
                StringBuilder avs = new StringBuilder();
                if (!avatares.isEmpty()){
                    for (Avatar av : avatares){
                        String ID = av.getId();
                        avs.append(""  ID);
                    }


                }

                matrizTablero[0][j] = String.format("%s%-10s%s", color, norte.get(j).getNombre(), Valor.RESET);
            }
            else matrizTablero[0][j] = String.format("%-10s", norte.get(j).getNombre());
        }

        // Llenar lado SUR (fila 10)
        ArrayList<Casilla> sur = posiciones.get(iSur);
        int tam = posiciones.get(iSur).size();
        for (int j = tam - 1; j > -1; j--) {
            if (sur.get(j).getTipo().equals("Solar")) {
                String color = sur.get(j).getGrupo().getColorGrupo();
                    matrizTablero[10][tam - j - 1] = String.format("%s%-10s%s", color, sur.get(j).getNombre(), Valor.RESET);
            }
            else matrizTablero[10][tam - j - 1] = String.format("%-10s", sur.get(j).getNombre());
        }

        // Llenar lado OESTE (columna 0)
        ArrayList<Casilla> oeste = posiciones.get(iOeste);
        tam = posiciones.get(iOeste).size();
        for (int j = tam - 1; j > -1; j--) {
            if (oeste.get(j).getTipo().equals("Solar")) {
                String color = oeste.get(j).getGrupo().getColorGrupo();
                matrizTablero[tam - j][0] = String.format("%s%-10s%s", color, oeste.get(j).getNombre(), Valor.RESET);
            }
            else matrizTablero[tam - j][0] = String.format("%-10s", oeste.get(j).getNombre());
        }

        // Llenar lado ESTE (columna 10)
        ArrayList<Casilla> este = posiciones.get(iEste);
        for (int j = 1; j < 10; j++) {
            if (este.get(j-1).getTipo().equals("Solar")) {
                String color = este.get(j-1).getGrupo().getColorGrupo();
                matrizTablero[j][10] = String.format("%s%-10s%s", color, este.get(j-1).getNombre(), Valor.RESET);
            }
            else matrizTablero[j][10] = String.format("%-10s", este.get(j-1).getNombre());
        }

        StringBuilder tablero = new StringBuilder();
        tablero.append(" -----------".repeat(columnas)).append("\n");

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (!matrizTablero[i][j].equals("          ")) {
                    if (j == 0) tablero.append("| ").append(matrizTablero[i][j]).append("| ");
                    else if (!matrizTablero[i][j-1].equals("          ")) tablero.append(matrizTablero[i][j]).append("| ");
                    else tablero.append("| ").append(matrizTablero[i][j]).append("| ");
                }
                else if (!matrizTablero[i][j+1].equals("          ")) tablero.append(matrizTablero[i][j]);
                else tablero.append("            ");
            }
            if (i == 0 || i == 9 || i == 10) tablero.append("\n").append(" -----------".repeat(columnas)).append("\n");
            else tablero.append("\n").append(" -----------").append(" ".repeat(108)).append(" -----------").append("\n");
        }

        return tablero.toString();
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

    private List<List<Integer>> SolServTrans = Arrays.asList(
            Arrays.asList(1,3,6,8,9,11,13,14,16,18,19,21,23,24,26,27,29,31,32,34,37,39), // Posición Solares
            Arrays.asList(12,28), // Posición Servicios
            Arrays.asList(5,15,25,35) // Posición Transporte
    );
    private List<Integer> impuestos = Arrays.asList(4,38);
    private List<List<Integer>> SuCajEsp = Arrays.asList(
            Arrays.asList(7,22,36),
            Arrays.asList(2,17,33),
            Arrays.asList(0,10,20,30)
    );

    private List<List<Object>> Solares = Arrays.asList(
            Arrays.asList("RONDA DE VALENCIA", 600000),
            Arrays.asList("PLAZA LAVAPIÉS", 600000),
            Arrays.asList("GLORIETA CUATRO CAMINOS", 1000000),
            Arrays.asList("AVENIDA DE REINA VICTORIA ", 1000000),
            Arrays.asList("CALLE BRAVO MURILLO", 1200000),
            Arrays.asList("GLORIETA DE BILBAO", 1400000),
            Arrays.asList("CALLE ALBERTO AGUILERA", 1400000),
            Arrays.asList("CALLE FUENCARRAL", 1600000),
            Arrays.asList("AVENIDA FELIPE II", 1800000),
            Arrays.asList("CALLE VELÁZQUEZ", 1800000),
            Arrays.asList("CALLE SERRANO", 2200000),
            Arrays.asList("AVENIDA DE AMÉRICA", 2200000),
            Arrays.asList("CALLE DE MARÍA MOLINA", 2200000),
            Arrays.asList("CALLE DE CEA BERMÚDEZ", 2400000),
            Arrays.asList("AVENIDA DE LOS REYES CATÓLICOS", 2600000),
            Arrays.asList("CALLE BAILÉN", 2600000),
            Arrays.asList("PLAZA DE ESPAÑA", 2800000),
            Arrays.asList("PUERTA DEL SOL", 3000000),
            Arrays.asList("CALLE DE ALCALÁ", 3000000),
            Arrays.asList("GRAN VÍA", 3200000),
            Arrays.asList("PASE DE LA CASTELLANA", 3500000),
            Arrays.asList("PASEO DEL PRADO", 4000000)
    );
}
