package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import model.Artist;
import model.Data;
import model.MusicData;
import model.Track;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerLibrary implements Initializable {

    @FXML
    TreeTableView<MusicData> Table;
    @FXML
    TreeTableColumn<MusicData, String> ColArtist;
    @FXML
    TreeTableColumn<MusicData, String> ColTrack;
    @FXML
    TreeTableColumn<MusicData, Number> ColPlaybacks;
    @FXML
    TreeTableColumn<MusicData, String> ColListeningTime;
    @FXML
    TextField Filter;

    TreeItem<MusicData> root = new TreeItem<>(new Artist("root", null, false));
    ObservableList<TreeItem<MusicData>> entries = FXCollections.observableArrayList();

    /**
     * Initializes the columns and table, implements a listener for library changes and a filter to filter the entries
     *
     * @param location  .
     * @param resources .
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ColArtist.setCellValueFactory(param -> param.getValue().getValue().getArtistNameProperty());
        ColTrack.setCellValueFactory(param -> param.getValue().getValue().getTitleProperty());
        ColPlaybacks.setCellValueFactory(param -> param.getValue().getValue().getTotalPlaybacksProperty());
        ColPlaybacks.setStyle("-fx-alignment: CENTER-RIGHT;");
        ColListeningTime.setCellValueFactory(param -> param.getValue().getValue().getTotalListeningTimeProperty());
        ColListeningTime.setStyle("-fx-alignment: CENTER-RIGHT;");

        /* Creates a comparator to sort the tracks correctly by observing track numbers and names */
        ColTrack.setComparator((dataA, dataB) -> {
            if (isNumeric(dataA) && isNumeric(dataB)) {
                return Integer.compare(Integer.parseInt(dataA), Integer.parseInt(dataB));
            }
            else {
                return dataB.compareToIgnoreCase(dataA);
            }
        });

        /* Creates a comparator to sort the listening times correctly */
        ColListeningTime.setComparator((dataA, dataB) -> {
            if (dataA.equals(dataB)) return 0;
            long valueA = Long.parseLong(dataA.substring(dataA.length()-2)) + Long.parseLong(dataA.substring(0, dataA.length()-3)) * 60;
            long valueB = Long.parseLong(dataB.substring(dataB.length()-2)) + Long.parseLong(dataB.substring(0, dataB.length()-3)) * 60;
            return Long.compare(valueB, valueA);
        });

        Data.libraryChangeEvent.addListener(change -> {
            importEntries();
            Data.libraryChangeEvent.setValue(false);
            //Dont know why this works or is needed but the event listener stops working without this line
            Data.libraryChangeEvent.getValue();
        });

        Filter.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Filter.getText().isEmpty()) {
                root.getChildren().clear();
                root.getChildren().addAll(entries);
            } else {
                root.getChildren().clear();
                root.getChildren().addAll(filterList(entries, Filter.getText()));
            }
        });

        Table.setRoot(root);
        Table.setShowRoot(false);
    }

    /**
     * Checks if a string is numeric
     * @param text given string value
     * @return true if the string is totally numeric
     */
    public static boolean isNumeric(String text) {
        if (text == null || text.length() == 0) {
            return false;
        }
        for (char c : text.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Deletes the search text from the filter
     */
    @FXML
    private void handleDeleteIcon() {
        Filter.setText("");
    }

    /**
     * Adds artists and their tracks from the library to a list and adds it to the table
     */
    private void importEntries() {
        root.getChildren().clear();
        entries.clear();
        for (Artist artist : Data.library) {
            TreeItem<MusicData> entry = new TreeItem<>(artist);
            for (Track track : artist.getTracks()) {
                TreeItem<MusicData> childEntry = new TreeItem<>(track);
                entry.getChildren().add(childEntry);
            }
            entries.add(entry);
        }
        root.getChildren().addAll(entries);
    }

    /**
     * Checks if an entry (artist or track does not matter) contains a search text
     *
     * @param entry      entry to check
     * @param searchText text to look for
     * @return true if entry contains the search text
     */
    private boolean entryContains(TreeItem<MusicData> entry, String searchText) {
        return (entry.getValue().getArtistName().toLowerCase().contains(searchText.toLowerCase())) ||
                (entry.getValue().getTitle().toLowerCase().contains(searchText.toLowerCase())) ||
                (entry.getValue().getTotalPlaybacksProperty().getValue().toString().contains(searchText)) ||
                (entry.getValue().getTotalListeningTimeProperty().getValue().contains(searchText));
    }

    /**
     * Creates a list only containing artists and artists with tracks containing a search text
     *
     * @param list       list with all entries
     * @param searchText text to look for
     * @return list with entries containing the search text
     */
    private ObservableList<TreeItem<MusicData>> filterList(ObservableList<TreeItem<MusicData>> list, String searchText) {
        List<TreeItem<MusicData>> filteredList = new ArrayList<>();
        for (TreeItem<MusicData> entry : list) {
            MusicData filteredEntryData = entry.getValue();
            TreeItem<MusicData> filteredEntry = new TreeItem<>(filteredEntryData);
            boolean wanted = entryContains(entry, searchText);
            boolean filteredEntryAdded = false;
            if (wanted) filteredList.add(entry);
            else {
                for (TreeItem<MusicData> childEntry : entry.getChildren()) {
                    if (entryContains(childEntry, searchText)) {
                        if (!filteredEntryAdded) {
                            filteredList.add(filteredEntry);
                            filteredEntryAdded = true;
                        }
                        filteredEntry.getChildren().add(childEntry);
                    }
                }
            }
        }
        return FXCollections.observableList(filteredList);
    }

}
