[1mdiff --cc monopoly/Casilla.java[m
[1mindex cac0523,0040280..0000000[m
[1m--- a/monopoly/Casilla.java[m
[1m+++ b/monopoly/Casilla.java[m
[36m@@@ -336,70 -336,28 +336,96 @@@[m [mpublic class Casilla [m
       */[m
      public String casEnVenta() {[m
  [m
[32m++<<<<<<< HEAD[m
[32m +        return "";[m
[32m +    }[m
[32m +    /**[m
[32m +     * Método que gestiona los pagos al caer en esta casilla.[m
[32m +     * Parámetros:[m
[32m +     * - jugadorActual: jugador cuyo avatar ha caído en esta casilla.[m
[32m +     * - banca: jugador banca.[m
[32m +     * - tirada: valor de los dados (para calcular servicios).[m
[32m +     */[m
[32m +    public void gestionarPago(Jugador jugadorActual, Jugador banca, int tirada) {[m
[32m +    [m
[32m +        boolean solvente = evaluarCasilla(jugadorActual, banca, tirada);[m
[32m +    [m
[32m +        if (!solvente) {[m
[32m +            System.out.println(jugadorActual.getNombre() + " no tiene dinero suficiente para pagar esta casilla.");[m
[32m +            // Avisar que debe hipotecar o declararse en bancarrota[m
[32m +            return;[m
[32m +        }[m
[32m +    [m
[32m +        // Casillas con dueño distinto al jugador actual[m
[32m +        if ((tipo.equals("Solar") || tipo.equals("Transporte") || tipo.equals("Servicios"))[m
[32m +                && duenho != null && duenho != banca && duenho != jugadorActual) {[m
[32m +    [m
[32m +            float cantidad = 0;[m
[32m +    [m
[32m +            switch (tipo) {[m
[32m +                case "Solar":[m
[32m +                    // Antes: se usaba impuesto fijo de la casilla[m
[32m +                    // Ahora: considerar si el grupo completo pertenece al dueño → se podría duplicar alquiler[m
[32m +                    cantidad = impuesto; [m
[32m +                    break;[m
[32m +                case "Transporte":[m
[32m +                    cantidad = 250000f;[m
[32m +                    break;[m
[32m +                case "Servicios":[m
[32m +                    cantidad = tirada * 4 * 50000f;[m
[32m +                    break;[m
[32m +            }[m
[32m +    [m
[32m +            jugadorActual.sumarFortuna(-cantidad);[m
[32m +            duenho.sumarFortuna(cantidad);[m
[32m +            System.out.println(jugadorActual.getNombre() + " paga " + cantidad + "€ a " + duenho.getNombre());[m
[32m +    [m
[32m +        } else if (tipo.equals("Impuesto")) {[m
[32m +            float cantidad = 2000000f;[m
[32m +            jugadorActual.sumarFortuna(-cantidad);[m
[32m +            banca.sumarFortuna(cantidad);[m
[32m +            System.out.println(jugadorActual.getNombre() + " paga " + cantidad + "€ que se depositan en el Parking");[m
[32m +        } [m
[32m +        else if (tipo.equals("Comunidad") || tipo.equals("Suerte")) {[m
[32m +            System.out.println(jugadorActual.getNombre() + " ha caído en " + nombre + ". Roba una carta.");[m
[32m +        }[m
[32m +        else if (tipo.equals("Especial")) {[m
[32m +            if(nombre.equals("Parking")) {[m
[32m +                System.out.println(jugadorActual.getNombre() + " recibe el bote de " + valor + "€");[m
[32m +                jugadorActual.sumarFortuna(valor);[m
[32m +                valor = 0; // Vaciar el bote[m
[32m +            } else if(nombre.equals("Salida")) {[m
[32m +                System.out.println(jugadorActual.getNombre() + " pasa por Salida y recibe 2.000.000€");[m
[32m +                jugadorActual.sumarFortuna(2000000f);[m
[32m +            }[m
[32m +            // Ir a Cárcel se gestiona fuera en Menu.java[m
[32m +        }[m
[32m +    }[m
[32m +[m
[32m++=======[m
[32m+         String tipo = this.getTipo();[m
[32m++>>>>>>> 44dbad577e48c6fd46965713c663d1fa3ac96d94[m
[32m+ [m
[32m+         switch(tipo){[m
[32m+             [m
[32m+             case "Solar":[m
[32m+                 return "\nTipo: " + this.tipo + "\n" +[m
[32m+                 "Grupo: " + this.grupo + "\n" +[m
[32m+                 "Valor: " + this.valor + "€\n";[m
[32m+ [m
[32m+             case "Transporte":[m
[32m+                 return "\nTipo: " + this.tipo + "\n" +[m
[32m+                 "Valor: " + this.valor + "€\n"; [m
  [m
[32m+             case "Servicios":[m
[32m+                 return "\nTipo: " + this.tipo + "\n" +[m
[32m+                 "Valor: " + this.valor + "€\n";[m
[32m+             case "Impuesto":[m
[32m+             case "Comunidad":[m
[32m+             case "Suerte":[m
[32m+                 return "\nEsta casilla no está en venta.\n";[m
[32m+             default:[m
[32m+                 return "\nError al mostrar casEnVenta().\n";[m
[32m+         }[m
[32m+     }[m
  }[m
