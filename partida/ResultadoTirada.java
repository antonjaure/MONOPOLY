package partida;

public class ResultadoTirada {
    private int dado1;
    private int dado2;
    private boolean esDoble;
    private boolean irCarcel;

    public ResultadoTirada(int dado1, int dado2, boolean esDoble, boolean irCarcel) {
        this.dado1 = dado1;
        this.dado2 = dado2;
        this.esDoble = esDoble;
        this.irCarcel = irCarcel;
    }

    public int getTotal() {
        return dado1 + dado2;
    }

    public int getDado1() {
        return dado1;
    }

    public int getDado2() {
        return dado2;
    }

    public boolean esDoble() {
        return esDoble;
    }

    public boolean irCarcel() {
        return irCarcel;
    }

    @Override
    public String toString() {
        String resultado = dado1 + "+" + dado2 + " = " + getTotal();
        if (esDoble) {
            resultado += " (Dobles)";
        }
        if (irCarcel) {
            resultado += " - ¡Ir a la Cárcel!";
        }
        return resultado;
    }

    public String toStringParaComando() {
        return "El avatar avanza " + getTotal() + " posiciones, desde [posición_actual] hasta [nueva_posición].";
    }
}
