package monopoly;

public class Edificio {
    private String nombre;
    private Casilla terreno;
    private String tipo;
    private int coste;
    private int alquiler;

    public Edificio(String nombre, Casilla terreno, String tipo) {
        this.nombre = nombre;
        this.terreno = terreno;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Casilla getCasilla() {
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
