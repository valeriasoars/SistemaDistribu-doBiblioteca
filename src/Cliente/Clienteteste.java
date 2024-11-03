package Cliente;

import java.io.*;
import java.net.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Clienteteste {
    private String nomeCliente;
    private final Scanner scanner = new Scanner(System.in);

    public Clienteteste(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public void iniciar() {
        while (true) {
            exibirMenu();
            int opcao = obterOpcao();

            if (opcao == 0) break;

            int porta = obterPortaServidor(opcao);
            if (porta == 0) {
                System.out.println("Erro: Servidor inválido. Tente novamente.");
                continue;
            }

            realizarConsulta(porta);
        }

        System.out.println("Cliente desconectado. Obrigado por usar o Sistema de Biblioteca.");
        scanner.close();
    }

    private void exibirMenu() {
        System.out.println();
        System.out.println("Bem-vindo ao Sistema de Biblioteca:");
        System.out.println("1. Consultar Livro");
        System.out.println("2. Consultar Aluno");
        System.out.println("3. Consultar Empréstimo");
        System.out.println("0. Sair");
        System.out.print("Selecione um servidor: ");
    }

    private int obterOpcao() {
        int opcao;
        try {
            opcao = scanner.nextInt();
            if (opcao < 0 || opcao > 3) {
                System.out.println("Erro: Opção inválida. Por favor, selecione uma opção entre 0 e 3.");
                return -1;
            }
        } catch (InputMismatchException e) {
            System.out.println("Erro: Por favor, insira um número válido para a opção.");
            scanner.nextLine();
            return -1;
        }
        return opcao;
    }

    private int obterPortaServidor(int opcao) {
        return switch (opcao) {
            case 1 -> 5000;
            case 2 -> 5001;
            case 3 -> 5002;
            default -> 0;
        };
    }

    private void realizarConsulta(int porta) {
        try (Socket socket = new Socket("localhost", porta);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(nomeCliente);

            System.out.println("\nConectado ao servidor de " + obterNomeServidor(porta));

            while (true) {
                System.out.print("Digite o ID para consulta ou 0 para voltar ao menu principal: ");

                int id;
                try {
                    id = scanner.nextInt();
                    if (id == 0) break;
                } catch (InputMismatchException e) {
                    System.out.println("Erro: O ID deve ser um número inteiro.");
                    scanner.nextLine();
                    continue;
                }

                out.println(id);

                String resposta = in.readLine();
                if (resposta == null || resposta.isEmpty()) {
                    System.out.println(">>> Erro: ID não encontrado no servidor.");
                } else {
                    System.out.println(">>> Resposta do Servidor: " + resposta);
                }
            }

            System.out.println("Desconectado do servidor de " + obterNomeServidor(porta));

        } catch (IOException e) {
            System.out.println(">>> Erro ao conectar ao servidor. Verifique se o servidor está ativo e tente novamente.");
        }
    }

    private String obterNomeServidor(int porta) {
        return switch (porta) {
            case 5000 -> "Livros";
            case 5001 -> "Alunos";
            case 5002 -> "Empréstimos";
            default -> "Desconhecido";
        };
    }
}
