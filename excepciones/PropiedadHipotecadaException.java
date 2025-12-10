package excepciones;

public class PropiedadHipotecadaException extends FinanzasException {
    public PropiedadHipotecadaException(String nombrePropiedad) {
        super("Operación inválida: La propiedad '" + nombrePropiedad + "' está hipotecada.");
    }
}
