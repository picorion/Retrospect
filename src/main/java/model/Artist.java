package model;

import java.time.LocalDateTime;

/**
 * Object that contains the data of an artist
 */
public class Artist extends MusicData {

    //default constructor
    public Artist(String artist, LocalDateTime dateTime) {
        super(artist, null, dateTime);
    }

    //constructor for extended data declared as podcast
    public Artist(String artist, LocalDateTime dateTime, boolean podcast) { super(artist, null, dateTime, podcast); }

    public void increaseTracks() {
        setTitle(String.valueOf(Integer.parseInt(super.getTitle()) + 1));
    }

}
