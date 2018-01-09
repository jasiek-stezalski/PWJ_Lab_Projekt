import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class ExitRunnable implements Runnable {

    ServerSocket serverSocket;

    public ExitRunnable(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        while (true) {
            if (in.nextLine().equals("exit")){
                break;
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
