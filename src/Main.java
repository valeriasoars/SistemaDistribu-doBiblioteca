
import Cliente.Clienteteste;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite seu nome: ");
        String nomeCliente = scanner.nextLine();

        Clienteteste clienteBiblioteca = new Clienteteste(nomeCliente);
        clienteBiblioteca.iniciar();

        scanner.close();
    }
}
