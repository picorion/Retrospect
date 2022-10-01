package model;

import java.time.LocalDateTime;

/**
 * Object that contains the data of an artist
 */
public class Artist extends MusicData {

    /**
     * default constructor
     *
     * @param artist   artist name
     * @param dateTime date and time of listening
     */
    public Artist(String artist, LocalDateTime dateTime) {
        super(artist, null, dateTime);
    }

    /**
     * constructor for podcasts contained in extended data
     *
     * @param artist   artist of the track or podcast
     * @param dateTime date and time of listening
     * @param podcast  true if it is a podcast
     */
    public Artist(String artist, LocalDateTime dateTime, boolean podcast) {
        super(artist, null, dateTime, podcast);
    }

    /**
     * Increases the number of tracks which is stored in {@link MusicData#getTitle()}
     */
    public void increaseTracks() {
        setTitle(String.valueOf(Integer.parseInt(super.getTitle()) + 1));
    }

}
