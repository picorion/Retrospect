package model;

import javafx.collections.ObservableList;

import java.time.LocalDateTime;

/**
 * Object that contains the data of a track
 */
public class Track extends MusicData {

    //default constructor
    public Track(String artist, String track, LocalDateTime dateTime) {
        super(artist, track, dateTime);
    }

    //constructor for extended data declared as podcast
    public Track(String artist, String track, LocalDateTime dateTime, boolean podcast) {
        super(artist, track, dateTime, podcast);
    }

    @Override
    public ObservableList<Track> getTracks() {
        System.out.println("!\ta track has no sub-tracks");
        return null;
    }

}
