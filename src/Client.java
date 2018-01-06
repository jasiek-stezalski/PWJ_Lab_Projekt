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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Application {

    static final int serverPort = 50000;
    InetAddress serverHost;
    Socket socket;
    BufferedReader in;
    PrintStream out;

    /**
     * The method connects to Socket
     */
    @Override
    public void init() throws Exception {
        super.init();
        serverHost = InetAddress.getLocalHost();
        socket = new Socket(serverHost, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintStream(socket.getOutputStream());
    }

    @Override
    public void start(Stage primaryStage) {

        try {
            // Create all labels and buttons
            VBox layout = new VBox(20);
            Label question = new Label(in.readLine());
            question.setFont(Font.font(null, FontWeight.BOLD, 20));
            RadioButton answer1 = new RadioButton(in.readLine());
            answer1.setFont(new Font(16));
            RadioButton answer2 = new RadioButton(in.readLine());
            answer2.setFont(new Font(16));
            RadioButton answer3 = new RadioButton(in.readLine());
            answer3.setFont(new Font(16));
            RadioButton answer4 = new RadioButton(in.readLine());
            answer4.setFont(new Font(16));
            Button button = new Button("Zatwierź");

            // Merge RadioButtons into one group
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

            // Reaction to a click the button
            button.setOnAction(e -> {
                if (answer1.isSelected()) {
                    // Send answer to database
                    out.println("A");
                    answer1.setSelected(false);
                    button.setDisable(true);
                } else if (answer2.isSelected()) {
                    // Send answer to database
                    out.println("B");
                    answer2.setSelected(false);
                    button.setDisable(true);
                } else if (answer3.isSelected()) {
                    // Send answer to database
                    out.println("C");
                    answer3.setSelected(false);
                    button.setDisable(true);
                } else if (answer4.isSelected()) {
                    // Send answer to database
                    out.println("D");
                    answer4.setSelected(false);
                    button.setDisable(true);
                }

                // Change the question to the next
                try {
                    String temp = in.readLine();
                    if (!temp.equals("exit")) {
                        question.setText(temp);
                        answer1.setText(in.readLine());
                        answer2.setText(in.readLine());
                        answer3.setText(in.readLine());
                        answer4.setText(in.readLine());
                    } else {
                        // If there are no more question, show the result of the questionnaire

                        // Clean the Scene from buttons
                        layout.getChildren().removeAll(question, answer1, answer2, answer3, answer4, button);

                        // Create the pie chart with result
                        ObservableList<PieChart.Data> pieChartData;
                        PieChart chart;
                        int counter = 1;
                        HBox hBox = new HBox(5);
                        Label result = new Label("Wyniki ankiety");
                        result.setFont(Font.font(null, FontWeight.BOLD, 20));

                        while (true) {
                            temp = in.readLine();
                            if (temp.equals("exit"))
                                break;

                            // Add answers from the database to the pie chart
                            pieChartData = FXCollections.observableArrayList(
                                    new PieChart.Data("A " + temp + "%", Integer.parseInt(temp)),
                                    new PieChart.Data("B " + (temp = in.readLine()) + "%", Integer.parseInt(temp)),
                                    new PieChart.Data("C " + (temp = in.readLine()) + "%", Integer.parseInt(temp)),
                                    new PieChart.Data("D " + (temp = in.readLine()) + "%", Integer.parseInt(temp)));
                            chart = new PieChart(pieChartData);
                            chart.setTitle("Pytanie nr " + counter++ + "\n\nTwoja odpowiedź : " + in.readLine());
                            hBox.getChildren().add(chart);
                        }
                        layout.getChildren().addAll(result, hBox);
                    }

                } catch (IOException el) {
                    el.printStackTrace();
                }
            });

            layout.getChildren().addAll(question, answer1, answer2, answer3, answer4, button);
            layout.setAlignment(Pos.CENTER);
            // Create Scene
            Scene scene = new Scene(layout, 1200, 600);
            // Set window title
            primaryStage.setTitle("Ankieta");
            // Set Scene
            primaryStage.setScene(scene);
            // Show Scene
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        out.close();
        in.close();
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

}
