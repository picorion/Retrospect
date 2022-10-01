package analysis;

import model.Artist;
import model.Data;
import model.Playback;
import model.PlaybackDatabase;
import model.Track;

import java.time.LocalDateTime;
import java.time.YearMonth;

/**
 * Basis analysis methods
 */
public class Analysis {

    /**
     * Private constructor to prevent instantiation
     */
    private Analysis() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Searches for an artist in the library
     *
     * @param artistName Name of the searched artist
     * @return Artist object if found, otherwise null
     */
    public static Artist searchArtist(String artistName) {
        for (Artist artist : Data.library) {
            if (artist.getArtistName().equalsIgnoreCase(artistName)) return artist;
        }
        return null;
    }

    /**
     * Searches for a track from an artist in the library
     *
     * @param artist    Artist object of the interpreter
     * @param trackName Name of the searched track
     * @return Track object if found, otherwise null
     */
    public static Track searchTrack(Artist artist, String trackName) {
        for (Track track : artist.getTracks()) {
            if (track.getTitle().equalsIgnoreCase(trackName)) return track;
        }
        return null;
    }

    /**
     * Searches for a database by year and month in the databases
     *
     * @param yearMonth Year and month to search for
     * @return Database of the right year and month if it exists, otherwise null
     */
    public static PlaybackDatabase searchDatabase(YearMonth yearMonth) {
        for (PlaybackDatabase database : Data.playbackDatabase) {
            if (database.getYearMonth().equals(yearMonth)) return database;
        }
        return null;
    }

    /**
     * Checks the database for identical entries to prevent duplicates
     *
     * @return True if there is already an identical entry
     */
    public static boolean duplicatePlaybackCheck(PlaybackDatabase database, LocalDateTime dateTime, Artist artist, Track track, long listeningTime) {
        for (Playback p : database.getPlaybacks()) {
            if (p.getDateTime().equals(dateTime) && p.getArtist() == artist && p.getTrack() == track && p.getListeningTime() == listeningTime)
                return true;
        }
        return false;
    }

    /*
    // Creates a new file and writes given string to it
    //date and time formatter for export files (2020-01-01)
    public final static DateTimeFormatter formatterExport = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static void print(File directory, String output) {
        try {
            File file = new File(directory + "\\SpotifyAnalysis[" + LocalDateTime.now().format(formatterExport) + "].txt");
            if (file.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(output.getBytes());
                fos.flush();
                fos.close();
                System.out.println("--> file exported to " + file);
            } else {
                System.out.println("--> an error occurred while creating the file (try to rename existing export)");
            }
        } catch (Exception e) {
            System.err.println("--> " + e);
        }
    }
     */

}
