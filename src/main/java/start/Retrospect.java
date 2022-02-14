package start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * RETROSPECT
 * Analyse your personal data packages from Spotify
 *
 * @author picorion - https://github.com/picorion/Retrospect
 * @version v0.11.0 - 14-02-2022
 */

public class Retrospect extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        System.out.println("-> Retrospect v0.11.0 <-\n");

        primaryStage.getIcons().addAll(
                new Image("icons/icon_x48.png"),
                new Image("icons/icon_x32.png"),
                new Image("icons/icon_x16.png")
        );

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainWindow.fxml"));
        Scene scene = new Scene(root);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Retrospect - Alpha Build");

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        primaryStage.setScene(scene);
        primaryStage.show();

    }

}
