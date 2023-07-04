import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private static final int PORT = 12345;
    private static Map<String, ClientHandler> clients = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);

        System.out.println("Server started on port " + PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected: " + socket);

            ClientHandler clientHandler = new ClientHandler(socket);
            clientHandler.start();
        }
    }

    public static void addClient(String username, ClientHandler clientHandler) {
        clients.put(username, clientHandler);
    }

    public static void removeClient(String username) {
        clients.remove(username);
    }

    public static void sendPublicMessage(String message, String sender) {
        for (ClientHandler clientHandler : clients.values()) {
            if (!clientHandler.getName().equals(sender)) {
                clientHandler.sendMessage(sender + ": " + message);
            }
        }
    }

    public static void sendPrivateMessage(String message, String sender, String recipient) {
        ClientHandler recipientHandler = clients.get(recipient);
        if (recipientHandler != null) {
            recipientHandler.sendMessage("[Private] " + sender + ": " + message);
        } else {
            System.out.println("Invalid recipient: " + recipient);
        }
    }
}

