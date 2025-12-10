package excepciones;


//Clase Base abtracta para las excepciones de monopoly
public class MonopolyException extends Exception {
    public MonopolyException(String mensaje) {
        super(mensaje);
    }
    
}
