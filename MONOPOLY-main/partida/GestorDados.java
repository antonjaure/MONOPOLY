package partida;


public class GestorDados {
    private Dado dado1;
    private Dado dado2;
    private int contadordobles;
    private boolean valoresforzados; // variable bandera para saber si los valores de los dados tienen que ser forzados
    private int valorforzado1;
    private int valorforzado2;

    public GestorDados(){
        this.dado1 = new Dado();
        this.dado2 = new Dado();
        this.contadordobles = 0;
        this.valoresforzados = false;
    }

    public ResultadoTirada lanzarDados(){
        int valor1, valor2;
        if(valoresforzados){
            valor1 = valorforzado1;
            valor2 = valorforzado2;
            valoresforzados = false;
        }else{
            valor1 = dado1.hacerTirada();
            valor2 = dado2.hacerTirada();
        }
        boolean esDoble=(valor1==valor2);
        boolean irCarcel= false;
        if(esDoble){
            contadordobles++;
            if(contadordobles==3){
                irCarcel=true;
                contadordobles=0;
            }
        }else{
            contadordobles=0;
        }
        return new ResultadoTirada(valor1,valor2,esDoble,irCarcel);
    }

    public void forzarTirada(int valor1, int valor2) {
        if (valor1 < 1 || valor1 > 6 || valor2 < 1 || valor2 > 6) {
            System.out.println("Los valores deben estar entre 1 y 6");
        }
        this.valorforzado1 = valor1;
        this.valorforzado2 = valor2;
        this.valoresforzados = true;
    }

    public int getContadorDobles() {
        return contadordobles;
    }

    public void resetContadorDobles() {
        this.contadordobles = 0;
    }


}

