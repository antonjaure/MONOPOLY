package monopoly;

import partida.Jugador;

import java.util.*;

public class Solar extends Propiedad {
    private ArrayList<Edificio> edificios;
    private HashMap<String, Float> valores = new HashMap<>(); // valores de compra de cada tipo de edificio
    private HashMap<String, Float> alquileres = new HashMap<>(); // alquileres de cada tipo de edificio

    public Solar(Jugador duenho, String nombre, String tipo, int posicion, float valor, float hipoteca) {
        super(duenho, nombre, tipo, posicion, valor, hipoteca);
        this.edificios = new ArrayList<>();
        asignarAlquileres();
        asignarPrecioEdificios();
    }

    @Override
    public String toString() {
        return super.toString() +
                "\n\tEdificios: " + edificios +
                "\n\tGrupo: " + getGrupo() +
                "\n\tAlquileres edificios: " + alquileres +
                "\n\tValores edificios: " + valores;
    }

    private void asignarPrecioEdificios() {
        List<List<Object>> PreciosConstruccion = MonopolyETSE.juego.getTablero().getPreciosConstruccion();
        List<List<Integer>> SolServTrans = MonopolyETSE.juego.getTablero().getSolServTrans();
        int pos = this.getPosicion();

        float valorCasa = (int) PreciosConstruccion.get(SolServTrans.getFirst().indexOf(pos)).get(1);
        float valorHotel = (int) PreciosConstruccion.get(SolServTrans.getFirst().indexOf(pos)).get(2);
        float valorPiscina = (int) PreciosConstruccion.get(SolServTrans.getFirst().indexOf(pos)).get(3);
        float valorPista = (int) PreciosConstruccion.get(SolServTrans.getFirst().indexOf(pos)).get(4);

        this.setValorCasa(valorCasa);
        this.setValorHotel(valorHotel);
        this.setValorPiscina(valorPiscina);
        this.setValorPista(valorPista);
    }

    private void asignarAlquileres() {
        List<List<Object>> Alquileres = MonopolyETSE.juego.getTablero().getAlquileres();
        List<List<Integer>> SolServTrans = MonopolyETSE.juego.getTablero().getSolServTrans();
        int pos = this.getPosicion();

        float impuesto = (int) Alquileres.get(SolServTrans.getFirst().indexOf(pos)).get(1);
        float alquilerCasa = (int) Alquileres.get(SolServTrans.getFirst().indexOf(pos)).get(2);
        float alquilerHotel = (int) Alquileres.get(SolServTrans.getFirst().indexOf(pos)).get(3);
        float alquilerPiscina = (int) Alquileres.get(SolServTrans.getFirst().indexOf(pos)).get(4);
        float alquilerPista = (int) Alquileres.get(SolServTrans.getFirst().indexOf(pos)).get(5);

        this.setImpuesto(impuesto);
        this.setAlquilerCasa(alquilerCasa);
        this.setAlquilerHotel(alquilerHotel);
        this.setAlquilerPiscina(alquilerPiscina);
        this.setAlquilerPista(alquilerPista);
    }

