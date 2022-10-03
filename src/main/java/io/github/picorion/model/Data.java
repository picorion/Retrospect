package io.github.picorion.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Contains all data
 */
public class Data {

    /**
     * Private constructor to prevent instantiation
     */
    private Data() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Data.class);

    //library containing the imported artists and their tracks
    public static ObservableList<Artist> library = FXCollections.observableArrayList();
    //contains all playbacks grouped by years and months
    public static ObservableList<PlaybackDatabase> playbackDatabase = FXCollections.observableArrayList();
    //clears all data
    public static void clearData() {
        LOGGER.debug("clearing data");
        updateTextOverview(" ℹ\talready imported streaming data got cleared\n");
        firstPlaybackDate = null;
        lastPlaybackDate = null;
        library.clear();
        playbackDatabase.clear();
        libraryChangeEvent.setValue(true);
        playbacksChangeEvent.setValue(true);
        rankingChangeEvent.setValue(true);
        listeningTimeChangeEvent.setValue(true);
        extendedDataChangeEvent.setValue(true);
        resetArtistCounter();
        resetTrackCounter();
        resetPlaybackCounter();
        updateImportedArtists();
        updateImportedTracks();
        updateImportedPlaybacks();
    }

    //date and time formatter (2020-01-01 04:20)
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static DateTimeFormatter getFormatter() { return formatter; }
    //date and time formatter for extended spotify data (2020-01-01T04:20Z)
    private static final DateTimeFormatter formatterExtended = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssz");
    public static DateTimeFormatter getFormatterExtended() { return formatterExtended; }

    //counts added artists
    private static int artistCounter = 0;
    public static int getArtistCounter() { return artistCounter; }
    public static void increaseArtistCounter() { artistCounter++; }
    private static void resetArtistCounter() { artistCounter = 0; }
    //counts added tracks
    private static int trackCounter = 0;
    public static int getTrackCounter() { return trackCounter; }
    public static void increaseTrackCounter() { trackCounter++; }
    private static void resetTrackCounter() { trackCounter = 0; }
    //counts added playbacks
    private static int playbackCounter = 0;
    public static int getPlaybackCounter() { return playbackCounter; }
    public static void increasePlaybackCounter() { playbackCounter++; }
    private static void resetPlaybackCounter() { playbackCounter = 0; }

    //first and last listening dates for the listening time chart
    private static LocalDate firstPlaybackDate;
    public static LocalDate getFirstPlaybackDate() { return firstPlaybackDate; }
    public static void setFirstPlaybackDate(LocalDate date) { firstPlaybackDate = date; }
    private static LocalDate lastPlaybackDate;
    public static LocalDate getLastPlaybackDate() { return lastPlaybackDate; }
    public static void setLastPlaybackDate(LocalDate date) { lastPlaybackDate = date; }

    //events for listeners, true if the library / database changed
    public static BooleanProperty libraryChangeEvent = new SimpleBooleanProperty(false);
    public static BooleanProperty playbacksChangeEvent = new SimpleBooleanProperty(false);
    public static BooleanProperty rankingChangeEvent = new SimpleBooleanProperty(false);
    public static BooleanProperty userdataChangeEvent = new SimpleBooleanProperty(false);
    public static BooleanProperty listeningTimeChangeEvent = new SimpleBooleanProperty(false);
    public static BooleanProperty extendedDataChangeEvent = new SimpleBooleanProperty(false);

    //event for switching between default mode and the mode for extended export data
    private static final BooleanProperty extendedMode = new SimpleBooleanProperty(false);
    public static BooleanProperty getExtendedModeProperty() { return extendedMode; }
    public static boolean getExtendedMode() { return extendedMode.getValue(); }
    public static void swapExtendedMode() { extendedMode.set(!extendedMode.getValue()); }

    //indicates if an import is in progress (for the MainWindowController)
    public static BooleanProperty importStatus = new SimpleBooleanProperty(false);

    //contains the text to output on the text overview on the import pane
    private static final StringProperty textOverviewPuffer = new SimpleStringProperty();
    public static void updateTextOverview(String text) {
        if (textOverviewPuffer.getValue() == null) textOverviewPuffer.set(text);
        else textOverviewPuffer.set(textOverviewPuffer.getValue() + text);
    }
    public static void clearTextOverview() { textOverviewPuffer.set(""); }
    public static StringProperty getTextOverviewPuffer() { return textOverviewPuffer; }

    //data to display on the import pane regarding the count of imported data
    private static final IntegerProperty importedArtists = new SimpleIntegerProperty();
    public static void updateImportedArtists() { importedArtists.set(artistCounter); }
    public static IntegerProperty getImportedArtists() { return importedArtists; }
    private static final IntegerProperty importedTracks = new SimpleIntegerProperty();
    public static void updateImportedTracks() { importedTracks.set(trackCounter); }
    public static IntegerProperty getImportedTracks() { return importedTracks; }
    private static final IntegerProperty importedPlaybacks = new SimpleIntegerProperty();
    public static void updateImportedPlaybacks() { importedPlaybacks.set(playbackCounter); }
    public static IntegerProperty getImportedPlaybacks() { return importedPlaybacks; }

}
