import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    static final int serverPort = 50000;

    public static void main(String[] args) throws IOException {

        Socket socket = null;
        OutputStream sockOut = null;
        InputStream sockIn = null;
        PrintStream output = null;

        Scanner sc = new Scanner(System.in);

        try {
            // Getting localhost
            InetAddress serverHost = InetAddress.getLocalHost();
            // Attempt to connect to the server
            socket = new Socket(serverHost, serverPort);

            sockIn = socket.getInputStream();
            sockOut = socket.getOutputStream();
//
//            //*pobieranie z servera
            Scanner scanner = new Scanner(socket.getInputStream());
//            System.out.println("Podaj dowolna liczbe");
//            int number = sc.nextInt();
//            // *do wysylania na server

            String temp;
            while (scanner.hasNext()) {
                temp = scanner.nextLine();
                if(temp.equals("exit"))
                    break;
                System.out.println(temp);
            }
            String answer = sc.nextLine();
            PrintStream printStream = new PrintStream(socket.getOutputStream());
            printStream.println(answer);
            temp = scanner.nextLine();
            System.out.println(temp);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch (ConnectException e) {
            System.err.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sockOut.close();
            sockIn.close();
            socket.close();
            System.out.println("Connection closed");
        }


    }
}