    public void edificar(String tipo) {
        Jugador propietario = this.getDuenho();
        int tamGrupo = getGrupo().numCasillas();
        ArrayList<Propiedad> prop = propietario.getPropiedades();
        int count = 0;
        // se comprueba si todo el grupo es del mismo jugador
        for (Propiedad p : prop) {
            if (p instanceof Solar s) {
                if (s.getGrupo() == getGrupo()) count += 1;
            }
        }
        if (count < tamGrupo) {
            Juego.consola.imprimir("\t*** No se puede edificar. Necesitas adquirir todo el grupo primero. ***");
            return;
        }
        if (this.getEdificios() == null) setEdificios(new ArrayList<>());
        // se distingue el tipo de edificio
        switch (tipo) {
            case "casa":
                if (this.valores.get("casa") > propietario.getFortuna()) {
                    Juego.consola.imprimir("\t*** No tienes suficiente liquidez. Coste por casa: " + valores.get("casa") + " ***");
                    return;
                }
                if (!construirCasa()) return;
                String Cnom = "casa-" + (MonopolyETSE.juego.getTablero().getCasas().size()+1); // id de la casa
                Casa casa = new Casa(Cnom, this, getValorCasa(), getAlquilerCasa());
                Jugador actual1 = this.getAvatares().getLast().getJugador();
                // se paga la casa
                actual1.sumarFortuna(-getValorCasa());
                actual1.agregarDineroInvertido(getValorCasa());
                // se contruye la casa
                getEdificios().add(casa);
                float impCasa = getImpuesto() + getAlquilerCasa();
                setImpuesto(impCasa);
                MonopolyETSE.juego.getTablero().getCasas().add(casa);
                break;
            case "hotel":
                if (this.valores.get("hotel") > propietario.getFortuna()) {
                    Juego.consola.imprimir("\t*** No tienes suficiente liquidez. Coste por hotel: " + valores.get("hotel") + " ***");
                    return;
                }
                if (!construirHotel()) return;
                String Hnom = "hotel-" + (MonopolyETSE.juego.getTablero().getHoteles().size()+1); // id del hotel
                Hotel hotel = new Hotel(Hnom, this, getValorHotel(), getAlquilerHotel());
                Jugador actual2 = this.getAvatares().getLast().getJugador();
                // paga el hotel
                actual2.sumarFortuna(-getValorHotel());
                actual2.agregarDineroInvertido(getValorHotel());

                // se sustituyen las casas por el hotel
                Iterator<Edificio> it = getEdificios().iterator();
                while (it.hasNext()) {
                    Edificio ed = it.next();
                    if (ed.getTipo().equals("casa")) {
                        it.remove();
                        MonopolyETSE.juego.getTablero().getCasas().remove(ed);
                    }
                }
                float impHotel = getImpuesto() - getAlquilerCasa() * 4;
                // se construye el hotel
                getEdificios().add(hotel);
                MonopolyETSE.juego.getTablero().getHoteles().add(hotel);
                impHotel += getAlquilerHotel();
                setImpuesto(impHotel);
                break;
            case "piscina":
                if (this.valores.get("piscina") > propietario.getFortuna()) {
                    Juego.consola.imprimir("\t*** No tienes suficiente liquidez. Coste por piscina:  " + valores.get("piscina") + " ***");
                    return;
                }
                if (!construirPiscina()) return;
                String Pnom = "piscina-" + (MonopolyETSE.juego.getTablero().getPiscinas().size()+1); // id de la piscina
                Piscina piscina = new Piscina(Pnom, this, getValorPiscina(), getAlquilerPiscina());
                Jugador actual3 = this.getAvatares().getLast().getJugador();
                // se paga la piscina
                actual3.sumarFortuna(-getValorPiscina());
                actual3.agregarDineroInvertido(getValorPiscina());
                // se construye la piscina
                getEdificios().add(piscina);
                float impPisc = getImpuesto() + getAlquilerPiscina();
                setImpuesto(impPisc);
                MonopolyETSE.juego.getTablero().getPiscinas().add(piscina);
                break;
            case "pista":
                if (this.valores.get("pista") > propietario.getFortuna()) {
                    Juego.consola.imprimir("\t*** No tienes suficiente liquidez. Coste por pista: " + valores.get("pista") + " ***");
                    return;
                }
                if (!construirPista()) return;
                String Pinom = "pista-" + (MonopolyETSE.juego.getTablero().getPistas().size()+1); // id de la pista
                PistaDeporte pista = new PistaDeporte(Pinom, this, getValorPista(), getAlquilerPista());
                Jugador actual4 = this.getAvatares().getLast().getJugador();
                // se paga la pista
                actual4.sumarFortuna(-getValorPista());
                actual4.agregarDineroInvertido(getValorPista());
                // se construye la pista
                getEdificios().add(pista);
                float impPista = getImpuesto() + getAlquilerPista();
                setImpuesto(impPista);
                MonopolyETSE.juego.getTablero().getPistas().add(pista);
                break;
            default:
                Juego.consola.imprimir("\t*** Tipo de edificio no registrado. ***");
                return;
        }
        Juego.consola.imprimir("\t" + Valor.GREEN + "Edificio (" + tipo + ") construido en " + this.getNombre() + Valor.RESET);
    }

