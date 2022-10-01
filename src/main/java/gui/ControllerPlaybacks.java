package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Data;
import model.Playback;
import model.PlaybackDatabase;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerPlaybacks implements Initializable {

    @FXML
    TableView<Playback> Table;
    @FXML
    TableColumn<Playback, String> ColArtist;
    @FXML
    TableColumn<Playback, String> ColTrack;
    @FXML
    TableColumn<Playback, String> ColDate;
    @FXML
    TableColumn<Playback, String> ColListeningTime;
    @FXML
    TextField Filter;

    ObservableList<Playback> entries = FXCollections.observableArrayList();

    /**
     * Initializes the table
     *
     * @param location  does not get used
     * @param resources does not get used
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ColArtist.setCellValueFactory(param -> param.getValue().getArtist().getArtistNameProperty());
        ColTrack.setCellValueFactory(param -> param.getValue().getTrack().getTitleProperty());
        ColDate.setCellValueFactory(param -> param.getValue().getDateTimeProperty());
        ColDate.setStyle("-fx-alignment: CENTER-RIGHT;");
        ColListeningTime.setCellValueFactory(param -> param.getValue().getListeningTimeProperty());
        ColListeningTime.setStyle("-fx-alignment: CENTER-RIGHT;");

        /* Creates a comparator to sort the listening times correctly */
        ColListeningTime.setComparator((dataA, dataB) -> {
            if (dataA.equals(dataB)) return 0;
            long valueA = Long.parseLong(dataA.substring(dataA.length() - 2)) + Long.parseLong(dataA.substring(0, dataA.length() - 3)) * 60;
            long valueB = Long.parseLong(dataB.substring(dataB.length() - 2)) + Long.parseLong(dataB.substring(0, dataB.length() - 3)) * 60;
            return Long.compare(valueB, valueA);
        });

        Data.playbacksChangeEvent.addListener(change -> {
            importEntries();
            Data.playbacksChangeEvent.setValue(false);
            //Don't know why this works or is needed but the event listener stops working without this line
            Data.playbacksChangeEvent.getValue();
        });

        Filter.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Filter.getText().isEmpty()) {
                Table.getItems().clear();
                Table.getItems().addAll(entries);
            } else {
                Table.getItems().clear();
                Table.getItems().addAll(filterList(entries, Filter.getText()));
            }
        });
    }

    /**
     * Deletes the search text from the filter
     */
    public void handleDeleteIcon() {
        Filter.setText("");
    }

    /**
     * Adds playbacks from the database to a list and adds it to the table
     */
    void importEntries() {
        Table.getItems().clear();
        entries.clear();
        for (PlaybackDatabase database : Data.playbackDatabase) {
            entries.addAll(database.getPlaybacks());
        }
        Table.getItems().setAll(entries);
    }

    /**
     * Creates a list only containing artists and artists with tracks containing a search text
     *
     * @param entries    list with all playbacks
     * @param searchText text to look for
     * @return list with entries containing the search text
     */
    private ObservableList<Playback> filterList(ObservableList<Playback> entries, String searchText) {
        List<Playback> filteredList = new ArrayList<>();
        for (Playback playback : entries) {
            if (entryContains(playback, searchText)) filteredList.add(playback);
        }
        return FXCollections.observableList(filteredList);
    }

    /**
     * Checks if an entry contains a search text
     *
     * @param entry      entry to check
     * @param searchText text to look for
     * @return true if entry contains the search text
     */
    private boolean entryContains(Playback entry, String searchText) {
        return (entry.getArtist().getArtistName().toLowerCase().contains(searchText.toLowerCase())) ||
                (entry.getTrack().getTitle().toLowerCase().contains(searchText.toLowerCase())) ||
                (entry.getDateTime().toString().contains(searchText)) ||
                (entry.getListeningTimeProperty().getValue().contains(searchText));
    }

}
