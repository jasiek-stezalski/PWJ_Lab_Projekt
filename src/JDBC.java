import java.sql.*;
import java.util.Properties;

public class JDBC {

    private Statement st;
    private Connection connection;

    public JDBC() {
    }

    public void connect() {
        if (chechDriver("com.mysql.jdbc.Driver"))
            System.out.println("... OK");
        else
            System.exit(1);

        connection = getConnection("jdbc:mysql://", "localhost", 3306, "root", "");
        st = createStatement();
        if (executeUpdate("USE nowaBaza;") == 0)
            System.out.println("Baza wybrana");
        else {
            System.out.println("Baza nie istnieje! Tworzymy baze: ");
            if (executeUpdate("create Database nowaBaza;") == 1)
                System.out.println("Baza utworzona");
            else
                System.out.println("Baza nieutworzona!");
            if (executeUpdate("USE nowaBaza;") == 0)
                System.out.println("Baza wybrana");
            else
                System.out.println("Baza niewybrana!");
            createTables();
        }
    }

    private boolean chechDriver(String driver) {
        System.out.print("Sprawdzanie sterownika bazy danych ");
        try {
            Class.forName(driver).newInstance();
            return true;
        } catch (Exception e) {
            System.out.println("Blad przy ladowaniu sterownika bazy!");
            return false;
        }
    }

    private Connection getConnection(String kindOfDatabase, String adres, int port, String userName, String password) {

        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", userName);
        connectionProps.put("password", password);
        try {
            conn = DriverManager.getConnection(kindOfDatabase + adres + ":" + port + "/",
                    connectionProps);
        } catch (SQLException e) {
            System.out.println("Blad poloczenia z baza danych danych! " + e.getMessage() + ": " + e.getErrorCode());
            System.exit(2);
        }
        System.out.println("Poloczenie z baza danych: ... OK");
        return conn;
    }

    private Statement createStatement() {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Blad createStatement! " + e.getMessage() + ": " + e.getErrorCode());
            System.exit(3);
        }
        return null;
    }

    private int executeUpdate(String sql) {
        try {
            return st.executeUpdate(sql);
        } catch (SQLException e) {
            return -1;
        }
    }

    private void createTables() {
        if (executeUpdate("CREATE TABLE bazapytan (idPytania INT NOT NULL, tresc varchar(100) NOT NULL, odpowiedz varchar(20) NOT NULL);") == 0) {
            System.out.println("Tabela bazaPytan utworzona");
            String sql = "INSERT INTO bazaPytan VALUES" +
                    "(1, 'pytanie nr 1', '1.odpa'), " +
                    "(1, 'pytanie nr 1', '2.odpb'), " +
                    "(1, 'pytanie nr 1', '3.odpc')," +
                    "(1, 'pytanie nr 1', '4.odpd')," +
                    "(2, 'pytanie nr 2', '1.odpa')," +
                    "(2, 'pytanie nr 2', '2.odpb')," +
                    "(2, 'pytanie nr 2', '3.odpc')," +
                    "(2, 'pytanie nr 2', '4.odpd');";
            executeUpdate(sql);
            System.out.println("Tabela bazaPytan wypelniona danymi");
        } else
            System.out.println("Tabela bazaPytanjuz istnieje!");
    }

    public ResultSet getBazaPytan() {
        String sql = "Select * from bazaPytan;";
        ResultSet rs = null;
        try {
             rs = st.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet getPytanie(int id) {
        String sql = "Select * from bazaPytan where idPytania = " + id;
        ResultSet rs = null;
        try {
            rs = st.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;

    }

    public void printBazaPytan(ResultSet rs) {
        try {
            while (rs.next()) {
                int idPytania = rs.getInt("idPytania");
                String tresc = rs.getString("tresc");
                String odpowiedz = rs.getString("odpowiedz");

                System.out.print("IdPytania: " + idPytania);
                System.out.print(", Tresc: " + tresc);
                System.out.println(", Odpowiedz: " + odpowiedz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        System.out.print("\nZamykanie polaczenia z baza:");
        try {
            st.close();
            connection.close();
        } catch (SQLException e) {
            System.out
                    .println("Blad przy zamykaniu polaczenia z baza! " + e.getMessage() + ": " + e.getErrorCode());
            System.exit(4);
        }
        System.out.print(" zamkniecie OK");
    }


}

