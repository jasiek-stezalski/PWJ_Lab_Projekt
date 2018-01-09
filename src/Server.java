import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    static final int serverPort = 50000;

    public static void main(String[] args) throws IOException {

        // Connect with database
        ServerSocket serverSocket = null;

        try {
            // Create Server socket
            serverSocket = new ServerSocket(serverPort);

            ExecutorService executor = Executors.newCachedThreadPool();

            executor.execute(new ExitRunnable(serverSocket));

            System.out.println("Server został uruchomiony");

            // Wait for new client
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    executor.execute(new ServerRunnable(socket));
                } catch (SocketException e) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            System.out.println("\nServer został wyłączony");
            System.exit(0);
        }
    }
}
