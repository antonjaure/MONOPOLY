package monopoly;

public class Edificio {
    private String nombre;
    private Casilla terreno;
    private String tipo;
    private int coste;
    private int alquiler;

    // cambiar atributos de valores en Casilla por un Hashmap
    public Edificio(String nombre, Casilla terreno, String tipo) {
        this.nombre = nombre;
        this.terreno = terreno;
        this.tipo = tipo;
    }

    public Casilla getTerreno() {
        return terreno;
    }

    public void setTerreno(Casilla terreno) {
        this.terreno = terreno;
    }

    public int getAlquiler() {
        return alquiler;
    }

    public void setAlquiler(int alquiler) {
        this.alquiler = alquiler;
    }

    public int getCoste() {
        return coste;
    }

    public void setCoste(int coste) {
        this.coste = coste;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
