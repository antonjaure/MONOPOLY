package excepciones;

public class PropiedadOtroJugadorException extends PropiedadesException {
    
    public PropiedadOtroJugadorException(String nombrePropiedad, String nombrePropietario) {
        super("No puedes comprar '" + nombrePropiedad + "' porque ya pertenece a " + nombrePropietario + ".");
    }
}