package excepciones;

public class PropiedadSinHipotecarException extends PropiedadesException {

    public PropiedadSinHipotecarException(String nombrePropiedad) {
        super("\tOperación inválida: La propiedad '" + nombrePropiedad + "' no ha sido hipotecada.");
    }

}