    public void venderEdificio(String tipoEd, int n) {
        if (this.getDuenho() != MonopolyETSE.juego.getJugadores().get(MonopolyETSE.juego.getTurno())) {
            Juego.consola.imprimir("\t*** No puedes vender. La casilla '" + this.getNombre() + "' no es tuya. ***");
            Juego.consola.imprimir("}\n");
            return;
        }
        if (n < 1) {
            Juego.consola.imprimir("\t*** Debes vender al menos 1 propiedad. ***");
            Juego.consola.imprimir("}\n");
            return;
        }
        Jugador j = MonopolyETSE.juego.getJugadores().get(MonopolyETSE.juego.getTurno());
        if (getEdificios() == null || getEdificios().isEmpty()) setEdificios(new ArrayList<>());
        int count = 0;
        switch (tipoEd) {
            case "casa":
                for (Edificio ed : this.getEdificios()) {
                    if (ed.getTipo().equals("casa")) count++;
                }
                if (count < n) {
                    Juego.consola.imprimir("\t*** No puedes vender " + n + " casas. Solo tienes " + count + ". ***");
                    return;
                }
                int i = 0;
                Iterator<Edificio> it = this.getEdificios().iterator();
                while (it.hasNext()) {
                    Edificio ed = it.next();
                    if (ed.getTipo().equals("casa")) {
                        if (i >= count - n) {
                            it.remove();
                            MonopolyETSE.juego.getTablero().getCasas().remove(ed);
                            float impCasa = getImpuesto() - getAlquilerCasa();
                            setImpuesto(impCasa);
                            j.sumarFortuna(getValorCasa());
                        }
                        i++;
                    }
                }
                Juego.consola.imprimir("\t" + j.getNombre() + " ha vendido " + n + " casas en " + this.getNombre() + ", recibiendo " + (n*getValorCasa())
                        + "€. En la propiedad queda(n) " + (count - n) + " casas.");
                break;
            case "hotel":
                for (Edificio ed : this.getEdificios()) {
                    if (ed.getTipo().equals("hotel")) count++;
                }
                if (count == 0) {
                    Juego.consola.imprimir("\t*** No tienes ningún hotel construído. ***");
                    Juego.consola.imprimir("}\n");
                    return;
                }
                Iterator<Edificio> it2 = this.getEdificios().iterator();
                while (it2.hasNext()) {
                    Edificio ed = it2.next();
                    if (ed.getTipo().equals("hotel")) {
                        it2.remove();
                        MonopolyETSE.juego.getTablero().getHoteles().remove(ed);
                        float impHotel = getImpuesto() - getAlquilerHotel();
                        setImpuesto(impHotel);
                        j.sumarFortuna(getValorHotel());
                        break;
                    }
                }
                String print = ", recibiendo " + getValorHotel() + "€.";
                if (n > 1) {
                    print = "\tSolo se puede vender un hotel" + print;
                }
                else print = "\t" + j.getNombre() + " ha vendido 1 hotel en " + this.getNombre() + print;
                Juego.consola.imprimir(print);
                break;
            case "piscina":
                for (Edificio ed : this.getEdificios()) {
                    if (ed.getTipo().equals("piscina")) count++;
                }
                if (count == 0) {
                    Juego.consola.imprimir("\t*** No tienes ninguna piscina construída. ***");
                    Juego.consola.imprimir("}\n");
                    return;
                }
                Iterator<Edificio> it3 = this.getEdificios().iterator();
                while (it3.hasNext()) {
                    Edificio ed = it3.next();
                    if (ed.getTipo().equals("piscina")) {
                        it3.remove();
                        MonopolyETSE.juego.getTablero().getPiscinas().remove(ed);
                        float impPisc = getImpuesto() - getAlquilerPiscina();
                        setImpuesto(impPisc);
                        j.sumarFortuna(getValorPiscina());
                        break;
                    }
                }
                String print2 = ", recibiendo " + getValorPiscina() + "€.";
                if (n > 1) {
                    print2 = "\tSolo se puede vender una piscina" + print2;
                }
                else print2 = "\t" + j.getNombre() + " ha vendido 1 piscina en " + this.getNombre() + print2;
                Juego.consola.imprimir(print2);
                break;
            case "pista":
                for (Edificio ed : this.getEdificios()) {
                    if (ed.getTipo().equals("pista")) count++;
                }
                if (count == 0) {
                    Juego.consola.imprimir("\t*** No tienes ninguna pista construída. ***");
                    Juego.consola.imprimir("}\n");
                    return;
                }
                Iterator<Edificio> it4 = this.getEdificios().iterator();
                while (it4.hasNext()) {
                    Edificio ed = it4.next();
                    if (ed.getTipo().equals("pista")) {
                        it4.remove();
                        MonopolyETSE.juego.getTablero().getPistas().remove(ed);
                        float impPista = getImpuesto() - getAlquilerPista();
                        setImpuesto(impPista);
                        j.sumarFortuna(getValorPista());
                        break;
                    }
                }
                String print3 = ", recibiendo " + getValorPista() + "€.";
                if (n > 1) {
                    print3 = "\tSolo se puede vender una pista" + print3;
                }
                else print3 = "\t" + j.getNombre() + " ha vendido 1 pista en " + this.getNombre() + print3;
                Juego.consola.imprimir(print3);
                break;
            default:
                Juego.consola.imprimir("\t*** No puedes vender. Tipo de edificio no registrado. ***");
                Juego.consola.imprimir("}\n");
                return;
        }
    }

