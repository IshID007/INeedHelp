import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void run() {
        try {
            out.println("Enter your username: ");
            username = in.readLine();
            Server.addClient(username, this);

            String input;
            while ((input = in.readLine()) != null) {
                if (input.startsWith("@")) {
                    String[] split = input.split(" ", 2);
                    String recipient = split[0].substring(1);
                    Server.sendPrivateMessage(split[1], username, recipient);
                } else {
                    Server.sendPublicMessage(input, username);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Server.removeClient(username);
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String getUsername() {
        return username;
    }
}
