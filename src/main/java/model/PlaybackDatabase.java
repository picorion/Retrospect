package model;

import java.time.YearMonth;
import java.util.ArrayList;

/**
 * Object which contains the imported playbacks grouped by a year and month
 */
public class PlaybackDatabase {

    private final YearMonth yearMonth;
    public ArrayList<Playback> playbacks = new ArrayList<>();

    public PlaybackDatabase(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
    }

    public YearMonth getYearMonth() {
        return yearMonth;
    }

}
