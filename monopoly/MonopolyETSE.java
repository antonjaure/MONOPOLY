package monopoly;

import partida.Jugador;

import java.util.Scanner;

public class MonopolyETSE {

    public static Menu menu = new Menu();
    public static Tablero tablero = new Tablero(menu.getBanca());

    public static void main(String[] args) {
        System.out.println(Valor.RED + "Monopoly ETSE\n" + Valor.RESET);
        System.out.println("\nIniciando partida...\n");
        System.out.println(tablero.toString()); // esto también crea las casillas, si se quisiese crear las casillas
        // sin imprimir el tablero habría que llamar a la función generarCasillas() de Tablero
        Scanner sc = new Scanner(System.in);
        String comando = "";

        // Bucle para leer los comandos
        do {
            System.out.println("Escribe una acción: ");
            comando = sc.nextLine();
            menu.ejecutarComando(comando);
        } while (!comando.equals("salir")); // comando para finalizar el programa

/*  /////////////////    PRUEBA DE DANI    //////////////////

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
*/

        sc.close();
    }
}
