package gui;

import analysis.RankingAnalysis;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import model.Data;
import model.RankingData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerRanking implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerRanking.class);

    @FXML
    DatePicker DateFrom;
    @FXML
    DatePicker DateUntil;
    @FXML
    ToggleButton ToggleListeningTime;
    @FXML
    ToggleButton TogglePlaybacks;
    @FXML
    Label Artist1;
    @FXML
    Label Artist2;
    @FXML
    Label Artist3;
    @FXML
    Label Artist4;
    @FXML
    Label Artist5;
    @FXML
    Label Artist6;
    @FXML
    Label Artist7;
    @FXML
    Label Artist8;
    @FXML
    Label Artist9;
    @FXML
    Label Artist10;
    @FXML
    Label Track1;
    @FXML
    Label Track2;
    @FXML
    Label Track3;
    @FXML
    Label Track4;
    @FXML
    Label Track5;
    @FXML
    Label Track6;
    @FXML
    Label Track7;
    @FXML
    Label Track8;
    @FXML
    Label Track9;
    @FXML
    Label Track10;

    /**
     * Generated ranking data
     */
    RankingData[] artistRanksByListeningTime = new RankingData[10];
    RankingData[] trackRanksByListeningTime = new RankingData[10];
    RankingData[] artistRanksByPlaybacks = new RankingData[10];
    RankingData[] trackRanksByPlaybacks = new RankingData[10];

    /**
     * Refreshes on library changes the top ten artists and tracks for the maximum period
     *
     * @param location  does not get used
     * @param resources does not get used
     */
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        //handles refresh after a new import
        Data.rankingChangeEvent.addListener(change -> {
            Data.rankingChangeEvent.setValue(false);
            artistRanksByListeningTime = new RankingData[10];
            trackRanksByListeningTime = new RankingData[10];
            artistRanksByPlaybacks = new RankingData[10];
            trackRanksByPlaybacks = new RankingData[10];
            refreshData();
            DateFrom.setValue(Data.getFirstPlaybackDate());
            DateUntil.setValue(Data.getLastPlaybackDate());
            handleRefresh();
            //Don't know why this works or is needed but the event listener stops working without this line
            Data.rankingChangeEvent.getValue();
        });

        ToggleListeningTime.setSelected(true);
        handleToggleListeningTime();
    }

    /**
     * Handles the Listening Time toggle switch by deselecting Playbacks and switching the ranking data
     */
    @FXML
    private void handleToggleListeningTime() {
        ToggleListeningTime.setDisable(true);
        TogglePlaybacks.setDisable(false);
        TogglePlaybacks.setSelected(false);
        refreshData();
    }

    /**
     * Handles the Playbacks toggle switch by deselecting Listening Time and switching the ranking data
     */
    @FXML
    private void handleTogglePlaybacks() {
        TogglePlaybacks.setDisable(true);
        ToggleListeningTime.setDisable(false);
        ToggleListeningTime.setSelected(false);
        refreshData();
    }

    /**
     * Refreshes the top ten artists and tracks for a given period and creates tooltips with the individual total listening time
     */
    @FXML
    private void handleRefresh() {
        LOGGER.debug("handling refresh of ranking");
        DateFrom.getStyleClass().remove("date-picker-error");
        DateUntil.getStyleClass().remove("date-picker-error");
        if (DateFrom.getValue() == null || DateUntil.getValue() == null) {
            DateFrom.getStyleClass().add("date-picker-error");
            DateUntil.getStyleClass().add("date-picker-error");
        } else {
            if (DateFrom.getValue().isBefore(DateUntil.getValue())) {   // !Data.library.isEmpty() &&
                RankingAnalysis.createRankingData(DateFrom.getValue(), DateUntil.getValue());
                artistRanksByListeningTime = RankingAnalysis.createArtistListeningTimeRanking();
                trackRanksByListeningTime = RankingAnalysis.createTrackListeningTimeRanking();
                artistRanksByPlaybacks = RankingAnalysis.createArtistPlaybackRanking();
                trackRanksByPlaybacks = RankingAnalysis.createTrackPlaybackRanking();
                refreshData();
            } else {
                DateFrom.getStyleClass().add("date-picker-error");
                DateUntil.getStyleClass().add("date-picker-error");
            }
        }
    }

    /**
     * Switches the ranking data to the data selected with the toggles
     */
    private void refreshData() {
        RankingData[] artistRanks;
        RankingData[] trackRanks;
        if (ToggleListeningTime.isSelected()) {
            artistRanks = artistRanksByListeningTime;
            trackRanks = trackRanksByListeningTime;
        } else {
            artistRanks = artistRanksByPlaybacks;
            trackRanks = trackRanksByPlaybacks;
        }

        Artist1.setText(artistRanks[0] == null ? "-" : artistRanks[0].getKey().getArtistName());
        if (artistRanks[0] != null)
            Tooltip.install(Artist1, new Tooltip("listening time: " + artistRanks[0].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + artistRanks[0].getPlaybacks()));
        Artist2.setText(artistRanks[1] == null ? "-" : artistRanks[1].getKey().getArtistName());
        if (artistRanks[1] != null)
            Tooltip.install(Artist2, new Tooltip("listening time: " + artistRanks[1].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + artistRanks[1].getPlaybacks()));
        Artist3.setText(artistRanks[2] == null ? "-" : artistRanks[2].getKey().getArtistName());
        if (artistRanks[2] != null)
            Tooltip.install(Artist3, new Tooltip("listening time: " + artistRanks[2].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + artistRanks[2].getPlaybacks()));
        Artist4.setText(artistRanks[3] == null ? "-" : artistRanks[3].getKey().getArtistName());
        if (artistRanks[3] != null)
            Tooltip.install(Artist4, new Tooltip("listening time: " + artistRanks[3].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + artistRanks[3].getPlaybacks()));
        Artist5.setText(artistRanks[4] == null ? "-" : artistRanks[4].getKey().getArtistName());
        if (artistRanks[4] != null)
            Tooltip.install(Artist5, new Tooltip("listening time: " + artistRanks[4].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + artistRanks[4].getPlaybacks()));
        Artist6.setText(artistRanks[5] == null ? "-" : artistRanks[5].getKey().getArtistName());
        if (artistRanks[5] != null)
            Tooltip.install(Artist6, new Tooltip("listening time: " + artistRanks[5].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + artistRanks[5].getPlaybacks()));
        Artist7.setText(artistRanks[6] == null ? "-" : artistRanks[6].getKey().getArtistName());
        if (artistRanks[6] != null)
            Tooltip.install(Artist7, new Tooltip("listening time: " + artistRanks[6].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + artistRanks[6].getPlaybacks()));
        Artist8.setText(artistRanks[7] == null ? "-" : artistRanks[7].getKey().getArtistName());
        if (artistRanks[7] != null)
            Tooltip.install(Artist8, new Tooltip("listening time: " + artistRanks[7].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + artistRanks[7].getPlaybacks()));
        Artist9.setText(artistRanks[8] == null ? "-" : artistRanks[8].getKey().getArtistName());
        if (artistRanks[8] != null)
            Tooltip.install(Artist9, new Tooltip("listening time: " + artistRanks[8].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + artistRanks[8].getPlaybacks()));
        Artist10.setText(artistRanks[9] == null ? "-" : artistRanks[9].getKey().getArtistName());
        if (artistRanks[9] != null)
            Tooltip.install(Artist10, new Tooltip("listening time: " + artistRanks[9].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + artistRanks[9].getPlaybacks()));

        Track1.setText(trackRanks[0] == null ? "-" : trackRanks[0].getKey().getTitle());
        if (trackRanks[0] != null)
            Tooltip.install(Track1, new Tooltip("by " + trackRanks[0].getArtistName() + "\nlistening time: " + trackRanks[0].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + trackRanks[0].getPlaybacks()));
        Track2.setText(trackRanks[1] == null ? "-" : trackRanks[1].getKey().getTitle());
        if (trackRanks[1] != null)
            Tooltip.install(Track2, new Tooltip("by " + trackRanks[1].getArtistName() + "\nlistening time: " + trackRanks[1].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + trackRanks[1].getPlaybacks()));
        Track3.setText(trackRanks[2] == null ? "-" : trackRanks[2].getKey().getTitle());
        if (trackRanks[2] != null)
            Tooltip.install(Track3, new Tooltip("by " + trackRanks[2].getArtistName() + "\nlistening time: " + trackRanks[2].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + trackRanks[2].getPlaybacks()));
        Track4.setText(trackRanks[3] == null ? "-" : trackRanks[3].getKey().getTitle());
        if (trackRanks[3] != null)
            Tooltip.install(Track4, new Tooltip("by " + trackRanks[3].getArtistName() + "\nlistening time: " + trackRanks[3].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + trackRanks[3].getPlaybacks()));
        Track5.setText(trackRanks[4] == null ? "-" : trackRanks[4].getKey().getTitle());
        if (trackRanks[4] != null)
            Tooltip.install(Track5, new Tooltip("by " + trackRanks[4].getArtistName() + "\nlistening time: " + trackRanks[4].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + trackRanks[4].getPlaybacks()));
        Track6.setText(trackRanks[5] == null ? "-" : trackRanks[5].getKey().getTitle());
        if (trackRanks[5] != null)
            Tooltip.install(Track6, new Tooltip("by " + trackRanks[5].getArtistName() + "\nlistening time: " + trackRanks[5].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + trackRanks[5].getPlaybacks()));
        Track7.setText(trackRanks[6] == null ? "-" : trackRanks[6].getKey().getTitle());
        if (trackRanks[6] != null)
            Tooltip.install(Track7, new Tooltip("by " + trackRanks[6].getArtistName() + "\nlistening time: " + trackRanks[6].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + trackRanks[6].getPlaybacks()));
        Track8.setText(trackRanks[7] == null ? "-" : trackRanks[7].getKey().getTitle());
        if (trackRanks[7] != null)
            Tooltip.install(Track8, new Tooltip("by " + trackRanks[7].getArtistName() + "\nlistening time: " + trackRanks[7].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + trackRanks[7].getPlaybacks()));
        Track9.setText(trackRanks[8] == null ? "-" : trackRanks[8].getKey().getTitle());
        if (trackRanks[8] != null)
            Tooltip.install(Track9, new Tooltip("by " + trackRanks[8].getArtistName() + "\nlistening time: " + trackRanks[8].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + trackRanks[8].getPlaybacks()));
        Track10.setText(trackRanks[9] == null ? "-" : trackRanks[9].getKey().getTitle());
        if (trackRanks[9] != null)
            Tooltip.install(Track10, new Tooltip("by " + trackRanks[9].getArtistName() + "\nlistening time: " + trackRanks[9].getListeningTimeFormatted() + " (HH:mm)\nplaybacks: " + trackRanks[9].getPlaybacks()));
        
    }
    
}
