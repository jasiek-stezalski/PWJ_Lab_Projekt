import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.regex.Pattern;

public class Client extends Application {

    private Socket socket;
    private BufferedReader in;
    private PrintStream out;

    @Override
    public void start(Stage primaryStage) {

        // First panel
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        final Text sceneTitle = new Text("Panel Klienta");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        final Label serverPort = new Label("Podaj port: ");
        grid.add(serverPort, 0, 2);

        TextField portField = new TextField();
        grid.add(portField, 1, 2);

        final Label serverHost = new Label("Podaj adres servera: ");
        grid.add(serverHost, 0, 3);

        TextField hostField = new TextField();
        grid.add(hostField, 1, 3);

        final Label errorMessage = new Label();
        grid.add(errorMessage, 1, 7);

        Button turnOnButton = new Button("Połącz");
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().add(turnOnButton);
        grid.add(hBox, 1, 5);

        // Second panel
        VBox layout = new VBox(20);
        Label question = new Label();
        RadioButton answer1 = new RadioButton();
        RadioButton answer2 = new RadioButton();
        RadioButton answer3 = new RadioButton();
        RadioButton answer4 = new RadioButton();
        Button button = new Button("Zatwierź");

        question.setFont(Font.font(null, FontWeight.BOLD, 20));
        answer1.setFont(new Font(16));
        answer2.setFont(new Font(16));
        answer3.setFont(new Font(16));
        answer4.setFont(new Font(16));

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
        button.setDisable(true);

        layout.getChildren().addAll(question, answer1, answer2, answer3, answer4, button);
        layout.setAlignment(Pos.CENTER);

        turnOnButton.setOnAction(e -> {
            String portRegex = "[0-9]+";
            String hostRegex = "([0-9]{1,3}.){3}[0-9]{1,3}";
            if (Pattern.matches(portRegex, portField.getText().toString()) && Pattern.matches(hostRegex, hostField.getText().toString())) {
                try {
                    socket = new Socket(hostField.getText().toString(), Integer.parseInt(portField.getText().toString()));
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintStream(socket.getOutputStream());
                    hBox.getChildren().remove(turnOnButton);
                    grid.getChildren().removeAll(sceneTitle, serverPort, portField, serverHost, hostField, hBox, errorMessage);

                    question.setText(in.readLine());
                    answer1.setText(in.readLine());
                    answer2.setText(in.readLine());
                    answer3.setText(in.readLine());
                    answer4.setText(in.readLine());

                    grid.getChildren().setAll(layout);

                } catch (IOException e1) {
                    errorMessage.setText("Nie udało się połączyć z serverem!");
                    errorMessage.setTextFill(Color.RED);
                }
            } else {
                errorMessage.setText("Podałeś błędne dane!");
                errorMessage.setTextFill(Color.RED);
            }
        });

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

        Scene scene = new Scene(grid, 1200, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ankieta");
        primaryStage.show();
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
