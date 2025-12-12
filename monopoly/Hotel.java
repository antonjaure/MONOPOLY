package monopoly;

public class Hotel extends Edificio {
    public Hotel(String nombre, Solar terreno, float coste, float alquiler){
        super(nombre,terreno,coste,alquiler);
    }

    @Override
    public String getTipo(){
        return "hotel";
    }
}
