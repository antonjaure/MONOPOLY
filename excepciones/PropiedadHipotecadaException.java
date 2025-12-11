package excepciones;

public class PropiedadHipotecadaException extends PropiedadesException {
    public PropiedadHipotecadaException(String nombrePropiedad) {
        super("\tOperación inválida: La propiedad '" + nombrePropiedad + "' está hipotecada.");
    }
}
