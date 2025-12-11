package monopoly;

public interface Consola {
    
    /**
     * Muestra un mensaje al usuario. 
     * Implementación: Debe usar System.out.println() o equivalente.
     */
    public void imprimir(String mensaje);
    
    /**
     * Muestra una descripción y espera la entrada del usuario.
     * Implementación: Debe usar Scanner o equivalente para leer.
     * * @param descripcion Mensaje a mostrar al usuario (ej: "Introduce nombre: ")
     * @return El dato introducido por el usuario como String.
     */
    public String leer(String descripcion);
}