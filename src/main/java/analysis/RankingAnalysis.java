package analysis;

import model.Data;
import model.MusicData;
import model.Playback;
import model.PlaybackDatabase;
import model.RankingData;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Methods for ranking creation
 */
public class RankingAnalysis {

    /**
     * Private constructor to prevent instantiation
     */
    private RankingAnalysis() {
        throw new IllegalStateException("Utility class");
    }

    //ranking data which contains artists / tracks with their listening time and playbacks of a time period
    private static ArrayList<RankingData> artistRankingData = new ArrayList<>();
    private static ArrayList<RankingData> trackRankingData = new ArrayList<>();

    /**
     * Creates the ranking data for the given time period
     * the ranking data contains a key (music data -> artist or track), the listening time and the number of playbacks
     *
     * @param from  start date
     * @param until end date
     */
    public static void createRankingData(LocalDate from, LocalDate until) {
        artistRankingData = new ArrayList<>();
        trackRankingData = new ArrayList<>();
        for (PlaybackDatabase database : Data.playbackDatabase) {
            if ((database.getYearMonth().isAfter(YearMonth.from(from)) && database.getYearMonth().isBefore(YearMonth.from(until)))
                    || database.getYearMonth().toString().equals(YearMonth.from(from).toString())
                    || database.getYearMonth().toString().equals(YearMonth.from(until).toString())) {
                for (Playback playback : database.getPlaybacks()) {
                    if (from.isBefore(LocalDate.from(playback.getDateTime())) && until.isAfter(LocalDate.from(playback.getDateTime())) || from.isEqual(LocalDate.from(playback.getDateTime())) || until.isEqual(LocalDate.from(playback.getDateTime()))) {
                        RankingData currentArtist = searchRankingData(artistRankingData, playback.getArtist());
                        if (currentArtist == null) {
                            artistRankingData.add(new RankingData(playback.getArtist(), "! artist has no artist", playback.getListeningTime(), 1));
                        } else {
                            currentArtist.addListeningTime(playback.getListeningTime());
                            currentArtist.increasePlaybacks();
                        }
                        RankingData currentTrack = searchRankingData(trackRankingData, playback.getTrack());
                        if (currentTrack == null) {
                            trackRankingData.add(new RankingData(playback.getTrack(), playback.getArtist().getArtistName(), playback.getListeningTime(), 1));
                        } else {
                            currentTrack.addListeningTime(playback.getListeningTime());
                            currentTrack.increasePlaybacks();
                        }
                    }
                }
            }
        }
    }

    /**
     * Searches for a rankingData element in an arrayList
     *
     * @param rankingData arrayList containing the ranking data
     * @param musicData   an musicData element (artist or track) to look for
     * @return rankingData element if it exists, otherwise null
     */
    private static RankingData searchRankingData(ArrayList<RankingData> rankingData, MusicData musicData) {
        for (RankingData data : rankingData) {
            if (data.getKey().equals(musicData)) return data;
        }
        return null;
    }

    /**
     * Calls getListeningTimeRanking for the top ten artists by listening time
     */
    public static RankingData[] createArtistListeningTimeRanking() {
        return getListeningTimeRanking(artistRankingData);
    }

    /**
     * Calls getListeningTimeRanking for the top ten tracks by listening time
     */
    public static RankingData[] createTrackListeningTimeRanking() {
        return getListeningTimeRanking(trackRankingData);
    }

    /**
     * Gets the top ten of the rankingData by listeningTime
     *
     * @param rankingData to use
     * @return top ten musicData elements (artists or tracks)
     */
    private static RankingData[] getListeningTimeRanking(ArrayList<RankingData> rankingData) {
        RankingData[] topTen = new RankingData[10];
        rankingData.sort(Comparator.comparingLong(RankingData::getListeningTime));
        Collections.reverse(rankingData);
        int i = 0;
        while (i < rankingData.size() && i < 10) {
            topTen[i] = rankingData.get(i);
            i++;
        }
        return topTen;
    }

    /**
     * Calls {@link RankingAnalysis#getPlaybackRanking(ArrayList)} to generate ranking data for artists
     *
     * @return {@link RankingData} containing the top ten artists by the number of playbacks
     */
    public static RankingData[] createArtistPlaybackRanking() {
        return getPlaybackRanking(artistRankingData);
    }

    /**
     * Calls {@link RankingAnalysis#getPlaybackRanking(ArrayList)} to generate ranking data for tracks
     *
     * @return {@link RankingData} containing the top ten tracks by the number of playbacks
     */
    public static RankingData[] createTrackPlaybackRanking() {
        return getPlaybackRanking(trackRankingData);
    }

    /**
     * Gets the top ten of the rankingData by the number ob playbacks
     *
     * @param rankingData to use
     * @return top ten musicData elements (artists or tracks)
     */
    private static RankingData[] getPlaybackRanking(ArrayList<RankingData> rankingData) {
        RankingData[] topTen = new RankingData[10];
        rankingData.sort(Comparator.comparingInt(RankingData::getPlaybacks));
        Collections.reverse(rankingData);
        int i = 0;
        while (i < rankingData.size() && i < 10) {
            topTen[i] = rankingData.get(i);
            i++;
        }
        return topTen;
    }

}
