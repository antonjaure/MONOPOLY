package monopoly;

import partida.Jugador;

public class MonopolyETSE {
    public static Menu menu = new Menu();
    public static Jugador banca = new Jugador();
    public static Tablero tablero = new Tablero(banca);
    public static void main(String[] args) {
        System.out.println(Valor.RED + "Monopoly ETSE\n" + Valor.RESET);

        System.out.println("Iniciando partida...\n");
        System.out.println(tablero.toString());
    }

}
