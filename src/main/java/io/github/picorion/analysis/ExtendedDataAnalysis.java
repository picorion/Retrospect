package io.github.picorion.analysis;

import io.github.picorion.model.Data;
import io.github.picorion.model.PlatformData;
import io.github.picorion.model.Playback;
import io.github.picorion.model.PlaybackDatabase;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Methods for io.github.picorion.analysis limited to the extended spotify export data
 */
public class ExtendedDataAnalysis {

    /**
     * Private constructor to prevent instantiation
     */
    private ExtendedDataAnalysis() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns the distribution of podcasts in the playbacks of a given period
     *
     * @param from  io.github.picorion.start date
     * @param until end date
     * @return Integer which represents the percentage of the share
     */
    public static long[] getPodcastDistribution(LocalDate from, LocalDate until) {
        //0 -> Songs, 1 -> Podcasts
        long[] time = {0, 0};
        for (PlaybackDatabase database : Data.playbackDatabase) {
            if ((database.getYearMonth().isAfter(YearMonth.from(from)) && database.getYearMonth().isBefore(YearMonth.from(until)))
                    || database.getYearMonth().toString().equals(YearMonth.from(from).toString())
                    || database.getYearMonth().toString().equals(YearMonth.from(until).toString())) {
                for (Playback playback : database.getPlaybacks()) {
                    if (from.isBefore(LocalDate.from(playback.getDateTime())) && until.isAfter(LocalDate.from(playback.getDateTime()))
                            || from.isEqual(LocalDate.from(playback.getDateTime())) || until.isEqual(LocalDate.from(playback.getDateTime()))) {
                        if (!playback.getTrack().isPodcast()) time[0] += (playback.getListeningTime() / 1000);
                        else time[1] += (playback.getListeningTime() / 1000);
                    }
                }
            }
        }
        return time;
    }

    /**
     * Creates a list with all platforms listened from and the number listened from
     *
     * @param from  io.github.picorion.start date
     * @param until end date
     * @return list of all with all platforms listened from
     */
    public static List<PlatformData> getPlatformDistribution(LocalDate from, LocalDate until) {
        ArrayList<PlatformData> platformList = new ArrayList<>();
        for (PlaybackDatabase database : Data.playbackDatabase) {
            if ((database.getYearMonth().isAfter(YearMonth.from(from)) && database.getYearMonth().isBefore(YearMonth.from(until)))
                    || database.getYearMonth().toString().equals(YearMonth.from(from).toString())
                    || database.getYearMonth().toString().equals(YearMonth.from(until).toString())) {
                for (Playback playback : database.getPlaybacks()) {
                    if (from.isBefore(LocalDate.from(playback.getDateTime())) && until.isAfter(LocalDate.from(playback.getDateTime()))
                            || from.isEqual(LocalDate.from(playback.getDateTime())) || until.isEqual(LocalDate.from(playback.getDateTime()))) {
                        String currentPlatform = playback.getPlatform();
                        boolean considered = false;
                        if (!platformList.isEmpty()) {
                            for (PlatformData platformData : platformList) {
                                if (platformData.getDescription().equals(currentPlatform)) {
                                    platformData.addTime(playback.getListeningTime() / 1000);
                                    considered = true;
                                }
                            }
                        }
                        if (!considered)
                            platformList.add(new PlatformData(currentPlatform, playback.getListeningTime() / 1000));
                    }
                }
            }
        }
        //limits to five platforms and adds the "platform" others if there are more than five platforms
        if (platformList.size() > 5) {
            platformList.sort(Comparator.comparingLong(PlatformData::getTime));
            PlatformData others = new PlatformData("Others", 0);
            ArrayList<PlatformData> othersList = new ArrayList<>(platformList.subList(0, platformList.size() - 4));
            for (PlatformData platformData : othersList) {
                others.addTime(platformData.getTime());
            }
            platformList = new ArrayList<>(platformList.subList(platformList.size() - 4, platformList.size()));
            platformList.add(others);
        }
        return platformList;
    }

}
