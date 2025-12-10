package excepciones;

public class DadosSinLanzarException extends DadosException {
    public DadosSinLanzarException() {
        super("\t*** El jugador actual debe lanzar los dados antes de acabar su turno. ***");
    }
}
