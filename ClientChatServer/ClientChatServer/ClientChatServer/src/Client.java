import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Connected to server.");

            // Receiving messages from the server
            Thread receiveMessagesThread = new Thread(() -> {
                String message;
                try {
                    while ((message = in.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveMessagesThread.start();

            // Sending messages to the server
            String input;
            while ((input = console.readLine()) != null) {
                out.println(input);
            }

            socket.close();
            in.close();
            out.close();
            console.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
