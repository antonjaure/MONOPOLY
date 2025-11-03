package monopoly;

//NO PARECE MUY NECESARIO TENER UNA CLASE CARTA

public class Carta {
    private String descripcion; //despcripcion de la carta
    private int id; //identificador de la carta (el numero de la carta)


    public Carta(String descripcion, int id) {
        this.descripcion = descripcion;
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }
 
}
