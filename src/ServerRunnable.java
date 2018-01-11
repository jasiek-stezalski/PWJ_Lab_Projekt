import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerRunnable implements Runnable {

    private Socket socket;
    private JDBC dataBase;
    private String idKlienta;

    public ServerRunnable(Socket socket, JDBC dataBase) {
        this.socket = socket;
        this.dataBase = dataBase;
        // Generate id for new client
        idKlienta = String.valueOf(UUID.randomUUID());
        System.out.println("Nowy klient o ID: " + idKlienta);
    }
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());

            ResultSet rs = dataBase.getBazaPytan();
            rs.last();
            // Count number of questions
            int numRows = rs.getRow();

            int idPytania = 0;

            // Send to client all questions
            for (int i = 1; i <= numRows; i++) {
                rs = dataBase.getPytanie(i);
                while (rs.next()) {
                    idPytania = rs.getInt("idPytania");
                    out.println(rs.getString("tresc"));
                    out.println(rs.getString("odpA"));
                    out.println(rs.getString("odpB"));
                    out.println(rs.getString("odpC"));
                    out.println(rs.getString("odpD"));
                }
                // Wait for an answer
                String answer = in.readLine();

                // Send the answer to the database
                dataBase.insertOdpowiedz(idKlienta, idPytania, answer);
                dataBase.updateWyniki(idPytania, answer);
            }
            // End of questions
            out.println("exit");
            rs = dataBase.getOdpowiedzi(idKlienta);
            List<String> rs2 = new ArrayList<>();
            while (rs.next()) {
                rs2.add(rs.getString("odpowiedz"));
            }
            rs = dataBase.getWyniki();
            int i = 0;
            while (rs.next()) {
                int counter = 0;
                counter += Integer.parseInt(rs.getString("odpA"));
                counter += Integer.parseInt(rs.getString("odpB"));
                counter += Integer.parseInt(rs.getString("odpC"));
                counter += Integer.parseInt(rs.getString("odpD"));
                // Send to client the results of the questionnaire
                out.println(Integer.parseInt(rs.getString("odpA")) * 100 / counter);
                out.println(Integer.parseInt(rs.getString("odpB")) * 100 / counter);
                out.println(Integer.parseInt(rs.getString("odpC")) * 100 / counter);
                out.println(Integer.parseInt(rs.getString("odpD")) * 100 / counter);
                out.println(rs2.get(i++));
            }
            out.println("exit");
            out.close();
            in.close();
            System.out.println("Zakończono połączenie z klientem o ID: " + idKlienta);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
