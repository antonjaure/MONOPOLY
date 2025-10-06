package partida;

import monopoly.*;
import java.util.ArrayList;

public class ComandoLanzarDados {
    private GestorDados gestorDados;
    private Jugador jugadorActual;
    private Tablero tablero;
    private static float boteParking = 0; // Static para que sea compartido entre todas las instancias

    public ComandoLanzarDados(GestorDados gestorDados, Jugador jugadorActual, Tablero tablero) {
        this.gestorDados = gestorDados;
        this.jugadorActual = jugadorActual;
        this.tablero = tablero;
    }

    public void ejecutar() {
        // 1. Lanzar los dados
        ResultadoTirada resultado = gestorDados.lanzarDados();

        // 2. Obtener información del jugador y avatar
        Avatar avatar = jugadorActual.getAvatar();
        Casilla casillaActual = avatar.getCasilla();
        String nombreCasillaActual = casillaActual.getNombre();

        // 3. Mover el avatar
        ArrayList<ArrayList<Casilla>> todasLasCasillas = tablero.getPosiciones();
        avatar.moverAvatar(todasLasCasillas, resultado.getTotal());

        // 4. Obtener la nueva posición después del movimiento
        Casilla casillaNueva = avatar.getCasilla();
        String nombreCasillaNueva = casillaNueva.getNombre();

        // 5. Mostrar movimiento
        System.out.print("El avatar " + avatar.getId() + " avanza " + resultado.getTotal() +
                " posiciones, desde " + nombreCasillaActual + " hasta " + nombreCasillaNueva);

        // 6. Aplicar y mostrar consecuencias según el tipo de casilla
        aplicarConsecuenciaCasilla(casillaNueva, resultado);

        // 7. Aplicar regla de tres dobles
        if (resultado.irCarcel()) {
            enviarACarcel(jugadorActual);
            System.out.println("\nEl avatar se coloca en la casilla de Cárcel.");
        }
    }

    private void aplicarConsecuenciaCasilla(Casilla casilla, ResultadoTirada resultado) {
        String tipoCasilla = casilla.getTipo();
        Jugador propietario = casilla.getDuenho();

        switch (tipoCasilla) {
            case "Solar":
                manejarSolar(casilla, propietario);
                break;
            case "Servicios":
                manejarServicio(casilla, propietario, resultado);
                break;
            case "Transporte":
                manejarTransporte(casilla, propietario);
                break;
            case "IrCarcel":
                enviarACarcel(jugadorActual);
                System.out.println(". El avatar se coloca en la casilla de Cárcel.");
                break;
            case "Parking":
                manejarParking();
                break;
            case "Impuestos":
                manejarImpuesto();
                break;
            case "Suerte":
            case "Comunidad":
                // No se realiza ninguna acción
                System.out.println(".");
                break;
            default:
                System.out.println(".");
        }
    }

    private void manejarSolar(Casilla solar, Jugador propietario) {
        // Si no hay propietario o es el propio jugador, no hace nada
        if (propietario == null || propietario == jugadorActual) {
            System.out.println(".");
            return;
        }

        // Calcular alquiler (usamos valores base del documento)
        float alquiler = calcularAlquilerSolar(solar.getNombre());

        // Verificar si el jugador tiene suficiente dinero
        if (jugadorActual.getFortuna() >= alquiler) {
            // Pagar alquiler
            jugadorActual.sumarFortuna(-alquiler);
            propietario.sumarFortuna(alquiler);
            System.out.println(". Se han pagado " + String.format("%,.0f", alquiler) + "€ de alquiler.");
        } else {
            // No tiene dinero suficiente
            System.out.println(". El jugador no tiene dinero suficiente para pagar " +
                    String.format("%,.0f", alquiler) + "€ de alquiler. " +
                    "Debe hipotecar alguna propiedad o declararse en bancarrota.");
        }
    }

    private void manejarServicio(Casilla servicio, Jugador propietario, ResultadoTirada resultado) {
        if (propietario == null || propietario == jugadorActual) {
            System.out.println(".");
            return;
        }

        // 4 × valor dados × factor servicio (50.000€)
        float pago = 4 * resultado.getTotal() * 50000;

        if (jugadorActual.getFortuna() >= pago) {
            jugadorActual.sumarFortuna(-pago);
            propietario.sumarFortuna(pago);
            System.out.println(". Se han pagado " + String.format("%,.0f", pago) + "€ por servicio.");
        } else {
            System.out.println(". El jugador no tiene dinero suficiente. Debe hipotecar o declarar bancarrota.");
        }
    }

