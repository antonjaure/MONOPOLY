package monopoly;

import partida.*;

import java.util.ArrayList;

public class CartaComunidad {
        public ArrayList<String> cartasComunidad;
        int contador = 0;

        public CartaComunidad() {
        cartasComunidad = new ArrayList<>();

        cartasComunidad.add("\tPaga 500.000€ por un fin de semana en un balneario de 5 estrellas.");
        cartasComunidad.add("\tTe investigan por fraude de identidad. Ve a la Cárcel. Ve directamente,\nsin pasar por la casilla de Salida y sin cobrar los 2.000.000€.");
        cartasComunidad.add("\tColócate en la casilla de Salida. Cobra 2.000.000€.");
        cartasComunidad.add("\tDevolución de Hacienda. Cobra 500.000€.");
        cartasComunidad.add("\tRetrocede hasta Solar1 para comprar antigüedades exóticas.");
        cartasComunidad.add("\tVe a Solar20 para disfrutar del San Fermín. Si pasas por la casilla de Salida, cobra 2.000.000€.");

        this.contador = 0;
    }

     public void sacarCartaComunidad() {

        //OBTENEMOS EL JUGADOR ACTUAL CON SU ID DE CARTA DE COMUNIDAD
        int turno = MonopolyETSE.menu.getTurno();
        ArrayList<Jugador> jugadores = MonopolyETSE.menu.getJugadores();
        Jugador jugadorActual = jugadores.get(turno % jugadores.size());
        Avatar avatarActual = jugadorActual.getAvatar();
        //int id = jugadorActual.getCartaComunidadId();

        int id = this.contador;


        if (id >= cartasComunidad.size()) {
            id = id % cartasComunidad.size();
            jugadorActual.setCartaComunidadId(id);
        }

        switch(id){
            case 0:
                System.out.println(cartasComunidad.get(0));
                if(jugadorActual.getFortuna() < 500000){
                    System.out.println("\n\t*** No tienes suficiente dinero para pagar, debes hipotecar para continuar ***");
                    break;
                }
                jugadorActual.sumarFortuna(-500000);
                jugadorActual.agregarPagoTasasEImpuestos(500000);
                Casilla parking = MonopolyETSE.tablero.encontrar_casilla("Parking");
                parking.sumarValor(500000);
                break;
            case 1:
                System.out.println(cartasComunidad.get(1));
                jugadorActual.encarcelar();
                jugadorActual.incrementarVecesEnCarcel();
                break;
            case 2:
                System.out.println(cartasComunidad.get(2));
                avatarActual.moverAvatar("Salida");
                jugadorActual.sumarFortuna(2000000);                
                System.out.println("\t" + jugadorActual.getNombre() + " ha cobrado 2.000.000€ por pasar por la casilla de Salida.");
                break;
            case 3:
                System.out.println(cartasComunidad.get(3));
                jugadorActual.sumarFortuna(500000);
                jugadorActual.agregarPremiosInversionesOBote(500000);
                System.out.println("\t" + jugadorActual.getNombre() + " recibe 500.000€");
                break;
            case 4:
                System.out.println(cartasComunidad.get(4));
                avatarActual.moverAvatar("Solar1");
                System.out.println("\t" + jugadorActual.getNombre() + "retrocedió hasta Solar1");
                avatarActual.getCasilla().gestionarPago(jugadorActual, MonopolyETSE.menu.getBanca(), 0);
                break;
            case 5:
                System.out.println(cartasComunidad.get(5));
                System.out.println("\t" + jugadorActual.getNombre() + " avanzó hasta Solar20");
                int tirada = MonopolyETSE.tablero.encontrar_casilla("Solar20").getPosicion() - avatarActual.getCasilla().getPosicion();
                avatarActual.moverAvatar(tirada); // usamos la version con tirada en lugar de con el nombre de la casilla, asi tenemos en cuenta si pasa por la casilla
                avatarActual.getCasilla().gestionarPago(jugadorActual, MonopolyETSE.menu.getBanca(), 0);
                break;
                
        }
        //ACTUALIZAMOS EL ID DE LA CARTA DE COMUNIDAD DEL JUGADOR
        // jugadorActual.setCartaComunidadId(id + 1);
        this.contador += 1;
    }
}

