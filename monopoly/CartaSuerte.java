package monopoly;

import partida.*;

import java.util.ArrayList;

public class CartaSuerte {
    private ArrayList<String> cartasSuerte;
    


    /*HAY QUE METER SACARCARTAS SUERTE Y COMUNIDAD AL CAER EN LA CASILLAS
     * EN MENU O DONDE SEA.
     */
    public CartaSuerte() {
        cartasSuerte = new ArrayList<>();

        cartasSuerte.add("Decides hacer un viaje de placer. Avanza hasta Solar19. Si pasas por la casilla de Salida, cobra 2.000.000€.");
        cartasSuerte.add("Los acreedores te persiguen por impago. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar los 2.000.000€.");
        cartasSuerte.add("¡Has ganado el bote de la lotería! Recibe 1.000.000€. ");
        cartasSuerte.add("Has sido elegido presidente de la junta directiva. Paga a cada jugador 250.000€. ");
        cartasSuerte.add("¡Hora punta de tráfico! Retrocede tres casillas.");
        cartasSuerte.add("Te multan por usar el móvil mientras conduces. Paga 150.000€.");
        cartasSuerte.add("Avanza hasta la casilla de transporte más cercana. Si no tiene dueño, puedes comprarla. Si tiene dueño, paga al dueño el doble de la operación indicada.");
    }

