package monopoly;

public class MonopolyETSE {

    // Única referencia estática al juego
    public static Juego juego;

    public static void main(String[] args) {
        juego = new Juego();
        
        if (args.length > 0) {
            juego.analizarArchivo(args);
        }

        juego.iniciarPartida();
    }
}
