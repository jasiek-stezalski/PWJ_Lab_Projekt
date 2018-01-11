import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Pattern;


public class Server extends Application {

    private ServerThread thread;

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        thread = new ServerThread();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        final Text sceneTitle = new Text("Panel Servera");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        final Label serverPort = new Label("Podaj port: ");
        grid.add(serverPort, 0, 2);

        TextField portField = new TextField();
        grid.add(portField, 1, 2);

        final Label errorMessage = new Label();
        grid.add(errorMessage, 1, 6);

        Button turnOnButton = new Button("Uruchom server");
        Button turnOffButton = new Button("Wyłącz server");
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().add(turnOnButton);
        grid.add(hBox, 1, 4);

        turnOnButton.setOnAction(e -> {
            String regex = "[0-9]+";
            if (Pattern.matches(regex, portField.getText().toString())) {
                thread.setServerPort(Integer.parseInt(portField.getText().toString()));
                thread.start();
                grid.getChildren().removeAll(portField, serverPort, errorMessage);
                hBox.getChildren().remove(turnOnButton);
                hBox.getChildren().add(turnOffButton);
                hBox.setAlignment(Pos.CENTER);
            } else {
                errorMessage.setText("Podałeś błędny port!");
                errorMessage.setTextFill(Color.RED);
            }
        });

        turnOffButton.setOnAction(event -> {
            try {
                thread.getServerSocket().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        Scene scene = new Scene(grid, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Server");
        primaryStage.show();
    }
}
