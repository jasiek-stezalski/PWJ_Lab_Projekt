import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Application {

    static final int serverPort = 50000;

    public static void main(String[] args) throws IOException {
        launch(args);

//        try {
//            // Getting localhost
//            InetAddress serverHost = InetAddress.getLocalHost();
//            // Attempt to connect to the server
//            Socket socket = new Socket(serverHost, serverPort);
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            PrintStream out = new PrintStream(socket.getOutputStream());
//
//            Scanner scanner = new Scanner(System.in);
//            String temp;
//            while (true) {
//                while (!(temp = in.readLine()).equals("stop")) {
//                    System.out.println(temp);
//                }
//                String answer;
//                do {
//                    answer = scanner.nextLine();
//                } while (!answer.equals("1") && !answer.equals("2") && !answer.equals("3") && !answer.equals("4"));
//                out.println(answer);
//                temp = in.readLine();
//                if (temp.equals("exit"))
//                    break;
//            }
//
//            while (true) {
//                temp = in.readLine();
//                if (temp.equals("exit"))
//                    break;
//                System.out.println(temp);
//            }
//            scanner.close();
//            out.close();
//            in.close();
//            socket.close();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (ConnectException e) {
//            System.err.println(e);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            System.out.println("Connection closed");
//        }


    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        InetAddress serverHost = InetAddress.getLocalHost();
        // Attempt to connect to the server
        Socket socket = new Socket(serverHost, serverPort);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintStream out = new PrintStream(socket.getOutputStream());


        VBox layout = new VBox(20);
        Label question = new Label(in.readLine());
        RadioButton answer1 = new RadioButton(in.readLine());
        RadioButton answer2 = new RadioButton(in.readLine());
        RadioButton answer3 = new RadioButton(in.readLine());
        RadioButton answer4 = new RadioButton(in.readLine());
        Button button = new Button("Zatwierź");
        ToggleGroup question1 = new ToggleGroup();

        answer1.setToggleGroup(question1);
        answer2.setToggleGroup(question1);
        answer3.setToggleGroup(question1);
        answer4.setToggleGroup(question1);

        button.setDisable(true);

        answer1.setOnAction(e -> button.setDisable(false));
        answer2.setOnAction(e -> button.setDisable(false));
        answer3.setOnAction(e -> button.setDisable(false));
        answer4.setOnAction(e -> button.setDisable(false));

        button.setOnAction(e -> {
            if (answer1.isSelected()) {
                out.println(1);
                button.setDisable(true);
            } else if (answer2.isSelected()) {
                out.println(2);
                button.setDisable(true);
            } else if (answer3.isSelected()) {
                out.println(3);
                button.setDisable(true);
            } else if (answer4.isSelected()) {
                out.println(4);
                button.setDisable(true);
            }

            try {
                String temp = in.readLine();
                if (!temp.equals("exit")) {
                    question.setText(temp);
                    answer1.setText(in.readLine());
                    answer2.setText(in.readLine());
                    answer3.setText(in.readLine());
                    answer4.setText(in.readLine());
                } else {
                    layout.getChildren().removeAll(question, answer1, answer2, answer3, answer4, button);

                    ObservableList<PieChart.Data> pieChartData;
                    PieChart chart;
                    int counter = 1;
                    HBox szukanaHBox = new HBox(5);
                    while (true) {
                        temp = in.readLine();
                        if (temp.equals("exit")) {
                            break;
                        }
                        pieChartData = FXCollections.observableArrayList(
                                new PieChart.Data("A " + temp + "%", Integer.parseInt(temp)),
                                new PieChart.Data("B " + (temp = in.readLine()) + "%", Integer.parseInt(temp)),
                                new PieChart.Data("C " + (temp = in.readLine()) + "%", Integer.parseInt(temp)),
                                new PieChart.Data("D " + (temp = in.readLine()) + "%", Integer.parseInt(temp)));
                        chart = new PieChart(pieChartData);
                        chart.setTitle("Pytanie nr " + counter++);
                        szukanaHBox.getChildren().add(chart);

                    }
                    layout.getChildren().addAll(szukanaHBox);
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        layout.getChildren().addAll(question, answer1, answer2, answer3, answer4, button);
        layout.setAlignment(Pos.CENTER);
        // Tworzymy Scene
        Scene scene = new Scene(layout, 1200, 600);
        // Tutuł okna
        primaryStage.setTitle("Ankieta");
        // Ustawianie sceny
        primaryStage.setScene(scene);
        // Pokaż
        primaryStage.show();
    }
}
