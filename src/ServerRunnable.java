import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ServerRunnable implements Runnable {

    private Socket socket;

    public ServerRunnable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
//            Scanner scanner = new Scanner(socket.getInputStream());
//            int number = scanner.nextInt();
//            int temp = number*2;
            PrintStream printStream = new PrintStream(socket.getOutputStream());
            printStream.println("Ankieta \nTresc pytania ? \nOdpowiedzi: \nA.odpA \nB.odpB \nC.odpC \nD.odpD \nexit");
            Scanner scanner = new Scanner(socket.getInputStream());
            String answer = scanner.nextLine();
            if (answer.equals("d") || answer.equals("D")){
                printStream.println("Brawo udzieliles poprawnej odpowiedzi");
            }
            else printStream.println("Niestety ta odpowiedz jest niepoprawna");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
