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
        Scanner odczyt = new Scanner(System.in);
        while (true) {
            if (odczyt.nextLine().equals("exit")){
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
