package monopoly;

import partida.Jugador;
import partida.Avatar;
import java.util.ArrayList;

public class Suerte extends Carta {
    
    private int idAccion; // Para identificar qué acción ejecutar

    public Suerte(String texto, int idAccion) {
        super(texto);
        this.idAccion = idAccion;
    }

    @Override
    public void accion(Jugador jugador) {
        Juego.consola.imprimir(this.descripcion);
        Avatar avatar = jugador.getAvatar();
        
        switch (idAccion) {
            case 0: // Viaje a Solar19
                Juego.consola.imprimir("\t" + jugador.getNombre() + " avanzó hasta Solar19");
                Casilla solar19 = MonopolyETSE.juego.getTablero().encontrar_casilla("Solar19");
                int tirada = solar19.getPosicion() - avatar.getCasilla().getPosicion();
                if (tirada < 0) tirada += 40;
                avatar.moverAvatar(tirada);
                // Si la casilla destino es propiedad, se gestiona el pago
                solar19.gestionarPago(jugador, MonopolyETSE.juego.getTablero().getBanca(), 0);
                break;

            case 1: // Cárcel directa
                jugador.encarcelar();
                jugador.incrementarVecesEnCarcel();
                break;

            case 2: // Lotería
                jugador.sumarFortuna(1000000);
                Juego.consola.imprimir("\t" + jugador.getNombre() + " recibe 1.000.000€");
                jugador.agregarPremiosInversionesOBote(1000000f);
                break;

            case 3: // Presidente (Pagar a todos)
                // Nota: Asumimos que MonopolyETSE.juego es la instancia estática de Juego
                ArrayList<Jugador> jugadores = MonopolyETSE.juego.getJugadores(); 
                for (Jugador j : jugadores) {
                    if (j != jugador) {
                        if (jugador.getFortuna() < 250000) {
                            Juego.consola.imprimir("\nNo tienes suficiente dinero para pagar, debes hipotecar.");
                            break; 
                        }
                        j.sumarFortuna(250000);
                        Juego.consola.imprimir("\t" + j.getNombre() + " ha recibido 250.000€ de " + jugador.getNombre());
                        jugador.sumarFortuna(-250000);
                    }
                }
                break;

            case 4: // Retroceder 3 casillas
                avatar.moverAvatar(-3);
                Juego.consola.imprimir("\t" + jugador.getNombre() + " ha retrocedido tres casillas.");
                avatar.getCasilla().gestionarPago(jugador, MonopolyETSE.juego.getTablero().getBanca(), 0);
                break;

            case 5: // Multa móvil
                if (jugador.getFortuna() < 150000) {
                    Juego.consola.imprimir("\t\n*** No tienes suficiente dinero para pagar... ***");
                    break;
                }
                jugador.sumarFortuna(-150000);
                jugador.agregarPagoTasasEImpuestos(150000);
                Casilla parking = MonopolyETSE.juego.getTablero().encontrar_casilla("Parking");
                if (parking != null) parking.sumarValor(150000);
                Juego.consola.imprimir("\t" + jugador.getNombre() + " ha pagado una multa de 150.000€.");
                break;

            case 6: // Transporte más cercano
                int posActual = avatar.getCasilla().getPosicion();
                Casilla siguienteTransporte = null;
                
                // Lógica manual para encontrar el siguiente transporte (según tu juego.getTablero() original)
                if (posActual < 5 || posActual > 35) siguienteTransporte = MonopolyETSE.juego.getTablero().encontrar_casilla("Trans1"); // Pos 5
                else if (posActual < 15) siguienteTransporte = MonopolyETSE.juego.getTablero().encontrar_casilla("Trans2"); // Pos 15
                else if (posActual < 25) siguienteTransporte = MonopolyETSE.juego.getTablero().encontrar_casilla("Trans3"); // Pos 25
                else siguienteTransporte = MonopolyETSE.juego.getTablero().encontrar_casilla("Trans1"); // Fallback

                if (siguienteTransporte != null) {
                    int pasos = siguienteTransporte.getPosicion() - posActual;
                    if (pasos < 0) pasos += 40;
                    avatar.moverAvatar(pasos);
                    
                    // Lógica especial: pagar doble si tiene dueño
                    if (siguienteTransporte.getDuenho() == MonopolyETSE.juego.getTablero().getBanca()) {
                        Juego.consola.imprimir("\tCasilla en venta.");
                    } else {
                        // Aquí forzamos el pago doble manualmente o llamamos a gestionarPago
                        // Como tu gestionarPago usa "impuesto" directo para transporte, lo calculamos aquí:
                        float pago = siguienteTransporte.getImpuesto() * 2;
                        if (jugador.getFortuna() >= pago) {
                            jugador.sumarFortuna(-pago);
                            siguienteTransporte.getDuenho().sumarFortuna(pago);
                            Juego.consola.imprimir(jugador.getNombre() + " paga DOBLE (" + pago + "€) a " + siguienteTransporte.getDuenho().getNombre());
                        } else {
                            Juego.consola.imprimir("No tienes dinero suficiente.");
                        }
                    }
                }
                break;
        }
    }
}
