package monopoly;

public class Edificio {
    private String nombre;
    private Casilla terreno;
    private String tipo;
    private float coste;
    private float alquiler;

    public Edificio(String nombre, Casilla terreno, String tipo, float coste, float alquiler) {
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

    public float getAlquiler() {
        return alquiler;
    }

    public void setAlquiler(float alquiler) {
        this.alquiler = alquiler;
    }

    public float getCoste() {
        return coste;
    }

    public void setCoste(float coste) {
        this.coste = coste;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


}
