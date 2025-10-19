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

        sc.close();
    }
}
