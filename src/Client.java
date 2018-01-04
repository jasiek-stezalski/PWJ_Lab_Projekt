import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    static final int serverPort = 50000;

    public static void main(String[] args) throws IOException {


        try {
            // Getting localhost
            InetAddress serverHost = InetAddress.getLocalHost();
            // Attempt to connect to the server
            Socket socket = new Socket(serverHost, serverPort);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());

            Scanner scanner = new Scanner(System.in);
            String temp = null;
            while (true) {
                while (!(temp = in.readLine()).equals("stop")) {
                    System.out.println(temp);
                }
                String answer = null;
                do {
                    answer = scanner.nextLine();
                } while (!answer.equals("1") && !answer.equals("2") && !answer.equals("3") && !answer.equals("4"));
                out.println(answer);
                temp = in.readLine();
                if (temp.equals("exit"))
                    break;
            }
            scanner.close();
            out.close();
            in.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            System.err.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Connection closed");
        }


    }
}
