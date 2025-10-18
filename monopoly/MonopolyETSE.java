package monopoly;

import partida.Jugador;

public class MonopolyETSE {

    public static Menu menu = new Menu();
    public static Tablero tablero = new Tablero(menu.getBanca());

    public static void main(String[] args) {

        System.out.println(Valor.RED + "\nMonopoly ETSE\n" + Valor.RESET);
        System.out.println("Iniciando partida...\n");

        Jugador jugador1 = new Jugador("anton", "coche", tablero.encontrar_casilla("Solar5"), menu.getAvatares());
        Jugador jugador2 = new Jugador("dani", "avión", tablero.encontrar_casilla("Solar5"), menu.getAvatares());

        System.out.println("\nImprimiendo tablero...\n");
        System.out.println(tablero.toString());

        // Asignar el tablero al menú para que pueda gestionarlo
        menu.setTablero(tablero);

        menu.setJugador(jugador1);
        menu.setJugador(jugador2);

        // Mostrar tablero con jugadores colocados
        System.out.println("\nTablero con jugadores:\n");
        System.out.println(tablero.toString());

        // -------------------------------
        // PRUEBA DEL MENÚ CON SWITCH
        // -------------------------------
        System.out.println("\n=== Simulación de partida ===");

        System.out.println("\nTurno de Anton:");
        menu.ejecutarOpcion("lanzar dados");
        menu.ejecutarOpcion("acabar turno");

        System.out.println("\nTurno de Dani:");
        menu.ejecutarOpcion("lanzar dados");
        menu.ejecutarOpcion("acabar turno");

        System.out.println("\n--- Fin de la simulación ---");
    }
}
