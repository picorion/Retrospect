package io.github.picorion.model;

import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Object that contains the data of a track
 */
public class Track extends MusicData {

    private static final Logger LOGGER = LoggerFactory.getLogger(Track.class);

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
        LOGGER.warn("a track has no sub-tracks");
        return null;
    }

}
