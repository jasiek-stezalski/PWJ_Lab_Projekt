import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    static final int serverPort = 50000;

    public static void main(String[] args) throws IOException {

        Socket socket = null;
        OutputStream sockOut = null;
        InputStream sockIn = null;

        try {
            // getting localhost
            InetAddress serverHost = InetAddress.getLocalHost();
            // attempt to connect to the server
            socket = new Socket(serverHost, serverPort);

            sockIn = socket.getInputStream();
            sockOut = socket.getOutputStream();



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
