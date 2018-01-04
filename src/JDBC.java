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


        if (executeUpdate("CREATE TABLE bazapytan (idPytania int(10) NOT NULL, tresc varchar(100) NOT NULL, " +
                "odp1 varchar(10) NOT NULL, odp2 varchar(10) NOT NULL, odp3 varchar(10) NOT NULL, odp4 varchar(10) NOT NULL);") == 0) {
            executeUpdate("ALTER TABLE bazapytan ADD PRIMARY KEY (idPytania);");
            System.out.println("Tabela bazaPytan utworzona");
            String sql = "INSERT INTO bazaPytan VALUES" +
                    "(1, 'pytanie nr 1', '1.odpa', '2.odpb', '3.odpc', '4.odpd'), " +
                    "(2, 'pytanie nr 2', '1.odpa', '2.odpb', '3.odpc', '4.odpd');";
            executeUpdate(sql);
            System.out.println("Tabela bazaPytan wypelniona danymi");
        } else
            System.out.println("Tabela bazaPytan juz istnieje!");

        if (executeUpdate("CREATE TABLE bazaodpowiedzi (idKlienta varchar(256) NOT NULL, idPytania int NOT NULL, odpowiedz varchar(20) NOT NULL);") == 0) {
            System.out.println("Tabela bazaOdpowiedzi utworzona");
        } else
            System.out.println("Tabela bazaOdpowiedzi juz istnieje!");

        if (executeUpdate("CREATE TABLE wyniki (idPytania int(10) NOT NULL,odp1 int(10) NOT NULL,odp2 int(10) NOT NULL,odp3 int(10) NOT NULL,odp4 int(10) NOT NULL);") == 0) {
            executeUpdate("ALTER TABLE wyniki ADD PRIMARY KEY (idPytania);");
            System.out.println("Tabela wyniki utworzona");
            String sql = "INSERT INTO wyniki VALUES" +
                    "(1, 0, 0, 0, 0), " +
                    "(2, 0, 0, 0, 0);";
            executeUpdate(sql);
            System.out.println("Tabela wyniki wypelniona danymi");
        } else
            System.out.println("Tabela wyniki juz istnieje!");

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

    public ResultSet getWyniki() {
        String sql = "Select * from wyniki;";
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

    public void insertOdpowiedz(String idKlienta, int idPytania, String odpowiedz) {
        String sql = "INSERT INTO bazaOdpowiedzi VALUES('" + idKlienta + "'," + idPytania + ",'" + odpowiedz + "');";
        executeUpdate(sql);
    }

    public void updateWyniki(int idPytania, String answer) {
        String odp = "odp" + answer;

        String sql = "Select " + odp + " from wyniki where idPytania =" + idPytania;
        int counter = 0;
        try {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                counter = rs.getInt(odp);
            }
            counter++;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "UPDATE wyniki set " + odp + " = " + counter + " where idPytania =" + idPytania;
        executeUpdate(sql);
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

