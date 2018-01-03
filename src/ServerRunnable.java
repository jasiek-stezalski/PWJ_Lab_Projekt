import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;

public class ServerRunnable implements Runnable {

    private Socket socket;
    private JDBC dataBase;



    public ServerRunnable(Socket socket, JDBC dataBase) {
        this.socket = socket;
        this.dataBase = dataBase;
    }

    @Override
    public void run() {
        ResultSet rs = dataBase.getBazaPytan();
        int numRows = 0;
        try {
            rs.last();
            numRows = rs.getRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        numRows /= 4;
        try {

            PrintStream printStream = new PrintStream(socket.getOutputStream());
            for (int i=1; i<=numRows; i++) {
                rs = dataBase.getPytanie(i);
                int counter = 0;
                try {
                    while (rs.next()) {
                        if (counter == 0){
                            printStream.println(rs.getString("tresc"));
                            counter++;
                        }
                        printStream.println(rs.getString("odpowiedz"));
                    }
                    printStream.println("stop");
                    Scanner scanner = new Scanner(socket.getInputStream());
                    String answer = scanner.nextLine();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
