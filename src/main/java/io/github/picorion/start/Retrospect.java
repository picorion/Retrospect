package io.github.picorion.start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <strong>Retrospect</strong> <br>
 * Analyse your personal data packages from Spotify
 *
 * @author picorion (<a href="https://github.com/picorion/Retrospect">GitHub</a>)
 * @version v0.11.0 - 14-02-2022
 */

public class Retrospect extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Retrospect.class);

    private double xOffset = 0;
    private double yOffset = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        LOGGER.info("\n\n$$$$$$$\\             $$\\                                                                   $$\\     \n" +
                "$$  __$$\\            $$ |                                                                  $$ |    \n" +
                "$$ |  $$ | $$$$$$\\ $$$$$$\\    $$$$$$\\   $$$$$$\\   $$$$$$$\\  $$$$$$\\   $$$$$$\\   $$$$$$$\\ $$$$$$\\   \n" +
                "$$$$$$$  |$$  __$$\\\\_$$  _|  $$  __$$\\ $$  __$$\\ $$  _____|$$  __$$\\ $$  __$$\\ $$  _____|\\_$$  _|  \n" +
                "$$  __$$< $$$$$$$$ | $$ |    $$ |  \\__|$$ /  $$ |\\$$$$$$\\  $$ /  $$ |$$$$$$$$ |$$ /        $$ |    \n" +
                "$$ |  $$ |$$   ____| $$ |$$\\ $$ |      $$ |  $$ | \\____$$\\ $$ |  $$ |$$   ____|$$ |        $$ |$$\\ \n" +
                "$$ |  $$ |\\$$$$$$$\\  \\$$$$  |$$ |      \\$$$$$$  |$$$$$$$  |$$$$$$$  |\\$$$$$$$\\ \\$$$$$$$\\   \\$$$$  |\n" +
                "\\__|  \\__| \\_______|  \\____/ \\__|       \\______/ \\_______/ $$  ____/  \\_______| \\_______|   \\____/ \n" +
                "                                                           $$ |                                    \n" +
                "                                                           $$ |                                    \n" +
                "                                                           \\__|\n");
        //TODO: indicate version

        primaryStage.getIcons().addAll(
                new Image("icons/icon_x48.png"),
                new Image("icons/icon_x32.png"),
                new Image("icons/icon_x16.png")
        );

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainWindow.fxml"));
        Scene scene = new Scene(root);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Retrospect"); //TODO: indicate version

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
