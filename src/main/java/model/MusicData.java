package model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Parent class of Artist and Track, introduces variables and methods
 */
public abstract class MusicData {

    private final StringProperty artistNameProperty = new SimpleStringProperty();
    private StringProperty titleProperty = new SimpleStringProperty();
    private final IntegerProperty totalPlaybacksProperty = new SimpleIntegerProperty();
    private final LongProperty totalListeningTimeProperty = new SimpleLongProperty();    //in milliseconds
    private LocalDateTime firstListened;
    private LocalDateTime lastListened;
    private final ObservableList<Track> tracks = FXCollections.observableArrayList();
    private final boolean podcast;  //only provided by extended export data, default is false

    //default constructor
    public MusicData(String artist, String track, LocalDateTime dateTime) {
        artistNameProperty.set(artist);
        if (track == null || track.isEmpty()) titleProperty.set("0");
        else titleProperty.set(track);
        totalPlaybacksProperty.set(0);
        totalListeningTimeProperty.set(0);
        firstListened = dateTime;
        lastListened = dateTime;
        this.podcast = false;
    }

    //constructor for extended data declared as podcast
    public MusicData(String artist, String track, LocalDateTime dateTime, boolean podcast) {
        artistNameProperty.set(artist);
        if (track == null || track.isEmpty()) titleProperty.set("0");
        else titleProperty.set(track);
        totalPlaybacksProperty.set(0);
        totalListeningTimeProperty.set(0);
        firstListened = dateTime;
        lastListened = dateTime;
        this.podcast = podcast;
    }

    public String getArtistName() { return artistNameProperty.getValue(); }
    public StringProperty getArtistNameProperty() { return artistNameProperty; }

    void setTitle(String title) { this.titleProperty = new SimpleStringProperty(title); }
    public String getTitle() { return titleProperty.getValue(); }
    public StringProperty getTitleProperty() { return titleProperty; }

    public int getTotalPlaybacks() { return totalPlaybacksProperty.getValue(); }
    public IntegerProperty getTotalPlaybacksProperty() { return totalPlaybacksProperty; }
    public void increaseTotalPlaybacks() { totalPlaybacksProperty.set(totalPlaybacksProperty.getValue() + 1); }

    public long getTotalListeningTime() { return totalListeningTimeProperty.getValue(); }
    public StringProperty getTotalListeningTimeProperty() {
        String time = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(getTotalListeningTime()),
                TimeUnit.MILLISECONDS.toMinutes(getTotalListeningTime()) % TimeUnit.HOURS.toMinutes(1));
        return new SimpleStringProperty(time);
    }
    public void increaseTotalListeningTime(long time) { totalListeningTimeProperty.set(getTotalListeningTime() + time); }

    public LocalDateTime getFirstListened() { return firstListened; }
    public void updateFirstListened(LocalDateTime dateTime) { firstListened = dateTime; }

    public LocalDateTime getLastListened() { return lastListened; }
    public void updateLastListened(LocalDateTime dateTime) { lastListened = dateTime; }

    public void addTrack(Track track) { tracks.add(track); }

    public ObservableList<Track> getTracks() { return tracks; }

    public boolean isPodcast() { return podcast; }

}
