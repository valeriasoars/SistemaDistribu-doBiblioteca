package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class ServidorEmprestimos {
    private static HashMap<Integer, String> registrosEmprestimos = new HashMap<>();

    public static void main(String[] args) {
        registrosEmprestimos.put(1, "Livro ID: 1, Aluno ID: 1001, Data: 2024-01-15");
        registrosEmprestimos.put(2, "Livro ID: 2, Aluno ID: 1002, Data: 2024-02-20");
        registrosEmprestimos.put(3, "Livro ID: 3, Aluno ID: 1003, Data: 2024-03-10");
        registrosEmprestimos.put(4, "Livro ID: 4, Aluno ID: 1001, Data: 2024-04-05");
        registrosEmprestimos.put(5, "Livro ID: 5, Aluno ID: 1002, Data: 2024-05-20");

        try (ServerSocket serverSocket = new ServerSocket(5002)) {
            System.out.println("Servidor de Empréstimos rodando na porta 5002...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            String nomeCliente = "";
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                nomeCliente = in.readLine();
                System.out.println("Cliente conectado: " + nomeCliente);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    try {
                        int idEmprestimo = Integer.parseInt(inputLine);
                        String dadosEmprestimo = registrosEmprestimos.getOrDefault(idEmprestimo, "Empréstimo não encontrado.");
                        out.println(dadosEmprestimo);
                    } catch (NumberFormatException e) {
                        out.println("Erro: ID de empréstimo inválido.");
                    }
                }

                System.out.println("Cliente desconectado: " + nomeCliente);
            } catch (SocketException e) {
                System.out.println("Cliente desconectado abruptamente: " + nomeCliente);
            } catch (IOException e) {
                System.out.println("Erro de entrada/saída com o cliente: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println("Erro ao fechar o socket do cliente: " + e.getMessage());
                }
            }
        }
    }
}

