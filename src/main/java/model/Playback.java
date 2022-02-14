package model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

/**
 * Object that contains the data of the individual playbacks
 */
public class Playback {

    private final Artist artist;
    private final Track track;
    private final LocalDateTime dateTime;
    private final LongProperty listeningTimeProperty = new SimpleLongProperty();  //in milliseconds
    private final String platform;  //only provided by extended export data, default is null

    public Playback(Artist artist, Track track, LocalDateTime dateTime, long listeningTime) {
        this.artist = artist;
        this.track = track;
        this.dateTime = dateTime;
        this.listeningTimeProperty.set(listeningTime);
        this.platform = null;
    }

    public Playback(Artist artist, Track track, LocalDateTime dateTime, long listeningTime, String platform) {
        this.artist = artist;
        this.track = track;
        this.dateTime = dateTime;
        this.listeningTimeProperty.set(listeningTime);
        this.platform = platform;
    }

    public Artist getArtist() { return artist; }

    public Track getTrack() { return track; }

    public LocalDateTime getDateTime() { return dateTime; }
    public StringProperty getDateTimeProperty() {
        String text = dateTime.format(Data.getFormatter());
        return new SimpleStringProperty(text);
    }

    public long getListeningTime() { return listeningTimeProperty.getValue(); }
    public StringProperty getListeningTimeProperty() {
        String time = "";
        if (getListeningTime() / 1000 / 60 < 10) time += 0;
        time += (getListeningTime() / 1000 / 60) + ":" + (getListeningTime() / 1000 % 60);
        if(getListeningTime() / 1000 % 60 < 10) time += "0";
        return new SimpleStringProperty(time);
    }

    public String getPlatform() { return platform; }

}
