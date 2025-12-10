package excepciones;

import monopoly.Valor;

//Los errores relacionados con las finanzas en el juego se printean en rojo
public class FinanzasException extends MonopolyException {
    public FinanzasException(String mensaje) {
        super(Valor.RED + mensaje + Valor.RESET);
    }
}
