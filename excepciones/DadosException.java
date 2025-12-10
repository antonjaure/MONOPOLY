package excepciones;

import monopoly.Valor;


//Los errores relacionados con los dados en el juego se printean en rojo
public class DadosException extends MonopolyException {

    public DadosException (String mensaje) {
        super(Valor.YELLOW + mensaje + Valor.RESET);
    }
}
