import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ServerRunnable implements Runnable {

    private Socket socket;
    private JDBC dataBase;


    public ServerRunnable(Socket socket, JDBC dataBase) {
        this.socket = socket;
        this.dataBase = dataBase;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());

            String idKlienta = String.valueOf(UUID.randomUUID());
            int idPytania = 0;

            ResultSet rs = dataBase.getBazaPytan();
            rs.last();
            int numRows = rs.getRow();

            for (int i = 1; i <= numRows; i++) {
                rs = dataBase.getPytanie(i);
                try {
                    while (rs.next()) {
                        idPytania = rs.getInt("idPytania");
                        out.println(rs.getString("tresc"));
                        out.println(rs.getString("odp1"));
                        out.println(rs.getString("odp2"));
                        out.println(rs.getString("odp3"));
                        out.println(rs.getString("odp4"));
                    }
                    out.println("stop");
                    String answer = in.readLine();
                    dataBase.insertOdpowiedz(idKlienta, idPytania, answer);
                    dataBase.updateWyniki(idPytania, answer);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            out.println("exit");

            out.println("Wyniki Ankiety: ");
            ResultSet rs2 = dataBase.getWyniki();

            while (rs2.next()) {
                int counter = 0;
                counter += Integer.parseInt(rs2.getString("odp1"));
                counter += Integer.parseInt(rs2.getString("odp2"));
                counter += Integer.parseInt(rs2.getString("odp3"));
                counter += Integer.parseInt(rs2.getString("odp4"));
                out.println("Pytanie nr" + rs2.getInt("idPytania"));
                out.println("Odpowiedz 1: " + Integer.parseInt(rs2.getString("odp1"))*100/counter + "%");
                out.println("Odpowiedz 2: " + Integer.parseInt(rs2.getString("odp2"))*100/counter + "%");
                out.println("Odpowiedz 3: " + Integer.parseInt(rs2.getString("odp3"))*100/counter + "%");
                out.println("Odpowiedz 4: " + Integer.parseInt(rs2.getString("odp4"))*100/counter + "%");
            }
            out.println("exit");
            out.close();
            in.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
