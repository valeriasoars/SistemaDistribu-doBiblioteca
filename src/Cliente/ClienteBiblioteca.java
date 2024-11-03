package Cliente;

import java.io.*;
import java.net.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ClienteBiblioteca {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite seu nome: ");
        String nomeCliente = scanner.nextLine();

        while (true) {
            System.out.println();
            System.out.println("Bem-vindo ao Sistema de Biblioteca:");
            System.out.println("1. Consultar Livro");
            System.out.println("2. Consultar Aluno");
            System.out.println("3. Consultar Empréstimo");
            System.out.println("0. Sair");

            System.out.print("Selecione um servidor: ");
            int opcao;
            try {
                opcao = scanner.nextInt();
                if (opcao < 0 || opcao > 3) {
                    System.out.println("Erro: Opção inválida. Por favor, selecione uma opção entre 0 e 3.");
                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("Erro: Por favor, insira um número válido para a opção.");
                scanner.nextLine();
                continue;
            }

            if (opcao == 0) break;

            int porta = 0;
            switch (opcao) {
                case 1 -> porta = 5000;
                case 2 -> porta = 5001;
                case 3 -> porta = 5002;
            }

            try (Socket socket = new Socket("localhost", porta);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                out.println(nomeCliente);

                System.out.println();
                if(opcao == 1)
                    System.out.println("Conectado no servidor de livros");
                else if(opcao == 2)
                    System.out.println("Conectado no servidor de alunos");
                else
                    System.out.println("Conectado no servidor de emprestimos");

                while (true) {
                    System.out.println("----------------------------");



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
            } catch (IOException e) {
                System.out.println(">>> Erro ao conectar ao servidor.");
            }
        }
        scanner.close();
    }
}

