package excepciones;

import monopoly.Valor;

//Los errores relacionados con las finanzas en el juego se printean en azul
public class PropiedadesException extends MonopolyException {
    public PropiedadesException(String mensaje) {
        super(Valor.BLUE + mensaje + Valor.RESET);
    }
}