    private boolean construirCasa() {
        int count = 0;
        for (Edificio ed : this.getEdificios()) {
            if (ed.getTipo().equals("casa")) count += 1;
        }
        if (count == 4) {
            Juego.consola.imprimir("\t*** Máximo de 4 casas alcanzado. ***");
            return false;
        }
        Juego.consola.imprimir("\tSe pueden construir " + (4 - count - 1) + " casas más.");
        return true;
    }

    private boolean construirHotel() {
        int count = 0;
        for (Edificio ed : this.getEdificios()) {
            if (ed.getTipo().equals("hotel")) {
                Juego.consola.imprimir("\t" + Valor.RED + "Ya hay un hotel construido." + Valor.RESET);
                return false;
            }
            if (ed.getTipo().equals("casa")) count += 1;
        }
        if (count != 4) {
            Juego.consola.imprimir("\t*** Se necesitan 4 casas para construir un hotel. ***");
            return false;
        }
        return true;
    }

    private boolean construirPiscina() {
        boolean hayHotel = false;
        for (Edificio ed : this.getEdificios()) {
            if (ed.getTipo().equals("piscina")) {
                Juego.consola.imprimir("\t" + Valor.RED + "Ya hay una piscina construida." + Valor.RESET);
                return false;
            }
            if (ed.getTipo().equals("hotel")) {
                hayHotel = true;
            }
        }
        if (!hayHotel) {
            Juego.consola.imprimir("\t*** Se necesita un hotel para construir una piscina. ***");
            return false;
        }
        return true;
    }

    private boolean construirPista() {
        boolean hayPiscina = false;
        for (Edificio ed : this.getEdificios()) {
            if (ed.getTipo().equals("pista")) {
                Juego.consola.imprimir("\t" + Valor.RED + "Ya hay una pista construida." + Valor.RESET);
                return false;
            }
            if (ed.getTipo().equals("piscina")) {
                hayPiscina = true;
            }
        }
        if (!hayPiscina) {
            Juego.consola.imprimir("\t*** Se necesita una piscina para construir una pista de deporte. ***");
            return false;
        }
        return true;
    }

    public void hipotecar(Jugador jugadorActual) {
        // Verificar dueño
        if (this.getDuenho() != jugadorActual) {
            Juego.consola.imprimir("\t" + Valor.RED + "Error: Solo el dueño puede hipotecar esta casilla." + Valor.RESET);
            return;
        }

        // Verificar si ya está hipotecada
        if (this.hipotecada) {
            Juego.consola.imprimir("\t" + Valor.RED + "Error: La propiedad ya está hipotecada." + Valor.RESET);
            return;
        }

        // Verificar si hay edificios
        if (this.getEdificios() != null && !this.getEdificios().isEmpty()) {
            Juego.consola.imprimir("\t" + Valor.YELLOW + "El dueño tiene edificios en esta casilla. Debe venderlos antes de hipotecar." + Valor.RESET);
            
            // CAMBIO PRINCIPAL: Pasamos la pregunta directamente al método leer()
            String respuesta = Juego.consola.leer("\t¿Quieres vender los edificios ahora? (s/n)");
    
            if (respuesta.trim().equalsIgnoreCase("s")) {
    
                while (!this.getEdificios().isEmpty()) {
                    // Preguntamos el tipo directamente en el leer
                    String tipo = Juego.consola.leer("\t¿Qué edificio quieres vender? (casa / hotel / piscina / pista)").toLowerCase();
                    this.venderEdificio(tipo, 1);
                }
                
                // Verificación de seguridad por si salió del bucle sin vender todo
                if (!this.getEdificios().isEmpty()) {
                     Juego.consola.imprimir("\t" + Valor.RED + "Aún quedan edificios. No se puede hipotecar." + Valor.RESET);
                     return;
                }
    
            } else {
                Juego.consola.imprimir("\t" + Valor.RED + "No se han vendido los edificios. No se puede hipotecar la casilla." + Valor.RESET);
                return;
            }
        }


        // Hipotecar
        this.hipotecada = true; // Marca la casilla como hipotecada
        this.puedeCobrarAlquiler = false; // Bloquear alquileres
        float dinero = getHipoteca() / 2;
        jugadorActual.sumarFortuna(dinero); // Entregar dinero
        Juego.consola.imprimir("\t" + Valor.GREEN + "Casilla hipotecada con éxito. Se han recibido " + this.getHipoteca() + "€." + Valor.RESET);
    }

