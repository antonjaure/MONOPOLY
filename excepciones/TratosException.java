package excepciones;

import monopoly.Valor;

public class TratosException extends MonopolyException {
    public TratosException(String mensaje) {
        super(Valor.PURPLE + "No se ha podido completar el Trato: "+ mensaje + Valor.RESET);
    }

}
