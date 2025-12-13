package monopoly;

import partida.Jugador;
import partida.Avatar;

public class CajaComunidad extends Carta {
    
    private int idAccion;

    public CajaComunidad(String texto, int idAccion) {
        super(texto);
        this.idAccion = idAccion;
    }

    @Override
    public void accion(Jugador jugador) {
        Juego.consola.imprimir(this.descripcion);
        Avatar avatar = jugador.getAvatar();

        switch (idAccion) {
            case 0: // Balneario
                if (jugador.getFortuna() < 500000) {
                    Juego.consola.imprimir("\n\t*** No tienes suficiente dinero para pagar... ***");
                } else {
                    jugador.sumarFortuna(-500000);
                    jugador.agregarPagoTasasEImpuestos(500000);
                    Casilla parking = MonopolyETSE.juego.getTablero().encontrar_casilla("Parking");
                    if (parking instanceof Parking) ((Parking) parking).sumarValor(500000);
                }
                break;

            case 1: // Investigado (Cárcel)
                jugador.encarcelar();
                jugador.incrementarVecesEnCarcel();
                break;

            case 2: // Ir a Salida
                // Mover directo a salida
                avatar.moverAvatar("Salida"); 
                // El cobro de 2M ya lo suele hacer moverAvatar si pasa por salida
                Juego.consola.imprimir("\t" + jugador.getNombre() + " ha cobrado 2.000.000€ por pasar por la casilla de Salida.");
                break;

            case 3: // Hacienda devuelve
                jugador.sumarFortuna(500000);
                jugador.agregarPremiosInversionesOBote(500000);
                Juego.consola.imprimir("\t" + jugador.getNombre() + " recibe 500.000€");
                break;

            case 4: // Retroceder a Solar1
                Casilla solar1 = MonopolyETSE.juego.getTablero().encontrar_casilla("Solar1");
                // Mover manualmente para evitar cobro de salida si retrocede
                avatar.getCasilla().eliminarAvatar(avatar);
                solar1.anhadirAvatar(avatar);
                avatar.setLugar(solar1);
                
                Juego.consola.imprimir("\t" + jugador.getNombre() + " retrocedió hasta Solar1");
                solar1.gestionarPago(jugador, MonopolyETSE.juego.getTablero().getBanca(), 0);
                break;

            case 5: // Ir a Solar20
                Juego.consola.imprimir("\t" + jugador.getNombre() + " avanzó hasta Solar20");
                Casilla solar20 = MonopolyETSE.juego.getTablero().encontrar_casilla("Solar20");
                int tirada = solar20.getPosicion() - avatar.getCasilla().getPosicion();
                if (tirada < 0) tirada += 40; // Asegurar positivo
                avatar.moverAvatar(tirada);
                solar20.gestionarPago(jugador, MonopolyETSE.juego.getTablero().getBanca(), 0);
                break;
        }
    }
}