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
                System.out.println("\t" + jugadorActual + "avanzó hasta Solar19");
                int tirada = MonopolyETSE.tablero.encontrar_casilla("Solar19").getPosicion() - avatarActual.getCasilla().getPosicion();
                avatarActual.moverAvatar(tirada); // usamos la version con tirada en lugar de con el nombre de la casilla, asi tenemos en cuenta si pasa por la casilla
                avatarActual.getCasilla().gestionarPago(jugadorActual, MonopolyETSE.menu.getBanca(), 0);
                break;
            case 1:
                System.out.println(cartasSuerte.get(1));
                jugadorActual.encarcelar();
                break;
            case 2:
                System.out.println(cartasSuerte.get(2));
                jugadorActual.sumarFortuna(1000000);
                System.out.println("\t" + jugadorActual.getNombre() + "recibe 1.000.000€");
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
                        System.out.println("\t" + j.getNombre() + " ha recibido 250.000€ de " + jugadorActual.getNombre() + ".\n");
                        jugadorActual.sumarFortuna(-250000);
                    }
                }
                break;
            case 4:
                System.out.println(cartasSuerte.get(4));
                avatarActual.moverAvatar(-3);
                System.out.println("\t" + jugadorActual.getNombre() + " ha retrocedido tres casillas.\n");
                avatarActual.getCasilla().gestionarPago(jugadorActual, MonopolyETSE.tablero.getBanca(), 0);
                break;
            case 5:
                System.out.println(cartasSuerte.get(5));
                if(jugadorActual.getFortuna() < 150000){
                    System.out.println("\t\n*** No tienes suficiente dinero para pagar, debes hipotecar para continuar. ***");
                    break;
                }
                jugadorActual.sumarFortuna(-150000);
                System.out.println("\t" + jugadorActual.getNombre() + " ha pagado una multa de 150.000€.\n");
                break;
            case 6:
                System.out.println(cartasSuerte.get(6));
                Casilla siguienteTransporte = null;
                ArrayList<ArrayList<Casilla>> tablero = MonopolyETSE.tablero.getPosiciones();
                ArrayList<Casilla> todas = new ArrayList<>(); // lista para recorrer circularmente

                for (ArrayList<Casilla> calle : tablero) {
                    todas.addAll(calle);
                }
                int start = todas.indexOf(avatarActual.getCasilla());
                int n = todas.size();
                for (int i = 1; i < n + 1; i++) {
                    int idx = (start + i) % n;   // índice circular
                    Casilla c = todas.get(idx);

                    if (c.getTipo().equals("Transporte")) {
                        siguienteTransporte = c;
                        break;
                    }
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
                        Jugador duenho = siguienteTransporte.getDuenho();
                        duenho.sumarFortuna(pago);
                        if(duenho != MonopolyETSE.menu.getBanca()){
                            siguienteTransporte.setRentabilidad(siguienteTransporte.getRentabilidad() + pago);
                        }
                        System.out.println(jugadorActual.getNombre() + " ha pagado " + pago + "€ a " + siguienteTransporte.getDuenho().getNombre() + ".\n");
                        // Actualizar estadísticas
                        jugadorActual.agregarPagoDeAlquileres(pago);
                        duenho.agregarCobroDeAlquileres(pago);
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
