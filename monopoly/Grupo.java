package monopoly;

import partida.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class Grupo {

    //Atributos
    private ArrayList<Propiedad> miembros; //Solares miembros del grupo.
    private String colorGrupo; //Color del grupo
    private String codigoColor;
    private int numCasillas; //Número de casillas del grupo.
    private float rentabilidad; // Rentabilidad del grupo


    public ArrayList<Propiedad> getMiembros() {
        return this.miembros;
    }
    public String getColorGrupo() {
        return colorGrupo;
    }

    public String getCodigoColor() {
        return codigoColor;
    }

    public float getRentabilidad() {
        return rentabilidad;
    }

    public void setRentabilidad(float rentabilidad) {
        this.rentabilidad = rentabilidad;
    }

    public int numCasillas() {
        return numCasillas;
    }

    public void setMiembros(ArrayList<Propiedad> miembros) {
        this.miembros = miembros;
    }

    
    //Constructor vacío.
    public Grupo(){
    }

    /*Constructor para cuando el grupo está formado por DOS SOLARES:
    * Requiere como parámetros las dos casillas miembro y el color del grupo.
     */
    public Grupo(String cas1, String cas2, String colorGrupo, String codigoColor) {
        List<Integer> Posiciones = MonopolyETSE.tablero.getSolServTrans().get(0);
        List<List<Object>> Solares = MonopolyETSE.tablero.getSolares();

        int pos1 = 0, pos2 = 0;

        for (int i = 0; i < Posiciones.size(); i++) {
            if (cas1 == Solares.get(i).getFirst()) {
                pos1 = i;
            }
            else if (cas2 == Solares.get(i).getFirst()) {
                pos2 = i;
            }
        }

        float valor1 = (int) Solares.get(Posiciones.indexOf(pos1)).get(1);
        float valor2 = (int) Solares.get(Posiciones.indexOf(pos2)).get(1);

        float hipoteca1 = (int) Solares.get(Posiciones.indexOf(pos1)).get(2);
        float hipoteca2 = (int) Solares.get(Posiciones.indexOf(pos2)).get(2);

        Solar sol1 = new Solar(MonopolyETSE.menu.getBanca(), cas1, "Solar", pos1, valor1, hipoteca1);
        Solar sol2 = new Solar(MonopolyETSE.menu.getBanca(), cas2, "Solar", pos2, valor2, hipoteca2);

        MonopolyETSE.tablero.añadirPropiedad(sol1, pos1);
        MonopolyETSE.tablero.añadirPropiedad(sol2, pos2);

        this.miembros = new ArrayList<>(Arrays.asList(sol1, sol2));
        sol1.setGrupo(this); sol2.setGrupo(this);
        this.colorGrupo = colorGrupo;
        this.codigoColor = codigoColor;
        this.numCasillas = 2;
    }

    /*Constructor para cuando el grupo está formado por TRES SOLARES:
    * Requiere como parámetros las tres casillas miembro y el color del grupo.
     */
    public Grupo(String cas1, String cas2, String cas3, String colorGrupo, String codigoColor) {
        List<Integer> Posiciones = MonopolyETSE.tablero.getSolServTrans().get(0);
        List<List<Object>> Solares = MonopolyETSE.tablero.getSolares();

        int pos1 = 0, pos2 = 0, pos3 = 0;

        for (int i = 0; i < Posiciones.size(); i++) {
            if (cas1 == Solares.get(i).getFirst()) pos1 = i;

            else if (cas2 == Solares.get(i).getFirst()) pos2 = i;

            else if (cas3 == Solares.get(i).getFirst()) pos3 = i;
        }

        float valor1 = (int) Solares.get(Posiciones.indexOf(pos1)).get(1);
        float valor2 = (int) Solares.get(Posiciones.indexOf(pos2)).get(1);
        float valor3 = (int) Solares.get(Posiciones.indexOf(pos3)).get(1);

        float hipoteca1 = (int) Solares.get(Posiciones.indexOf(pos1)).get(2);
        float hipoteca2 = (int) Solares.get(Posiciones.indexOf(pos2)).get(2);
        float hipoteca3 = (int) Solares.get(Posiciones.indexOf(pos3)).get(2);

        Solar sol1 = new Solar(MonopolyETSE.menu.getBanca(), cas1, "Solar", pos1, valor1, hipoteca1);
        Solar sol2 = new Solar(MonopolyETSE.menu.getBanca(), cas2, "Solar", pos2, valor2, hipoteca2);
        Solar sol3 = new Solar(MonopolyETSE.menu.getBanca(), cas3, "Solar", pos3, valor3, hipoteca3);

        MonopolyETSE.tablero.añadirPropiedad(sol1, pos1);
        MonopolyETSE.tablero.añadirPropiedad(sol2, pos2);
        MonopolyETSE.tablero.añadirPropiedad(sol3, pos3);

        this.miembros = new ArrayList<>(Arrays.asList(sol1, sol2, sol3));
        sol1.setGrupo(this); sol2.setGrupo(this); sol3.setGrupo(this);
        this.colorGrupo = colorGrupo;
        this.codigoColor = codigoColor;
        this.numCasillas = 3;
    }

    public Grupo(int tipo, String nombreGrupo, String codigoColor) {
        List<List<Integer>> Posiciones = MonopolyETSE.tablero.getSolServTrans();
        List<List<Object>> Solares = MonopolyETSE.tablero.getSolares();
        this.miembros = new ArrayList<>();

        switch (tipo) {
            // grupo servicios
            case 1 -> {
                List<Integer> serv = Posiciones.get(1);

                float valor = 500000, impuesto = 50000;
                String nomtipo = "Servicios";
                for (Integer i : serv) {
                    String nombre = "Serv" + (serv.indexOf(i) + 1);
                    Servicio s = new Servicio(MonopolyETSE.menu.getBanca(), nombre, nomtipo, i, valor, valor/2);
                    s.setImpuesto(impuesto);
                    MonopolyETSE.tablero.añadirPropiedad(s, i);
                    miembros.add(s);
                    s.setGrupo(this);
                }
            }
            case 2 -> {
                List<Integer> trans = Posiciones.get(2);

                float valor = 500000, impuesto = 250000;
                String nomtipo = "Transporte";
                for (Integer i : trans) {
                    String nombre = "Trans" + (trans.indexOf(i) + 1);
                    Transporte t = new Transporte(MonopolyETSE.menu.getBanca(), nombre, nomtipo, i, valor, valor/2);
                    t.setImpuesto(impuesto);
                    MonopolyETSE.tablero.añadirPropiedad(t, i);
                    miembros.add(t);
                    t.setGrupo(this);
                }
            }
            default -> {
                return;
            }
        }

        this.colorGrupo = nombreGrupo;
        this.codigoColor = codigoColor;
        this.numCasillas = Posiciones.size();
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
        ArrayList<Propiedad> propiedades = jugador.getPropiedades();
        int lenGrupo = miembros.size();
        int count = 0;
        for (Propiedad p : propiedades) {
            if (p instanceof Solar) {
                Solar s = (Solar) p;
                if (s.getGrupo() == this) count++;
            }
        }
        return count == lenGrupo;
    }

}