    public void deshipotecar(Jugador jugadorActual){
        // Verificar dueño
        if (this.getDuenho() != jugadorActual) {
            Juego.consola.imprimir("\t" + Valor.RED + "Error: Solo el dueño puede deshipotecar esta casilla." + Valor.RESET);
            return;
        }

        // Verificar si está hipotecada
        if (!this.hipotecada) {
            Juego.consola.imprimir("\t" + Valor.RED + "Error: La propiedad no está hipotecada." + Valor.RESET);
            return;
        }

        // Verificar si el jugador tiene suficiente dinero
        float costoDeshipotecar = this.getHipoteca() ;
        if (jugadorActual.getFortuna() < costoDeshipotecar) {
            Juego.consola.imprimir("\t" + Valor.RED + "Error: No tienes suficiente dinero para deshipotecar esta casilla." + Valor.RESET);
            return;
        }

        // Deshipotecar
        this.hipotecada = false; // Marca la casilla como no hipotecada
        this.puedeCobrarAlquiler = true; // Permitir alquileres
        jugadorActual.sumarFortuna(-costoDeshipotecar/2); // Cobrar al jugador
        Juego.consola.imprimir("\t" + Valor.GREEN + "Casilla deshipotecada con éxito. Se han pagado " + costoDeshipotecar + "€." + Valor.RESET);
    }

    public boolean alquiler() {
        if (this.getDuenho() != MonopolyETSE.juego.getBanca() && !estaHipotecada())
            return true;
        return false;
    }

    public float valor() {
        return this.getValor();
    }


    public boolean estaHipotecada() {
        return this.hipotecada;
    }

    public void setValorCasa(float valorCasa) {
        this.valores.put("casa", valorCasa);
    }
    public void setValorHotel(float valorHotel) {
        this.valores.put("hotel", valorHotel);
    }
    public void setValorPiscina(float valorPiscina) {
        this.valores.put("piscina", valorPiscina);
    }
    public void setValorPista(float valorPista) {
        this.valores.put("pista", valorPista);
    }
    public void setAlquilerCasa(float alquilerCasa) {
        this.alquileres.put("casa", alquilerCasa);
    }
    public void setAlquilerHotel(float alquilerHotel) {
        this.alquileres.put("hotel", alquilerHotel);
    }
    public void setAlquilerPiscina(float alquilerPiscina) {
        this.alquileres.put("piscina", alquilerPiscina);
    }
    public void setAlquilerPista(float alquilerPista) {
        this.alquileres.put("pista", alquilerPista);
    }
    public void setEdificios(ArrayList<Edificio> edificios) {
        this.edificios = edificios;
    }

    public ArrayList<Edificio> getEdificios() {
        if (this.edificios == null) {
            this.edificios = new ArrayList<>();
        }
        return this.edificios;
    }
    public float getValorCasa() {
        return valores.get("casa");
    }
    public float getValorHotel() {
        return valores.get("hotel");
    }
    public float getValorPiscina() {
        return valores.get("piscina");
    }
    public float getValorPista() {
        return valores.get("pista");
    }
    public float getAlquilerCasa() {
        return alquileres.get("casa");
    }
    public float getAlquilerHotel() {
        return alquileres.get("hotel");
    }
    public float getAlquilerPiscina() {
        return alquileres.get("piscina");
    }
    public float getAlquilerPista() {
        return alquileres.get("pista");
    }

}
