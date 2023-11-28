package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.io.IOException;

public class FXMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage)  {
        try {
            URL url = getClass().getResource("MereniMain.fxml");
            if (url != null) {
                Parent root = FXMLLoader.load(url);
                stage.setScene(new Scene(root));
                stage.setTitle("ProgMereni");
                stage.show();
            } else {
                System.err.println("Chyba při získávání URL z .fxml soubrou");
            }
        } catch (IOException ex) {
            System.err.println("Chyba při práci s .fxml souborem");
        }

    }
}
