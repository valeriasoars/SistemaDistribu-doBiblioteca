package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class ServidorLivros {
    private static HashMap<Integer, String> catalogoLivros = new HashMap<>();

    public static void main(String[] args) {
        catalogoLivros.put(1, "Flores Para Algernon - Daniel Keyes - 1966");
        catalogoLivros.put(2, "O Alienista - Machado de Assis - 1882");
        catalogoLivros.put(3, "Vidas Secas - Graciliano Ramos - 1938");
        catalogoLivros.put(4, "Quincas Borba - Machado de Assis - 1891");
        catalogoLivros.put(5, "Memórias Póstumas de Brás Cubas - Machado de Assis - 1881");

        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Servidor de Livros rodando na porta 5000...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                        int idLivro = Integer.parseInt(inputLine);
                        String descricao = catalogoLivros.getOrDefault(idLivro, "Livro não encontrado.");
                        out.println(descricao);
                    } catch (NumberFormatException e) {
                        out.println("Erro: ID de livro inválido.");
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

