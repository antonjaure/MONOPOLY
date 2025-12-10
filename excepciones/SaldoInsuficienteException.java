package excepciones;

public class SaldoInsuficienteException extends FinanzasException {
    private final int saldoActual;
    private final int cantidadNecesaria;

    public SaldoInsuficienteException(int saldoActual, int cantidadNecesaria) {
        super("Saldo insuficiente. Tienes " + saldoActual + "€ pero necesitas " + cantidadNecesaria + "€.");
        this.saldoActual = saldoActual;
        this.cantidadNecesaria = cantidadNecesaria;
    }

    public int getDeficit() {
        return cantidadNecesaria - saldoActual;
    }
}