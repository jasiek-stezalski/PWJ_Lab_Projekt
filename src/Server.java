import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    static final int serverPort = 50000;

    public static void main(String[] args) throws IOException {

        // Connect with database
        JDBC dataBase = new JDBC();
        dataBase.connect();
        ServerSocket serverSocket = null;

        try {
            // Create Server socket
            serverSocket = new ServerSocket(serverPort);

            ExecutorService executor = Executors.newCachedThreadPool();

            // Wait for new client
            while (true) {
                Socket socket = serverSocket.accept();
                executor.execute(new ServerRunnable(socket, dataBase));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverSocket.close();
            dataBase.closeConnection();
            System.out.println("\nServer został wyłączony");
        }
    }
}
