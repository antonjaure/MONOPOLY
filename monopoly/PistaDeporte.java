package monopoly;

public class PistaDeporte extends Edificio {

    public PistaDeporte(String nombre, Solar terreno, float coste, float alquiler) {
        super(nombre, terreno, coste, alquiler);
    }

    @Override
    public String getTipo() {
        return "pista"; // 
    }
}