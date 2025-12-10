package excepciones;

public class DadosYaLanzadosException extends DadosException {
    public DadosYaLanzadosException() {
        super("\t*** Solo una tirada por turno. ***");
    }
}
