package monopoly;

public class Hotel extends Edificio {
    public Hotel(String nombre, Casilla terreno, float coste, float alquiler){
        super(nombre,terreno,coste,alquiler);
    }

    @Override
    public String getTipo(){
        return "hotel";
    }
}
