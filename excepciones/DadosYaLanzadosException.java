package excepciones;

public class DadosYaLanzadosException extends DadosException {
    public DadosYaLanzadosException() {
        super("\tYa has lanzado los dados en este turno y no sacaste dobles. No puedes volver a tirar.");
    }
}
