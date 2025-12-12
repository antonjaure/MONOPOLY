package monopoly;

public class MonopolyETSE {

    // Mantenemos la referencia estática a Juego para que las Cartas (Suerte) puedan acceder si lo necesitan
    public static Juego juego;

    public static void main(String[] args) {
        juego = new Juego();

        //Si hay archivo de comandos (args), lo procesamos
        if (args.length > 0) {
            juego.analizarArchivo(args);
        }
        //Iniciamos el bucle principal del juego
        juego.iniciarPartida();
    }
}
