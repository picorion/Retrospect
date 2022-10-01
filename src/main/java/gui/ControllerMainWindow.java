package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Data;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerMainWindow implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerMainWindow.class);

    @FXML
    AnchorPane Parent;
    @FXML
    HBox SidebarImport;
    @FXML
    Label SidebarImportLabel;
    @FXML
    FontIcon SidebarImportIcon;
    @FXML
    HBox SidebarYourData;
    @FXML
    Label SidebarYourDataLabel;
    @FXML
    FontIcon SidebarYourDataIcon;
    @FXML
    HBox SidebarLibrary;
    @FXML
    Label SidebarLibraryLabel;
    @FXML
    FontIcon SidebarLibraryIcon;
    @FXML
    HBox SidebarPlaybacks;
    @FXML
    Label SidebarPlaybacksLabel;
    @FXML
    FontIcon SidebarPlaybacksIcon;
    @FXML
    HBox SidebarRanking;
    @FXML
    Label SidebarRankingLabel;
    @FXML
    FontIcon SidebarRankingIcon;
    @FXML
    HBox SidebarListeningTime;
    @FXML
    Label SidebarListeningTimeLabel;
    @FXML
    FontIcon SidebarListeningTimeIcon;
    @FXML
    HBox SidebarExtendedData;
    @FXML
    Label SidebarExtendedDataLabel;
    @FXML
    FontIcon SidebarExtendedDataIcon;
    @FXML
    Button Close;
    @FXML
    Button Minimize;
    @FXML
    Image Logo;
    @FXML
    Label Version;
    @FXML
    Label Author;


    /**
     * Loads all panes of the program
     */
    Pane importPane = FXMLLoader.load(getClass().getResource("/fxml/Import.fxml"));
    Pane yourDataPane = FXMLLoader.load(getClass().getResource("/fxml/YourData.fxml"));
    Pane libraryPane = FXMLLoader.load(getClass().getResource("/fxml/Library.fxml"));
    Pane playbacksPane = FXMLLoader.load(getClass().getResource("/fxml/Playbacks.fxml"));
    Pane rankingPane = FXMLLoader.load(getClass().getResource("/fxml/Ranking.fxml"));
    Pane listeningTimePane = FXMLLoader.load(getClass().getResource("/fxml/ListeningTime.fxml"));
    Pane extendedDataPane = FXMLLoader.load(getClass().getResource("/fxml/ExtendedData.fxml"));

    /**
     * Constructor to declare the IOException
     *
     * @throws IOException .
     */
    public ControllerMainWindow() throws IOException {
        /* required for declaring the possible throw of an IOException */
    }

    /**
     * Calls focusImport() to show the importPane on startup
     *
     * @param location  .
     * @param resources .
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //starts with the display of the import pane
        focusImport();
        //displays the current version and the author on the lower left
        Version.setText("v0.11.0"); //TODO: version
        Author.setText("by github.com/picorion");
        //switches between default and extended mode
        Data.getExtendedModeProperty().addListener(change -> {
            if (Data.getExtendedMode()) LOGGER.debug("extended mode enabled");
            else LOGGER.debug("default mode enabled");
            unfocusExtendedData();
        });
    }

    /**
     * Closes the window on which the event was triggered
     *
     * @param event ActionEvent needed to identify the stage
     */
    @FXML
    private void close(ActionEvent event) {
        LOGGER.debug("closing window");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /* Method for a maximize button
    @FXML
    private void maximize(ActionEvent event) {
        System.out.println(">Maximize<");
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (stage.getX() == bounds.getMinX() && stage.getY() == bounds.getMinY() && stage.getWidth() == bounds.getWidth() && stage.getHeight() == bounds.getHeight()) {
            stage.setX(x);
            stage.setY(y);
            stage.setWidth(1000);
            stage.setHeight(635);
        }
        else {
            x = stage.getX();
            y = stage.getY();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
        }
    }
    */

    /**
     * Minimizes the window on which the event was triggered
     *
     * @param event ActionEvent needed to identify the stage
     */
    @FXML
    private void minimize(ActionEvent event) {
        LOGGER.debug("minimizing window");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * Focuses the "import" pane by showing it in the parentPane of the mainWindow
     */
    @FXML
    private void focusImport() {
        LOGGER.debug("showing \"import\" pane");
        Parent.getChildren().clear();
        Parent.getChildren().add(importPane);
        SidebarImport.getStyleClass().clear();
        SidebarImport.getStyleClass().add("sidebarEntryActive");
        SidebarImportLabel.getStyleClass().clear();
        SidebarImportLabel.getStyleClass().add("mediumText");
        SidebarImportIcon.getStyleClass().clear();
        SidebarImportIcon.getStyleClass().add("iconDark");
        unfocusYourData();
        unfocusLibrary();
        unfocusPlaybacks();
        unfocusRanking();
        unfocusListeningTime();
        unfocusExtendedData();
    }

    /**
     * Unfocuses the "import" pane by resetting the style of the sidebar entry
     */
    private void unfocusImport() {
        SidebarImport.getStyleClass().clear();
        SidebarImport.getStyleClass().add("sidebarEntry");
        SidebarImportLabel.getStyleClass().clear();
        SidebarImportLabel.getStyleClass().add("mediumTextLight");
        SidebarImportIcon.getStyleClass().clear();
        SidebarImportIcon.getStyleClass().add("iconLight");
    }

    /**
     * Focuses the "your data" pane by showing it in the parentPane of the mainWindow
     */
    @FXML
    private void focusYourData() {
        if (!Data.importStatus.get()) {
            LOGGER.debug("showing \"your data\" pane");
            Parent.getChildren().clear();
            Parent.getChildren().add(yourDataPane);
            SidebarYourData.getStyleClass().clear();
            SidebarYourData.getStyleClass().add("sidebarEntryActive");
            SidebarYourDataLabel.getStyleClass().clear();
            SidebarYourDataLabel.getStyleClass().add("mediumText");
            SidebarYourDataIcon.getStyleClass().clear();
            SidebarYourDataIcon.getStyleClass().add("iconDark");
            unfocusImport();
            unfocusLibrary();
            unfocusPlaybacks();
            unfocusRanking();
            unfocusListeningTime();
            unfocusExtendedData();
        }
    }

    /**
     * Unfocuses the "your data" pane by resetting the style of the sidebar entry
     */
    private void unfocusYourData() {
        SidebarYourData.getStyleClass().clear();
        SidebarYourData.getStyleClass().add("sidebarEntry");
        SidebarYourDataLabel.getStyleClass().clear();
        SidebarYourDataLabel.getStyleClass().add("mediumTextLight");
        SidebarYourDataIcon.getStyleClass().clear();
        SidebarYourDataIcon.getStyleClass().add("iconLight");
    }

    /**
     * Focuses the "library" pane by showing it in the parentPane of the mainWindow
     */
    @FXML
    private void focusLibrary() {
        if (!Data.importStatus.get()) {
            LOGGER.debug("showing \"library\" pane");
            Parent.getChildren().clear();
            Parent.getChildren().add(libraryPane);
            SidebarLibrary.getStyleClass().clear();
            SidebarLibrary.getStyleClass().add("sidebarEntryActive");
            SidebarLibraryLabel.getStyleClass().clear();
            SidebarLibraryLabel.getStyleClass().add("mediumText");
            SidebarLibraryIcon.getStyleClass().clear();
            SidebarLibraryIcon.getStyleClass().add("iconDark");
            unfocusImport();
            unfocusYourData();
            unfocusPlaybacks();
            unfocusRanking();
            unfocusListeningTime();
            unfocusExtendedData();
        }
    }

    /**
     * Unfocuses the "library" pane by resetting the style of the sidebar entry
     */
    private void unfocusLibrary() {
        SidebarLibrary.getStyleClass().clear();
        SidebarLibrary.getStyleClass().add("sidebarEntry");
        SidebarLibraryLabel.getStyleClass().clear();
        SidebarLibraryLabel.getStyleClass().add("mediumTextLight");
        SidebarLibraryIcon.getStyleClass().clear();
        SidebarLibraryIcon.getStyleClass().add("iconLight");
    }

    /**
     * Focuses the "playbacks" pane by showing it in the parentPane of the mainWindow
     */
    @FXML
    private void focusPlaybacks() {
        if (!Data.importStatus.get()) {
            LOGGER.debug("showing \"playbacks\" pane");
            Parent.getChildren().clear();
            Parent.getChildren().add(playbacksPane);
            SidebarPlaybacks.getStyleClass().clear();
            SidebarPlaybacks.getStyleClass().add("sidebarEntryActive");
            SidebarPlaybacksLabel.getStyleClass().clear();
            SidebarPlaybacksLabel.getStyleClass().add("mediumText");
            SidebarPlaybacksIcon.getStyleClass().clear();
            SidebarPlaybacksIcon.getStyleClass().add("iconDark");
            unfocusImport();
            unfocusYourData();
            unfocusLibrary();
            unfocusRanking();
            unfocusListeningTime();
            unfocusExtendedData();
        }
    }

    /**
     * Unfocuses the "playbacks" pane by resetting the style of the sidebar entry
     */
    private void unfocusPlaybacks() {
        SidebarPlaybacks.getStyleClass().clear();
        SidebarPlaybacks.getStyleClass().add("sidebarEntry");
        SidebarPlaybacksLabel.getStyleClass().clear();
        SidebarPlaybacksLabel.getStyleClass().add("mediumTextLight");
        SidebarPlaybacksIcon.getStyleClass().clear();
        SidebarPlaybacksIcon.getStyleClass().add("iconLight");
    }

    /**
     * Focuses the "ranking" pane by showing it in the parentPane of the mainWindow
     */
    @FXML
    private void focusRanking() {
        if (!Data.importStatus.get()) {
            LOGGER.debug("showing \"ranking\" pane");
            Parent.getChildren().clear();
            Parent.getChildren().add(rankingPane);
            SidebarRanking.getStyleClass().clear();
            SidebarRanking.getStyleClass().add("sidebarEntryActive");
            SidebarRankingLabel.getStyleClass().clear();
            SidebarRankingLabel.getStyleClass().add("mediumText");
            SidebarRankingIcon.getStyleClass().clear();
            SidebarRankingIcon.getStyleClass().add("iconDark");
            unfocusImport();
            unfocusYourData();
            unfocusLibrary();
            unfocusPlaybacks();
            unfocusListeningTime();
            unfocusExtendedData();
        }
    }

    /**
     * Unfocuses the "ranking" pane by resetting the style of the sidebar entry
     */
    private void unfocusRanking() {
        SidebarRanking.getStyleClass().clear();
        SidebarRanking.getStyleClass().add("sidebarEntry");
        SidebarRankingLabel.getStyleClass().clear();
        SidebarRankingLabel.getStyleClass().add("mediumTextLight");
        SidebarRankingIcon.getStyleClass().clear();
        SidebarRankingIcon.getStyleClass().add("iconLight");
    }

    /**
     * Focuses the "listening time" pane by showing it in the parentPane of the mainWindow
     */
    @FXML
    private void focusListeningTime() {
        if (!Data.importStatus.get()) {
            LOGGER.debug("showing \2listening time\" pane");
            Parent.getChildren().clear();
            Parent.getChildren().add(listeningTimePane);
            SidebarListeningTime.getStyleClass().clear();
            SidebarListeningTime.getStyleClass().add("sidebarEntryActive");
            SidebarListeningTimeLabel.getStyleClass().clear();
            SidebarListeningTimeLabel.getStyleClass().add("mediumText");
            SidebarListeningTimeIcon.getStyleClass().clear();
            SidebarListeningTimeIcon.getStyleClass().add("iconDark");
            unfocusImport();
            unfocusYourData();
            unfocusLibrary();
            unfocusPlaybacks();
            unfocusRanking();
            unfocusExtendedData();
        }
    }

    /**
     * Unfocuses the "listening time" pane by resetting the style of the sidebar entry
     */
    private void unfocusListeningTime() {
        SidebarListeningTime.getStyleClass().clear();
        SidebarListeningTime.getStyleClass().add("sidebarEntry");
        SidebarListeningTimeLabel.getStyleClass().clear();
        SidebarListeningTimeLabel.getStyleClass().add("mediumTextLight");
        SidebarListeningTimeIcon.getStyleClass().clear();
        SidebarListeningTimeIcon.getStyleClass().add("iconLight");
    }

    /**
     * Focuses the "extended data" pane by showing it in the parentPane of the mainWindow
     */
    @FXML
    private void focusExtendedData() {
        if (!Data.importStatus.get() && Data.getExtendedMode()) {
            LOGGER.debug("showing \"extended data\" pane");
            Parent.getChildren().clear();
            Parent.getChildren().add(extendedDataPane);
            SidebarExtendedData.getStyleClass().clear();
            SidebarExtendedData.getStyleClass().add("sidebarEntryActive");
            SidebarExtendedDataLabel.getStyleClass().clear();
            SidebarExtendedDataLabel.getStyleClass().add("mediumText");
            SidebarExtendedDataIcon.getStyleClass().clear();
            SidebarExtendedDataIcon.getStyleClass().add("iconDark");
            unfocusImport();
            unfocusYourData();
            unfocusLibrary();
            unfocusPlaybacks();
            unfocusRanking();
            unfocusListeningTime();
        }
    }

    /**
     * Unfocuses the "extended data" pane by resetting the style of the sidebar entry
     */
    private void unfocusExtendedData() {
        if (Data.getExtendedMode()) {
            SidebarExtendedData.getStyleClass().clear();
            SidebarExtendedData.getStyleClass().add("sidebarEntry");
            SidebarExtendedDataLabel.getStyleClass().clear();
            SidebarExtendedDataLabel.getStyleClass().add("mediumTextLight");
            SidebarExtendedDataIcon.getStyleClass().clear();
            SidebarExtendedDataIcon.getStyleClass().add("iconLight");
        } else {
            SidebarExtendedData.getStyleClass().clear();
            SidebarExtendedData.getStyleClass().add("sidebarEntryDisabled");
            SidebarExtendedDataLabel.getStyleClass().clear();
            SidebarExtendedDataLabel.getStyleClass().add("mediumText");
            SidebarExtendedDataIcon.getStyleClass().clear();
            SidebarExtendedDataIcon.getStyleClass().add("iconDark");
        }
    }

}
