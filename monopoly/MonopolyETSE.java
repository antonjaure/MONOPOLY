package monopoly;

import partida.Jugador;

public class MonopolyETSE {
    public static Menu menu = new Menu();
    public static Tablero tablero = new Tablero(menu.getBanca());

    public static void main(String[] args) {
        System.out.println(Valor.RED + "\nMonopoly ETSE\n" + Valor.RESET);
        System.out.println("Iniciando partida...\n");
        System.out.println(tablero.toString()); // esto crea también las casillas, asi que primero se tiene que imprimir el tablero vacío para poder
        // añadir los avatares y el resto de atributos

        Jugador jugador1 = new Jugador("anton", "coche", tablero.encontrar_casilla("Solar5"), menu.getAvatares());
        Jugador jugador2 = new Jugador("dani", "avión", tablero.encontrar_casilla("Solar5"), menu.getAvatares());

        System.out.println("\nImprimiendo tablero...\n");
        System.out.println(tablero.toString());
    }

}
