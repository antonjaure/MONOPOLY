package monopoly;

public abstract class Edificio {
    protected String nombre;
    protected Casilla terreno;
    protected float coste;
    protected float alquiler;

    public Edificio(String nombre, Casilla terreno, float coste, float alquiler) {
        this.nombre = nombre;
        this.terreno = terreno;
        this.coste = coste;
        this.alquiler = alquiler;
    }

    // Cada subclase nos dirá qué tipo es 
    public abstract String getTipo();

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
}
