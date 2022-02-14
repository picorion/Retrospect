package gui;

import analysis.Import;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.Data;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerImport implements Initializable {

    @FXML
    Button ImportButton;
    @FXML
    ProgressIndicator ProgressIndicator;
    @FXML
    FontIcon StatusIcon;
    @FXML
    Button Choose;
    @FXML
    TextField ImportTextField;
    @FXML
    Slider ImportSlider;
    @FXML
    Label ImportSliderDisplay;
    @FXML
    TextArea TextOverview;
    @FXML
    Label ImportedArtists;
    @FXML
    Label ImportedTracks;
    @FXML
    Label ImportedPlaybacks;
    @FXML
    CheckBox ExtendedModeCheckBox;

    /**
     * Initializes the slider and the related label
     *
     * @param location  .
     * @param resources .
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //updates the import slider value display if the import slider gets changed
        ImportSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> ImportSliderDisplay.textProperty().setValue(String.valueOf(newValue.intValue())));

        //updates the text overview in the ui
        Data.getTextOverviewPuffer().addListener(change -> Platform.runLater(() -> TextOverview.setText(Data.getTextOverviewPuffer().getValue())));

        //updates the labels displaying the number of imported data
        Data.getImportedArtists().addListener(change -> Platform.runLater(() -> ImportedArtists.setText(String.valueOf(Data.getImportedArtists().getValue()))));
        Data.getImportedTracks().addListener(change -> Platform.runLater(() -> ImportedTracks.setText(String.valueOf(Data.getImportedTracks().getValue()))));
        Data.getImportedPlaybacks().addListener(change -> Platform.runLater(() -> ImportedPlaybacks.setText(String.valueOf(Data.getImportedPlaybacks().getValue()))));

        //updates the extended mode property if the user activates or disables the extended mode and clears all data
        ExtendedModeCheckBox.selectedProperty().addListener(change -> {
            Data.clearTextOverview();
            Data.swapExtendedMode();
            Data.clearData();
        });
    }

    /**
     * Handles import button
     */
    @FXML
    private void handleImport() {
        Data.importStatus.set(true);
        System.out.println(">Import<");
        //clears the text overview
        Data.clearTextOverview();
        ImportTextField.getStyleClass().remove("textFieldError");
        StatusIcon.getStyleClass().clear();
        StatusIcon.getStyleClass().add("iconLight");
        ProgressIndicator.setVisible(true);

        Thread thread = new Thread(runnable);
        thread.start();
    }

    Runnable runnable =
            new Runnable() {
                public void run() {
                    ExtendedModeCheckBox.setDisable(true);
                    String text = ImportTextField.getText();
                    //uses the current directory if no input was made
                    if (text.equals("")) text = System.getProperty("user.dir");
                    File directory = new File(text);
                    int requiredPlaybackTime = (int) ImportSlider.getValue();
                    Data.updateTextOverview("→\tdirectory: " + directory + "\n");
                    if (!directory.exists()) {
                        Data.updateTextOverview("❌\tdirectory does not exist\n");
                        ImportTextField.getStyleClass().add("textFieldError");
                        StatusIcon.setIconLiteral("fas-exclamation-triangle");
                        StatusIcon.getStyleClass().clear();
                        StatusIcon.getStyleClass().add("iconError");
                    } else if (Import.containsSpotifyExportData(directory)) {
                        Data.updateTextOverview("→\timporting data...\n");
                        Import.importData(directory, requiredPlaybackTime);
                        StatusIcon.setIconLiteral("fas-check");
                        StatusIcon.getStyleClass().clear();
                        StatusIcon.getStyleClass().add("iconSuccess");
                    } else {
                        Data.updateTextOverview("❌\tdirectory does not contain needed files\n");
                        ImportTextField.getStyleClass().add("textFieldError");
                        StatusIcon.setIconLiteral("fas-exclamation-triangle");
                        StatusIcon.getStyleClass().clear();
                        StatusIcon.getStyleClass().add("iconError");
                    }
                    Data.updateTextOverview("\n");
                    ProgressIndicator.setVisible(false);
                    Data.importStatus.set(false);
                    Platform.runLater(() -> {       //enables the extended mode checkbox after the import finishes
                        ExtendedModeCheckBox.setDisable(false);
                    });
                }
            };

    /**
     * Uses DirectoryChooser to let the user choose a directory and get their path
     *
     * @param event button press event
     */
    @FXML
    private void handleChoose(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        directoryChooser.setTitle("Choose MyData Directory");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File directory = directoryChooser.showDialog(stage);
        if (directory != null) ImportTextField.setText(directory.toString());
    }

}
