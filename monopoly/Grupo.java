package monopoly;

import partida.*;
import java.util.ArrayList;
import java.util.Arrays;


class Grupo {

    //Atributos
    private ArrayList<Casilla> miembros; //Casillas miembros del grupo.
    private String colorGrupo; //Color del grupo
    private String codigoColor;
    private int numCasillas; //Número de casillas del grupo.


    public ArrayList<Casilla> getMiembros() {
        return this.miembros;
    }
    public String getColorGrupo() {
        return colorGrupo;
    }

    public String getCodigoColor() {
        return codigoColor;
    }

    public int numCasillas() {
        return numCasillas;
    }

    public void setMiembros(ArrayList<Casilla> miembros) {
        this.miembros = miembros;
    }

    
    //Constructor vacío.
    public Grupo(){
    }

    /*Constructor para cuando el grupo está formado por DOS CASILLAS:
    * Requiere como parámetros las dos casillas miembro y el color del grupo.
     */
    public Grupo(Casilla cas1, Casilla cas2, String colorGrupo, String codigoColor) {
        this.miembros = new ArrayList<>(Arrays.asList(cas1, cas2));
        cas1.setGrupo(this); cas2.setGrupo(this);
        this.colorGrupo = colorGrupo;
        this.codigoColor = codigoColor;
        this.numCasillas = 2;
    }

    /*Constructor para cuando el grupo está formado por TRES CASILLAS:
    * Requiere como parámetros las tres casillas miembro y el color del grupo.
     */
    public Grupo(Casilla cas1, Casilla cas2, Casilla cas3, String colorGrupo, String codigoColor) {
        this.miembros = new ArrayList<>(Arrays.asList(cas1, cas2, cas3));
        cas1.setGrupo(this); cas2.setGrupo(this); cas3.setGrupo(this);
        this.colorGrupo = colorGrupo;
        this.codigoColor = codigoColor;
        this.numCasillas = 3;
    }

    /* Método que añade una casilla al array de casillas miembro de un grupo.
    * Parámetro: casilla que se quiere añadir.
     */
    public void anhadirCasilla(Casilla miembro) {
        
    
    }

    /*Método que comprueba si el jugador pasado tiene en su haber todas las casillas del grupo:
    * Parámetro: jugador que se quiere evaluar.
    * Valor devuelto: true si es dueño de todas las casillas del grupo, false en otro caso.
     */
    public boolean esDuenhoGrupo(Jugador jugador) {
        ArrayList<Casilla> propiedades = jugador.getPropiedades();
        int lenGrupo = miembros.size();
        int count = 0;
        for (Casilla casilla : propiedades) {
            if (casilla.getGrupo() == this) count++;
        }
        return count == lenGrupo;
    }

}
