import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerThread extends Thread {

    private int serverPort;
    private ServerSocket serverSocket;

    public ServerThread() {
    }

    @Override
    public void run() {
        // Connect with database

        try {
            // Create Server socket
            serverSocket = new ServerSocket(serverPort);

            ExecutorService executor = Executors.newCachedThreadPool();

            System.out.println("Server został uruchomiony");

            JDBC dataBase = new JDBC();
            dataBase.connect();

            // Wait for new client
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    executor.execute(new ServerRunnable(socket, dataBase));
                } catch (SocketException e) {
                    break;
                }
            }
            dataBase.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("\nServer został wyłączony");
            System.exit(0);
        }

    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

}
