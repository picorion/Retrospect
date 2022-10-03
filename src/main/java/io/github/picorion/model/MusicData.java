package io.github.picorion.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

    /**
     * default constructor
     *
     * @param artist   artist of the track
     * @param track    name of the track
     * @param dateTime date and time of listening
     */
    protected MusicData(String artist, String track, LocalDateTime dateTime) {
        artistNameProperty.set(artist);
        if (track == null || track.isEmpty()) titleProperty.set("0");
        else titleProperty.set(track);
        totalPlaybacksProperty.set(0);
        totalListeningTimeProperty.set(0);
        firstListened = dateTime;
        lastListened = dateTime;
        this.podcast = false;
    }

    /**
     * constructor for podcasts contained in extended data
     *
     * @param artist   artist of the track or podcast
     * @param track    name of the track or podcast
     * @param dateTime date and time of listening
     * @param podcast  true if it is a podcast
     */
    protected MusicData(String artist, String track, LocalDateTime dateTime, boolean podcast) {
        artistNameProperty.set(artist);
        if (track == null || track.isEmpty()) titleProperty.set("0");
        else titleProperty.set(track);
        totalPlaybacksProperty.set(0);
        totalListeningTimeProperty.set(0);
        firstListened = dateTime;
        lastListened = dateTime;
        this.podcast = podcast;
    }

    /**
     * Returns the name of the artist as {@link String}
     *
     * @return name of the artist as {@link String}
     */
    public String getArtistName() {
        return artistNameProperty.getValue();
    }

    /**
     * Returns the name of the artist as {@link StringProperty}
     *
     * @return name of the artist as {@link StringProperty}
     */
    public StringProperty getArtistNameProperty() {
        return artistNameProperty;
    }

    /**
     * Sets the title of a track (also used to store the number of tracks of artists in {@link Artist}
     *
     * @param title name of the track
     */
    void setTitle(String title) {
        this.titleProperty = new SimpleStringProperty(title);
    }

    /**
     * Returns the name of the track as {@link String}
     *
     * @return name of the track as {@link String}
     */
    public String getTitle() {
        return titleProperty.getValue();
    }

    /**
     * Returns the name of the track as {@link StringProperty}
     *
     * @return name of the track as {@link StringProperty}
     */
    public StringProperty getTitleProperty() {
        return titleProperty;
    }

    /**
     * Returns the total number of playbacks
     *
     * @return total number of playbacks
     */
    public int getTotalPlaybacks() {
        return totalPlaybacksProperty.getValue();
    }

    /**
     * Returns the number of playbacks as {@link IntegerProperty}
     *
     * @return number of playbacks as {@link IntegerProperty}
     */
    public IntegerProperty getTotalPlaybacksProperty() {
        return totalPlaybacksProperty;
    }

    /**
     * increases the total number of playbacks by one
     */
    public void increaseTotalPlaybacks() {
        totalPlaybacksProperty.set(totalPlaybacksProperty.getValue() + 1);
    }

    /**
     * Returns the total listening time
     *
     * @return total listening time
     */
    public long getTotalListeningTime() {
        return totalListeningTimeProperty.getValue();
    }

    /**
     * Returns the total listening time as {@link StringProperty}
     *
     * @return total listening time as {@link StringProperty}
     */
    public StringProperty getTotalListeningTimeProperty() {
        String time = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(getTotalListeningTime()),
                TimeUnit.MILLISECONDS.toMinutes(getTotalListeningTime()) % TimeUnit.HOURS.toMinutes(1));
        return new SimpleStringProperty(time);
    }

    /**
     * Increases the total listening time with the given value
     *
     * @param time time to add on
     */
    public void increaseTotalListeningTime(long time) {
        totalListeningTimeProperty.set(getTotalListeningTime() + time);
    }

    /**
     * Returns the data and time a track or artist got the first time listened
     *
     * @return data and time a track or artist got the first time listened
     */
    public LocalDateTime getFirstListened() {
        return firstListened;
    }

    /**
     * Updates the time and date a track or artist got the first time listened
     *
     * @param dateTime new time and date
     */
    public void updateFirstListened(LocalDateTime dateTime) {
        firstListened = dateTime;
    }

    /**
     * Returns the data and time a track or artist got listened the last time
     *
     * @return data and time a track or artist got the last time listened
     */
    public LocalDateTime getLastListened() {
        return lastListened;
    }

    /**
     * Updates the time and date a track or artist got the last time listened
     *
     * @param dateTime new time and date
     */
    public void updateLastListened(LocalDateTime dateTime) {
        lastListened = dateTime;
    }

    /**
     * Adds a track to the list of an artist
     *
     * @param track track to add
     */
    public void addTrack(Track track) {
        tracks.add(track);
    }

    /**
     * Returns the list of tracks of an artist
     *
     * @return list of tracks
     */
    public ObservableList<Track> getTracks() {
        return tracks;
    }

    /**
     * Check if the track is a podcast or a song
     *
     * @return true if the track is a podcast and not a song
     */
    public boolean isPodcast() {
        return podcast;
    }

}
