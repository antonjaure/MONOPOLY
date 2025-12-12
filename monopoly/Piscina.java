package monopoly;

public class Piscina extends Edificio {

    public Piscina(String nombre, Solar terreno, float coste, float alquiler) {
        super(nombre, terreno, coste, alquiler);
    }

    @Override
    public String getTipo() {
        return "piscina";
    }
}