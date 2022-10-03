package io.github.picorion.model;

import java.util.concurrent.TimeUnit;

/**
 * Ranking data of an artist or track with data of a specified period
 * <p>
 * <strong>only for the ranking io.github.picorion.analysis</strong>
 */
public class RankingData {

    private final MusicData key;
    private final String artistName;
    private long listeningTime;
    private int playbacks;

    public RankingData(MusicData key, String artistName, long listeningTime, int playbacks) {
        this.key = key;
        this.artistName = artistName;
        this.listeningTime = listeningTime;
        this.playbacks = playbacks;
    }

    public MusicData getKey() {
        return key;
    }

    public String getArtistName() { return artistName; }

    public String getListeningTimeFormatted() {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(listeningTime),
                TimeUnit.MILLISECONDS.toMinutes(listeningTime) % TimeUnit.HOURS.toMinutes(1));
    }
    public long getListeningTime() {
        return listeningTime;
    }
    public void addListeningTime(long time) {
        listeningTime += time;
    }

    public int getPlaybacks() { return playbacks; }
    public void increasePlaybacks() {
        playbacks++;
    }

}
