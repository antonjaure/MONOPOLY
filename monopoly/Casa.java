package monopoly;

public class Casa extends Edificio {

    public Casa(String nombre, Casilla terreno, float coste, float alquiler) {
        super(nombre, terreno, coste, alquiler);
    }

    @Override
    public String getTipo() {
        return "casa";
    }
}
