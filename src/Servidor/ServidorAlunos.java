package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class ServidorAlunos {
    private static HashMap<Integer, String> registrosAlunos = new HashMap<>();

    public static void main(String[] args) {
        registrosAlunos.put(1001, "Carlos");
        registrosAlunos.put(1002, "Maria");
        registrosAlunos.put(1003, "João");
        registrosAlunos.put(1004, "Ana");
        registrosAlunos.put(1005, "Pedro");

        try (ServerSocket serverSocket = new ServerSocket(5001)) {
            System.out.println("Servidor de Alunos rodando na porta 5001...");
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
                        int idAluno = Integer.parseInt(inputLine);
                        String dadosAluno = registrosAlunos.getOrDefault(idAluno, "Aluno não encontrado.");
                        out.println(dadosAluno);
                    } catch (NumberFormatException e) {
                        out.println("Erro: ID inválido. Por favor, envie um número válido.");
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