    private void manejarTransporte(Casilla transporte, Jugador propietario) {
        if (propietario == null || propietario == jugadorActual) {
            System.out.println(".");
            return;
        }

        // Alquiler fijo de 250.000€
        float alquiler = 250000;

        if (jugadorActual.getFortuna() >= alquiler) {
            jugadorActual.sumarFortuna(-alquiler);
            propietario.sumarFortuna(alquiler);
            System.out.println(". Se han pagado " + String.format("%,.0f", alquiler) + "€ por transporte.");
        } else {
            System.out.println(". El jugador no tiene dinero suficiente. Debe hipotecar o declarar bancarrota.");
        }
    }

    private void manejarParking() {
        if (boteParking > 0) {
            jugadorActual.sumarFortuna(boteParking);
            System.out.println(". El jugador " + jugadorActual.getNombre() + " recibe " +
                    String.format("%,.0f", boteParking) + "€.");
            boteParking = 0;
        } else {
            System.out.println(". No hay bote acumulado en el Parking.");
        }
    }

    private void manejarImpuesto() {
        float impuesto = 2000000; // 2.000.000€ fijos

        if (jugadorActual.getFortuna() >= impuesto) {
            jugadorActual.sumarFortuna(-impuesto);
            boteParking += impuesto;
            System.out.println(". El jugador paga " + String.format("%,.0f", impuesto) +
                    "€ que se depositan en el Parking.");
        } else {
            System.out.println(". El jugador no tiene dinero suficiente para pagar impuestos. " +
                    "Debe hipotecar o declarar bancarrota.");
        }
    }

    private float calcularAlquilerSolar(String nombreSolar) {
        // Valores de alquiler base según el documento
        switch (nombreSolar) {
            case "RONDA DE VALENCIA": return 20000;
            case "PLAZA LAVAPIÉS": return 40000;
            case "GLORIETA CUATRO CAMINOS": return 60000;
            case "AVENIDA DE REINA VICTORIA": return 60000;
            case "CALLE BRAVO MURILLO": return 80000;
            case "GLORIETA DE BILBAO": return 100000;
            case "CALLE ALBERTO AGUILERA": return 100000;
            case "CALLE FUENCARRAL": return 120000;
            case "AVENIDA FELIPE II": return 140000;
            case "CALLE VELÁZQUEZ": return 140000;
            case "CALLE SERRANO": return 160000;
            case "AVENIDA DE AMÉRICA": return 180000;
            case "CALLE MARÍA MOLINA": return 180000;
            case "CALLE CEA BERMÚDEZ": return 200000;
            case "AVENIDA DE LOS REYES CATÓLICOS": return 220000;
            case "CALLE BAILÉN": return 220000;
            case "PLAZA DE ESPAÑA": return 240000;
            case "PUERTA DEL SOL": return 260000;
            case "CALLE DE ALCALÁ": return 260000;
            case "GRAN VÍA": return 280000;
            case "PASEO DE LA CASTELLANA": return 350000;
            case "PASEO DEL PRADO": return 500000;
            default: return 150000; // Valor por defecto para otros solares
        }
    }

    private void enviarACarcel(Jugador jugador) {
        Casilla carcel = tablero.encontrar_casilla("Cárcel");
        Avatar avatar = jugador.getAvatar();

        if (carcel != null && avatar != null) {
            // Mover avatar directamente a la cárcel
            avatar.setLugar(carcel);
            jugador.setEnCarcel(true);
            jugador.setTiradasCarcel(0);
        }
    }

    public void ejecutar(String argumentos) {
        if (argumentos != null && argumentos.contains("+")) {
            try {
                String[] partes = argumentos.split("\\+");
                int valor1 = Integer.parseInt(partes[0].trim());
                int valor2 = Integer.parseInt(partes[1].trim());

                gestorDados.forzarTirada(valor1, valor2);
                ejecutar(); // Ejecutar con los valores forzados

            } catch (NumberFormatException e) {
                System.out.println("Error: Los valores deben ser números entre 1 y 6");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            ejecutar();
        }
    }

    // Getter para el bote del parking (útil para testing)
    public static float getBoteParking() {
        return boteParking;
    }

    // Setter para el jugador actual (cuando cambia el turno)
    public void setJugadorActual(Jugador jugadorActual) {
        this.jugadorActual = jugadorActual;
    }
}