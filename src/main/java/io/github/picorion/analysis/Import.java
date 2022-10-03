package io.github.picorion.analysis;

import javafx.scene.image.Image;
import io.github.picorion.model.Artist;
import io.github.picorion.model.Data;
import io.github.picorion.model.Playback;
import io.github.picorion.model.PlaybackDatabase;
import io.github.picorion.model.Track;
import io.github.picorion.model.Userdata;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Methods for the data import from spotify export files
 */
public class Import {

    /**
     * Private constructor to prevent instantiation
     */
    private Import() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Import.class);

    /**
     * Checks if a directory at least contains one type of spotify export data
     *
     * @param directory containing files
     * @return true if the directory contains relevant files
     */
    public static boolean containsSpotifyExportData(File directory) {
        return Objects.requireNonNull(directory.list((dir, name) -> new File(dir, name).isFile() && (name.contains("StreamingHistory") || name.contains("endsong")))).length > 0;
    }

    /**
     * Gets data from a .json file with array structure
     *
     * @param file .json file
     * @return {@link JSONArray} from the file
     */
    public static JSONArray readJsonArray(File file) {
        JSONParser parser = new JSONParser();
        JSONArray data = null;
        try {
            Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            data = (JSONArray) parser.parse(reader);
            reader.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets data from a .json file with object structure
     *
     * @param file .json file
     * @return {@link JSONObject} from the file
     */
    public static JSONObject readJsonObject(File file) {
        JSONParser parser = new JSONParser();
        JSONObject data = null;
        try {
            Reader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8));
            data = (JSONObject) parser.parse(reader);
            reader.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Imports streaming history data from a given spotify export folder
     *
     * @param directory containing one or more <em>StreamingHistory*.json</em> or <em>endsong_*.json</em> file(s)
     */
    public static void importData(File directory, int requiredPlaybackTime) {

        //adjusts the given required playback time
        if (requiredPlaybackTime <= 0) requiredPlaybackTime = 0;
        else requiredPlaybackTime *= 1000;

        //points out limitations of the extended mode when enabled
        if (Data.getExtendedMode()) {
            Data.updateTextOverview(" ℹ\textended mode enabled (default streaming data will be ignored)\n");
        }

        //checks if the directory contains streaming history files from a normal export and imports them
        if ((Objects.requireNonNull(directory.list((dir, name) -> name.contains("StreamingHistory")))).length > 0 && !Data.getExtendedMode()) {
            importDefaultStreamingHistory(directory, requiredPlaybackTime);
        }

        //checks if the directory contains streaming history files from an extended export and imports them
        if ((Objects.requireNonNull(directory.list((dir, name) -> name.contains("endsong_")))).length > 0) {
            importExtendedStreamingHistory(directory, requiredPlaybackTime);
        }

        importUserData(directory);

        //indicates a library, database and userdata change for listening methods
        LOGGER.debug("triggering change events");
        Data.libraryChangeEvent.setValue(true);
        Data.playbacksChangeEvent.setValue(true);
        Data.rankingChangeEvent.setValue(true);
        Data.userdataChangeEvent.setValue(true);
        Data.listeningTimeChangeEvent.setValue(true);
        Data.extendedDataChangeEvent.setValue(true);
    }

    /**
     * Imports default streaming history files
     *
     * @param directory            containing one or more StreamingHistory*.json file(s)
     * @param requiredPlaybackTime .
     */
    private static void importDefaultStreamingHistory(File directory, int requiredPlaybackTime) {
        //creates the file paths for the streaming history files
        String[] historyFilenames = directory.list((dir, name) -> name.contains("StreamingHistory"));
        assert historyFilenames != null;
        File[] historyFiles = new File[historyFilenames.length];
        for (int i = 0; i < historyFilenames.length; i++) {
            historyFiles[i] = new File(directory, historyFilenames[i]);
        }
        //runs through the files and creates arrays containing the playbacks
        for (File i : historyFiles) {
            Data.updateTextOverview("→\trunning through " + i.toString().substring(directory.toString().length() + 1) + "\n");
            JSONArray playbacks = readJsonArray(i);
            int counter = 0;
            int duplicateCounter = 0;
            int unmetPlaybackTimeCounter = 0;
            //runs through the individual playbacks and adds them to the library and database
            for (Object j : playbacks) {
                JSONObject playback = (JSONObject) j;
                //translates the data and creates or fetches the objects
                LocalDateTime dateTime = LocalDateTime.parse(playback.get("endTime").toString(), Data.getFormatter());
                Artist currentArtist = Analysis.searchArtist((String) playback.get("artistName"));
                if (currentArtist == null) {
                    currentArtist = new Artist((String) playback.get("artistName"), dateTime);
                    Data.library.add(currentArtist);
                    Data.increaseArtistCounter();
                }
                Track currentTrack = Analysis.searchTrack(currentArtist, (String) playback.get("trackName"));
                if (currentTrack == null) {
                    //insert "(String) playback.get("artistName")" instead of "" to display the artist names in the library
                    //removed because of the indent bug caused by searching / filtering
                    currentTrack = new Track("", (String) playback.get("trackName"), dateTime);
                    currentArtist.addTrack(currentTrack);
                    currentArtist.increaseTracks();
                    Data.increaseTrackCounter();
                }
                YearMonth yearMonth = YearMonth.parse(playback.get("endTime").toString().substring(0, 7));
                long listeningTime = (long) playback.get("msPlayed");
                PlaybackDatabase currentDatabase = Analysis.searchDatabase(yearMonth);
                if (currentDatabase == null) {
                    currentDatabase = new PlaybackDatabase(yearMonth);
                    Data.playbackDatabase.add(currentDatabase);
                }
                //checks for too short playback time and duplicates and adds if everything is ok to the playbacks
                if ((long) playback.get("msPlayed") < requiredPlaybackTime) {
                    unmetPlaybackTimeCounter++;
                } else if (Analysis.duplicatePlaybackCheck(currentDatabase, dateTime, currentArtist, currentTrack, listeningTime)) {
                    duplicateCounter++;
                } else {
                    counter++;
                    currentArtist.increaseTotalPlaybacks();
                    currentArtist.increaseTotalListeningTime(listeningTime);
                    currentTrack.increaseTotalPlaybacks();
                    currentTrack.increaseTotalListeningTime(listeningTime);
                    currentDatabase.getPlaybacks().add(new Playback(currentArtist, currentTrack, dateTime, listeningTime));
                    Data.increasePlaybackCounter();
                }
                //updates the first and last playback dates
                LocalDate listeningDate = LocalDate.parse(playback.get("endTime").toString().substring(0, 10));
                if (Data.getFirstPlaybackDate() == null) Data.setFirstPlaybackDate(listeningDate);
                else if (Data.getFirstPlaybackDate().isAfter(listeningDate)) Data.setFirstPlaybackDate(listeningDate);
                if (Data.getLastPlaybackDate() == null) Data.setLastPlaybackDate(listeningDate);
                else if (Data.getLastPlaybackDate().isBefore(listeningDate)) Data.setLastPlaybackDate(listeningDate);
            }
            Data.updateImportedArtists();
            Data.updateImportedTracks();
            Data.updateImportedPlaybacks();
            Data.updateTextOverview("✓\timported " + counter + " / " + (counter + unmetPlaybackTimeCounter + duplicateCounter) + " playbacks\n");
        }
    }

    /**
     * Imports extended streaming history files
     *
     * @param directory            containing one or more endsong_*.json file(s)
     * @param requiredPlaybackTime .
     */
    private static void importExtendedStreamingHistory(File directory, int requiredPlaybackTime) {
        Data.updateTextOverview(" ℹ\tfound extended spotify export data\n");
        //creates the file paths for the streaming history files
        String[] historyFilenames = directory.list((dir, name) -> name.contains("endsong_"));
        assert historyFilenames != null;
        File[] historyFiles = new File[historyFilenames.length];
        for (int i = 0; i < historyFilenames.length; i++) {
            historyFiles[i] = new File(directory, historyFilenames[i]);
        }
        //runs through the files and creates arrays containing the playbacks
        for (File i : historyFiles) {
            Data.updateTextOverview("→\trunning through " + i.toString().substring(directory.toString().length() + 1) + "\n");
            JSONArray playbacks = readJsonArray(i);
            int counter = 0;
            int duplicateCounter = 0;
            int unmetPlaybackTimeCounter = 0;
            //runs through the individual playbacks and adds them to the library and database
            for (Object j : playbacks) {
                JSONObject playback = (JSONObject) j;
                //checks if playback is track or podcast, there is another unknown type which does not contain necessary data
                if (playback.get("episode_show_name") != null || playback.get("master_metadata_album_artist_name") != null) {
                    //checks if playback is track or podcast
                    boolean podcast = playback.get("episode_name") != null;
                    //translates the data and creates or fetches the objects
                    LocalDateTime dateTime = LocalDateTime.parse(playback.get("ts").toString(), Data.getFormatterExtended());
                    //removes seconds because of comparability reasons
                    dateTime = dateTime.truncatedTo(ChronoUnit.MINUTES);
                    Artist currentArtist;
                    if (podcast) currentArtist = Analysis.searchArtist((String) playback.get("episode_show_name"));
                    else
                        currentArtist = Analysis.searchArtist((String) playback.get("master_metadata_album_artist_name"));
                    if (currentArtist == null) {
                        if (podcast)
                            currentArtist = new Artist((String) playback.get("episode_show_name"), dateTime, true);
                        else
                            currentArtist = new Artist((String) playback.get("master_metadata_album_artist_name"), dateTime);
                        Data.library.add(currentArtist);
                        Data.increaseArtistCounter();
                    }
                    Track currentTrack;
                    if (podcast)
                        currentTrack = Analysis.searchTrack(currentArtist, (String) playback.get("episode_name"));
                    else
                        currentTrack = Analysis.searchTrack(currentArtist, (String) playback.get("master_metadata_track_name"));
                    if (currentTrack == null) {
                        //insert "(String) playback.get("artistName")" instead of "" to display the artist names in the library
                        //removed because of the indent bug caused by searching / filtering
                        if (podcast)
                            currentTrack = new Track("", (String) playback.get("episode_name"), dateTime, true);
                        else
                            currentTrack = new Track("", (String) playback.get("master_metadata_track_name"), dateTime);
                        currentArtist.addTrack(currentTrack);
                        currentArtist.increaseTracks();
                        Data.increaseTrackCounter();
                    }
                    YearMonth yearMonth = YearMonth.parse(playback.get("ts").toString().substring(0, 7));
                    long listeningTime = (long) playback.get("ms_played");
                    PlaybackDatabase currentDatabase = Analysis.searchDatabase(yearMonth);
                    if (currentDatabase == null) {
                        currentDatabase = new PlaybackDatabase(yearMonth);
                        Data.playbackDatabase.add(currentDatabase);
                    }
                    //checks for too short playback time and duplicates and adds if everything is ok to the playbacks
                    if ((long) playback.get("ms_played") < requiredPlaybackTime) {
                        unmetPlaybackTimeCounter++;
                    } else if (Analysis.duplicatePlaybackCheck(currentDatabase, dateTime, currentArtist, currentTrack, listeningTime)) {
                        duplicateCounter++;
                    } else {
                        counter++;
                        currentArtist.increaseTotalPlaybacks();
                        currentArtist.increaseTotalListeningTime(listeningTime);
                        currentTrack.increaseTotalPlaybacks();
                        currentTrack.increaseTotalListeningTime(listeningTime);
                        currentDatabase.getPlaybacks().add(new Playback(currentArtist, currentTrack, dateTime, listeningTime, playback.get("platform").toString()));
                        Data.increasePlaybackCounter();
                    }
                    //updates the first and last playback dates
                    LocalDate listeningDate = LocalDate.parse(playback.get("ts").toString().substring(0, 10));
                    if (Data.getFirstPlaybackDate() == null) Data.setFirstPlaybackDate(listeningDate);
                    else if (Data.getFirstPlaybackDate().isAfter(listeningDate))
                        Data.setFirstPlaybackDate(listeningDate);
                    if (Data.getLastPlaybackDate() == null) Data.setLastPlaybackDate(listeningDate);
                    else if (Data.getLastPlaybackDate().isBefore(listeningDate))
                        Data.setLastPlaybackDate(listeningDate);
                }
            }
            Data.updateImportedArtists();
            Data.updateImportedTracks();
            Data.updateImportedPlaybacks();
            Data.updateTextOverview("✓\timported " + counter + " / " + (counter + unmetPlaybackTimeCounter + duplicateCounter) + " playbacks\n");
        }
    }

    /**
     * Imports userdata from known files if they exist
     *
     * @param directory of the spotify export data
     */
    public static void importUserData(File directory) {
        //creates the files
        File familyPlan = new File(directory + "\\FamilyPlan.json");
        File identity = new File(directory + "\\Identity.json");
        File userdata = new File(directory + "\\Userdata.json");
        if (familyPlan.exists()) {
            JSONObject data = readJsonObject(familyPlan);
            JSONObject address = (JSONObject) data.get("address");
            String tmp;
            tmp = (String) address.get("street");
            if (tmp != null && !tmp.equals("")) Userdata.setStreet(tmp);
            tmp = (String) address.get("city");
            if (tmp != null && !tmp.equals("")) Userdata.setCity(tmp);
            tmp = (String) address.get("postal_code");
            if (tmp != null && !tmp.equals("")) Userdata.setPostalCode(tmp);
            tmp = (String) address.get("state");
            if (tmp != null && !tmp.equals("")) Userdata.setState(tmp);
            tmp = (String) address.get("country");
            if (tmp != null && !tmp.equals("")) Userdata.setCountry(tmp);
            tmp = (String) address.get("real_name");
            if (tmp != null && !tmp.equals("")) Userdata.setRealName(tmp);
            Data.updateTextOverview("✓\timported FamilyPlan.json\n");
        } else Data.updateTextOverview("❌\tFamilyPlan.json not found\n");
        if (identity.exists()) {
            JSONObject data = readJsonObject(identity);
            String tmp = (String) data.get("largeImageUrl");
            if (tmp != null && !tmp.equals("")) Userdata.setImage(new Image(tmp, true));
            Data.updateTextOverview("✓\timported Identity.json\n");
        } else Data.updateTextOverview("❌\tIdentity.json not found\n");
        if (userdata.exists()) {
            JSONObject data = readJsonObject(userdata);
            String tmp = (String) data.get("username");
            if (tmp != null && !tmp.equals("")) Userdata.setUsername(tmp);
            tmp = (String) data.get("email");
            if (tmp != null && !tmp.equals("")) Userdata.setEmail(tmp);
            tmp = (String) data.get("country");
            if (tmp != null && !tmp.equals("")) Userdata.setCountry(tmp);
            tmp = (String) data.get("facebookUid");
            if (tmp != null && !tmp.equals("")) Userdata.setFacebookID(tmp);
            tmp = (String) data.get("birthdate");
            if (tmp != null && !tmp.equals("")) Userdata.setBirthdate(tmp);
            tmp = (String) data.get("gender");
            if (tmp != null && !tmp.equals("")) Userdata.setGender(tmp);
            tmp = (String) data.get("postalCode");
            if (tmp != null && !tmp.equals("")) Userdata.setPostalCode(tmp);
            tmp = (String) data.get("mobileNumber");
            if (tmp != null && !tmp.equals("")) Userdata.setMobileNumber(tmp);
            tmp = (String) data.get("creationTime");
            if (tmp != null && !tmp.equals("")) Userdata.setCreationTime(tmp);
            Data.updateTextOverview("✓\timported Userdata.json\n");
        } else Data.updateTextOverview("❌\tUserdata.json not found\n");
    }

}
