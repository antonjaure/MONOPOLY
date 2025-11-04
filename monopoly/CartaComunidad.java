package monopoly;

import partida.*;

import java.util.ArrayList;

public class CartaComunidad {
        private ArrayList<String> cartasComunidad;

        public CartaComunidad() {
        cartasComunidad = new ArrayList<>();

        cartasComunidad.add("Paga 500.000€ por un fin de semana en un balneario de 5 estrellas.");
        cartasComunidad.add("Te investigan por fraude de identidad. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y\r\n" + //
                        "sin cobrar los 2.000.000€.");
        cartasComunidad.add("Colócate en la casilla de Salida. Cobra 2.000.000€.");
        cartasComunidad.add("Devolución de Hacienda. Cobra 500.000€.");
        cartasComunidad.add("Retrocede hasta Solar1 para comprar antigüedades exóticas.");
        cartasComunidad.add("Ve a Solar20 para disfrutar del San Fermín. Si pasas por la casilla de Salida, cobra 2.000.000€.");
    }

     public void sacarCartaComunidad() {

        //OBTENEMOS EL JUGADOR ACTUAL CON SU ID DE CARTA DE COMUNIDAD
        int turno = MonopolyETSE.menu.getTurno();
        ArrayList<Jugador> jugadores = MonopolyETSE.menu.getJugadores();
        Jugador jugadorActual = jugadores.get(turno % jugadores.size());
        Avatar avatarActual = jugadorActual.getAvatar();
        int posicionActual = avatarActual.getCasilla().getPosicion();
        int id = jugadorActual.getCartaComunidadId();


        if (id >= cartasComunidad.size()) {
            id = id % cartasComunidad.size();
            jugadorActual.setCartaComunidadId(id);
        }

        switch(id){
            case 0:
                System.out.println(cartasComunidad.get(0));
                if(jugadorActual.getFortuna() < 500000){
                    //HIPOTECA
                    System.out.println("\nNo tienes suficiente dinero para pagar, debes hipotecar para continuar\n");
                    break;//QUITAR BREAK CUANDO SE HAGA HIPOTECAR
                }

                jugadorActual.sumarFortuna(500000);
                Casilla parking = MonopolyETSE.tablero.encontrar_casilla("Parking");
                parking.sumarValor(500000);
                System.out.println(jugadorActual.getNombre() + " ha pagado una multa de 500.000€.\n");
                
                break;

                

            case 1:
                System.out.println(cartasComunidad.get(1));
                Casilla carcel = MonopolyETSE.tablero.encontrar_casilla("Cárcel");
                avatarActual.getCasilla().eliminarAvatar(avatarActual);
                carcel.anhadirAvatar(avatarActual);
                jugadorActual.setEnCarcel(true);
                jugadorActual.setDoblesConsecutivos(0);
                jugadorActual.setTiradasCarcel(0);
                System.out.println(jugadorActual.getNombre() + " ha sido enviado a la cárcel.\n");
                break;

            case 2:
                System.out.println(cartasComunidad.get(2));
                Casilla salida = MonopolyETSE.tablero.encontrar_casilla("Salida");
                avatarActual.getCasilla().eliminarAvatar(avatarActual);
                salida.anhadirAvatar(avatarActual);
                jugadorActual.sumarFortuna(2000000);
                System.out.println(jugadorActual.getNombre() + " ha cobrado 2.000.000€ por pasar por la casilla de Salida.\n");
                
                break;

               

            case 3:
                System.out.println(cartasComunidad.get(3));
                jugadorActual.sumarFortuna(500000);
                System.out.println(jugadorActual.getNombre() + "recibe 500.000€");
                break;
            
            case 4:

                System.out.println(cartasComunidad.get(4));
                Casilla solar1 = MonopolyETSE.tablero.encontrar_casilla("Solar1");
                avatarActual.getCasilla().eliminarAvatar(avatarActual);
                solar1.anhadirAvatar(avatarActual);
                System.out.println(jugadorActual + "retrocede hasta Solar1");
                break;

            case 5:
                System.out.println(cartasComunidad.get(5));
                Casilla solar20 = MonopolyETSE.tablero.encontrar_casilla("Solar20");
                avatarActual.getCasilla().eliminarAvatar(avatarActual);
                solar20.anhadirAvatar(avatarActual);
                System.out.println(jugadorActual + "avanza hasta Solar20");
                if(jugadorActual.getAvatar().getCasilla().getPosicion() > 34){
                    jugadorActual.sumarFortuna(2000000);
                    System.out.println(jugadorActual.getNombre() + " ha cobrado 2.000.000€ por pasar por la casilla de Salida.\n");
                }
                break;
                
        }
        //ACTUALIZAMOS EL ID DE LA CARTA DE COMUNIDAD DEL JUGADOR
        jugadorActual.setCartaComunidadId(id + 1);
        return;
    }
}