    public void sacarCartaSuerte() {

        //OBTENEMOS EL JUGADOR ACTUAL CON SU ID DE CARTA DE SUERTE
        int turno = MonopolyETSE.menu.getTurno();
        ArrayList<Jugador> jugadores = MonopolyETSE.menu.getJugadores();
        Jugador jugadorActual = jugadores.get(turno % jugadores.size());
        Avatar avatarActual = jugadorActual.getAvatar();
        int posicionActual = avatarActual.getCasilla().getPosicion();
        int id = jugadorActual.getCartaSuerteId();


        if (id >= cartasSuerte.size()) {
            id = id % cartasSuerte.size();
            jugadorActual.setCartaSuerteId(id);
        }

        switch(id){
            case 0:
                System.out.println(cartasSuerte.get(0));
                Casilla solar19 = MonopolyETSE.tablero.encontrar_casilla("Solar19");
                avatarActual.getCasilla().eliminarAvatar(avatarActual);
                solar19.anhadirAvatar(avatarActual);
                if(posicionActual > solar19.getPosicion()){
                    jugadorActual.sumarFortuna(2000000);
                    System.out.println(jugadorActual.getNombre() + " ha cobrado 2.000.000€ por pasar por la casilla de Salida.\n");
                }
                solar19.gestionarPago(jugadorActual, MonopolyETSE.tablero.getBanca(), 0);
                break;

            case 1:
            //?¿?¿ HAY QUE GESTIONAR PAGO O ALGO PARECIDO AL IR A LA CARCEL?¿?¿?¿
                System.out.println(cartasSuerte.get(1));
                Casilla carcel = MonopolyETSE.tablero.encontrar_casilla("Cárcel");
                avatarActual.getCasilla().eliminarAvatar(avatarActual);
                carcel.anhadirAvatar(avatarActual);
                jugadorActual.setEnCarcel(true);
                jugadorActual.setDoblesConsecutivos(0);
                jugadorActual.setTiradasCarcel(0);
                System.out.println(jugadorActual.getNombre() + " ha sido enviado a la cárcel.\n");
                break;

            case 2:
                System.out.println(cartasSuerte.get(2));
                jugadorActual.sumarFortuna(1000000);
                System.out.println(jugadorActual.getNombre() + "recibe 1.000.000€");
                break;

            case 3:
                System.out.println(cartasSuerte.get(3));
                ArrayList<Jugador> jugadoresActivos = MonopolyETSE.menu.getJugadores();
                for (Jugador j : jugadoresActivos) {
                    if (j != jugadorActual) {
                        if(jugadorActual.getFortuna() < 250000){
                            System.out.println("\nNo tienes suficiente dinero para pagar, debes hipotecar para continuar\n");
                            break;
                        }
                        j.sumarFortuna(250000);
                        System.out.println(j.getNombre() + " ha recibido 250.000€ de " + jugadorActual.getNombre() + ".\n");  
                        jugadorActual.sumarFortuna(-250000);
                    }
                }
                break;
            
            case 4:
            
                System.out.println(cartasSuerte.get(4));
                Casilla destino =  MonopolyETSE.tablero.avanzarCasillas(avatarActual.getCasilla(), -3);
                avatarActual.getCasilla().eliminarAvatar(avatarActual);
                destino.anhadirAvatar(avatarActual);
                System.out.println(jugadorActual.getNombre() + " ha retrocedido tres casillas.\n");
                destino.gestionarPago(jugadorActual, MonopolyETSE.tablero.getBanca(), 0);
                break;

            case 5:
                System.out.println(cartasSuerte.get(5));
                if(jugadorActual.getFortuna() < 150000){
                    System.out.println("\nNo tienes suficiente dinero para pagar, debes hipotecar para continuar\n");
                    break;
                }
                jugadorActual.sumarFortuna(-150000);
                Casilla parking = MonopolyETSE.tablero.encontrar_casilla("Parking");
                parking.sumarValor(150000);
                System.out.println(jugadorActual.getNombre() + " ha pagado una multa de 150.000€.\n");
                break;

            case 6:
                System.out.println(cartasSuerte.get(6));
                Casilla siguienteTransporte = null;

                if(posicionActual == 7 ) {
                    siguienteTransporte = MonopolyETSE.tablero.encontrar_casilla("Transporte2");
                    avatarActual.getCasilla().eliminarAvatar(avatarActual);
                    siguienteTransporte.anhadirAvatar(avatarActual);

                }else if(posicionActual == 22 ) {
                    siguienteTransporte = MonopolyETSE.tablero.encontrar_casilla("Transporte3");
                    avatarActual.getCasilla().eliminarAvatar(avatarActual);
                    siguienteTransporte.anhadirAvatar(avatarActual);
                    

                }else if (posicionActual == 36 ) {
                    siguienteTransporte = MonopolyETSE.tablero.encontrar_casilla("Transporte1");
                    jugadorActual.sumarFortuna(2000000);
                    System.out.println(jugadorActual.getNombre() + " ha cobrado 2.000.000€ por pasar por la casilla de Salida.\n");
                    avatarActual.getCasilla().eliminarAvatar(avatarActual);
                    siguienteTransporte.anhadirAvatar(avatarActual);
                } else {
                    System.out.println("Error: La posición actual no es válida para esta carta.");
                }
                if(siguienteTransporte == null){
                    System.out.println("Error: No se ha podido encontrar la siguiente casilla de transporte.");
                }else{
                    if(siguienteTransporte.getDuenho() == MonopolyETSE.menu.getBanca()) {
                        System.out.println("\tCasilla en venta.");
                        return;
                    }else{
                        float pago = siguienteTransporte.getImpuesto() * 2;

                        if(jugadorActual.getFortuna() < pago){
                            System.out.println("\nNo tienes suficiente dinero para pagar, debes hipotecar para continuar\n");
                            break;
                        }
                        jugadorActual.sumarFortuna(-pago);
                        siguienteTransporte.getDuenho().sumarFortuna(pago);
                        System.out.println(jugadorActual.getNombre() + " ha pagado " + pago + "€ a " + siguienteTransporte.getDuenho().getNombre() + ".\n");
                        break;
                    }

                }
                break;
        }
        
        //ACTUALIZAMOS EL ID DE LA CARTA DE SUERTE DEL JUGADOR
        jugadorActual.setCartaSuerteId(id + 1);
        return;
    }
}
