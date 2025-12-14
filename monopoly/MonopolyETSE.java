package monopoly;

public class MonopolyETSE {

    public static Juego juego;

    public static void main(String[] args) {
        juego = new Juego();
        
        if (args.length > 0) {
            juego.analizarArchivo(args);
        }

        juego.iniciarPartida();
    }
}
