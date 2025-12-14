
package monopoly;

import partida.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Grupo {
    // Atributos
    private ArrayList<Propiedad> miembros; 
    private String colorGrupo;             
    private String codigoColor;            
    private int numCasillas; 
    private float rentabilidad; 

    // Constructor para Grupos Generales (Servicios / Transportes)
    public Grupo(int tipo, String nombreGrupo, String codigoColor) {
        this.miembros = new ArrayList<>();
        this.colorGrupo = nombreGrupo;
        this.codigoColor = codigoColor;
        this.numCasillas = 0; 

        Tablero tablero = MonopolyETSE.juego.getTablero();
        
        if (tipo == 1) { // Servicios
            List<Integer> posiciones = tablero.getSolServTrans().get(1); 
            float precio = 750000; // Valor estándar
            
            for (Integer pos : posiciones) {
                String nombre = (pos == 12) ? "Electricidad" : "Agua";
                
                Servicio s = new Servicio(tablero.getBanca(), nombre, "Servicios", pos, precio, precio/2); 
                
                s.setGrupo(this);
                this.miembros.add(s);
                tablero.añadirPropiedad(s, pos);
            }
        } else if (tipo == 2) { // Transportes
            List<Integer> posiciones = tablero.getSolServTrans().get(2); 
            float precio = 500000; // Valor estándar
            int contador = 1;
            
            for (Integer pos : posiciones) {
                String nombre = "Transporte" + contador;
                
                Transporte t = new Transporte(tablero.getBanca(), nombre, "Transporte", pos, precio, precio/2); 
                
                t.setGrupo(this);
                this.miembros.add(t);
                tablero.añadirPropiedad(t, pos);
                contador++;
            }
        }
        this.numCasillas = miembros.size();
    }

    // Constructor para 2 Solares
    public Grupo(String s1, String s2, String color, String codigoColor) {
        this(new String[]{s1, s2}, color, codigoColor);
    }

    // Constructor para 3 Solares
    public Grupo(String s1, String s2, String s3, String color, String codigoColor) {
        this(new String[]{s1, s2, s3}, color, codigoColor);
    }

    // Constructor privado unificado
    private Grupo(String[] nombresSolares, String color, String codigoColor) {
        this.miembros = new ArrayList<>();
        this.colorGrupo = color;
        this.codigoColor = codigoColor;
        this.numCasillas = nombresSolares.length;
        
        Tablero tablero = MonopolyETSE.juego.getTablero();
        List<List<Object>> datosSolares = tablero.getSolares();
        
        List<Integer> posicionesSolares = tablero.getSolServTrans().get(0);

        for (String nombreBuscado : nombresSolares) {
            int indiceEncontrado = -1;
            
            for (int i = 0; i < datosSolares.size(); i++) {
                String nombreEnLista = (String) datosSolares.get(i).get(0);
                if (nombreEnLista.equals(nombreBuscado)) {
                    indiceEncontrado = i;
                    break;
                }
            }

            if (indiceEncontrado == -1) {
                Juego.consola.imprimir("ERROR: No se encontró el solar: " + nombreBuscado);
                continue; 
            }

            int posicionTablero = posicionesSolares.get(indiceEncontrado);
            List<Object> datos = datosSolares.get(indiceEncontrado);
            
            // Casting seguro a float
            float precio = ((Number) datos.get(1)).floatValue(); 
            float hipoteca = ((Number) datos.get(2)).floatValue();

            Solar solar = new Solar(tablero.getBanca(), nombreBuscado, "Solar", posicionTablero, precio, hipoteca);
            
            solar.setGrupo(this); // Asignamos el grupo aquí
            
            this.miembros.add(solar);
            tablero.añadirPropiedad(solar, posicionTablero);
        }
    }


    public boolean esDuenhoGrupo(Jugador jugador) {
        if (jugador == null) return false;
        
        int contador = 0;
        for (Propiedad p : miembros) {
            if (p.getDuenho() == jugador) {
                contador++;
            }
        }
        // Si tiene tantas como el tamaño del grupo, es dueño de todo
        return contador == miembros.size();
    }
    

    // Getters y Setters básicos
    public ArrayList<Propiedad> getMiembros() { return miembros; }
    public String getColorGrupo() { return colorGrupo; }
    public String getCodigoColor() { return codigoColor; }
    public float getRentabilidad() { return rentabilidad; }
    public void setRentabilidad(float r) { this.rentabilidad = r; }
    public int numCasillas() { return numCasillas; }
}