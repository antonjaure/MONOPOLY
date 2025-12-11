package monopoly;

import java.util.Scanner;

public class ConsolaNormal implements Consola {

    private final Scanner scanner = new Scanner(System.in);
        
        @Override
        public void imprimir(String mensaje) {
            System.out.println(mensaje);
        }
        
        @Override
        public String leer(String descripcion) {
            System.out.println(descripcion);
            return scanner.nextLine();
        }
    
}
