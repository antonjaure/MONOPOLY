package excepciones;

public class DadosSinLanzarException extends DadosException {
    public DadosSinLanzarException() {
        super("\tNo puedes terminar el turno sin haber lanzado los dados al menos una vez.");
    }
}
