package io.github.picorion.analysis;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import io.github.picorion.model.Data;
import io.github.picorion.model.Playback;
import io.github.picorion.model.PlaybackDatabase;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Methods for the io.github.picorion.analysis of listening times
 */
public class ListeningTimeAnalysis {

    /**
     * Private constructor to prevent instantiation
     */
    private ListeningTimeAnalysis() {
        throw new IllegalStateException("Utility class");
    }

    /*
     * listening time values for a specified period by days, index declaration: (daily)
     * 1   Monday          2   Tuesday             3   Wednesday           4   Thursday
     * 5   Friday          6   Saturday            7   Sunday              0   Average
     */
    public static ObservableList<Long> night = FXCollections.observableArrayList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
    public static ObservableList<Long> morning = FXCollections.observableArrayList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
    public static ObservableList<Long> afternoon = FXCollections.observableArrayList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
    public static ObservableList<Long> evening = FXCollections.observableArrayList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
    /*
     * listening time values for a specified period by months, index declaration: (monthly)
     * 1    January        2    February      ...       12  December       0   Average
     */
    public static ObservableList<Long> months = FXCollections.observableArrayList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
    /* listening time values for the given years */
    public static ObservableList<Long> years = FXCollections.observableArrayList();
    /* Collects the total numbers of listening time and playbacks in a specified period */
    public static IntegerProperty totalListeningTime = new SimpleIntegerProperty(0);
    public static IntegerProperty totalPlaybacks = new SimpleIntegerProperty(0);

    /**
     * Refreshes the listening time values with data from the given period
     *
     * @param from  io.github.picorion.start date
     * @param until end date
     */
    public static void refresh(LocalDate from, LocalDate until) {
        night = FXCollections.observableArrayList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
        morning = FXCollections.observableArrayList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
        afternoon = FXCollections.observableArrayList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
        evening = FXCollections.observableArrayList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
        months = FXCollections.observableArrayList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
        years = FXCollections.observableArrayList();
        totalListeningTime.set(0);
        totalPlaybacks.set(0);
        //creates a list containing all years of the period and adds placeholders to years according to the number of years
        List<String> yearList = getYears(from, until);
        for (int i = 0; i < yearList.size(); i++) {
            years.add(0L);
        }
        int playbackCounter = 0;
        for (PlaybackDatabase database : Data.playbackDatabase) {
            int currentYear = database.getYearMonth().getYear();
            long yearlyListeningTime = 0;
            if ((database.getYearMonth().isAfter(YearMonth.from(from))
                    && database.getYearMonth().isBefore(YearMonth.from(until)))
                    || database.getYearMonth().toString().equals(YearMonth.from(from).toString())
                    || database.getYearMonth().toString().equals(YearMonth.from(until).toString())) {
                for (Playback playback : database.getPlaybacks()) {
                    if (from.isBefore(ChronoLocalDate.from(playback.getDateTime()))
                            && until.isAfter(ChronoLocalDate.from(playback.getDateTime()))
                            || from.isEqual(ChronoLocalDate.from(playback.getDateTime()))
                            || until.isEqual(ChronoLocalDate.from(playback.getDateTime()))) {
                        playbackCounter++;
                        //1 Monday, 2 Tuesday, ... 7 Sunday
                        //converts listening times from milliseconds to seconds
                        if (playback.getDateTime().getDayOfWeek().getValue() == 1) {
                            if (playback.getDateTime().getHour() < 6) {
                                night.set(1, night.get(1) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 12) {
                                morning.set(1, morning.get(1) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 18) {
                                afternoon.set(1, afternoon.get(1) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 24) {
                                evening.set(1, evening.get(1) + (int) (playback.getListeningTime() / 1000));
                            }
                        } else if (playback.getDateTime().getDayOfWeek().getValue() == 2) {
                            if (playback.getDateTime().getHour() < 6) {
                                night.set(2, night.get(2) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 12) {
                                morning.set(2, morning.get(2) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 18) {
                                afternoon.set(2, afternoon.get(2) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 24) {
                                evening.set(2, evening.get(2) + (int) (playback.getListeningTime() / 1000));
                            }
                        } else if (playback.getDateTime().getDayOfWeek().getValue() == 3) {
                            if (playback.getDateTime().getHour() < 6) {
                                night.set(3, night.get(3) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 12) {
                                morning.set(3, morning.get(3) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 18) {
                                afternoon.set(3, afternoon.get(3) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 24) {
                                evening.set(3, evening.get(3) + (int) (playback.getListeningTime() / 1000));
                            }
                        } else if (playback.getDateTime().getDayOfWeek().getValue() == 4) {
                            if (playback.getDateTime().getHour() < 6) {
                                night.set(4, night.get(4) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 12) {
                                morning.set(4, morning.get(4) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 18) {
                                afternoon.set(4, afternoon.get(4) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 24) {
                                evening.set(4, evening.get(4) + (int) (playback.getListeningTime() / 1000));
                            }
                        } else if (playback.getDateTime().getDayOfWeek().getValue() == 5) {
                            if (playback.getDateTime().getHour() < 6) {
                                night.set(5, night.get(5) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 12) {
                                morning.set(5, morning.get(5) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 18) {
                                afternoon.set(5, afternoon.get(5) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 24) {
                                evening.set(5, evening.get(5) + (int) (playback.getListeningTime() / 1000));
                            }
                        } else if (playback.getDateTime().getDayOfWeek().getValue() == 6) {
                            if (playback.getDateTime().getHour() < 6) {
                                night.set(6, night.get(6) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 12) {
                                morning.set(6, morning.get(6) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 18) {
                                afternoon.set(6, afternoon.get(6) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 24) {
                                evening.set(6, evening.get(6) + (int) (playback.getListeningTime() / 1000));
                            }
                        } else if (playback.getDateTime().getDayOfWeek().getValue() == 7) {
                            if (playback.getDateTime().getHour() < 6) {
                                night.set(7, night.get(7) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 12) {
                                morning.set(7, morning.get(7) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 18) {
                                afternoon.set(7, afternoon.get(7) + (int) (playback.getListeningTime() / 1000));
                            } else if (playback.getDateTime().getHour() < 24) {
                                evening.set(7, evening.get(7) + (int) (playback.getListeningTime() / 1000));
                            }
                        }
                        if (playback.getDateTime().getHour() < 6) {
                            night.set(0, night.get(0) + (int) (playback.getListeningTime() / 1000));
                        } else if (playback.getDateTime().getHour() < 12) {
                            morning.set(0, morning.get(0) + (int) (playback.getListeningTime() / 1000));
                        } else if (playback.getDateTime().getHour() < 18) {
                            afternoon.set(0, afternoon.get(0) + (int) (playback.getListeningTime() / 1000));
                        } else if (playback.getDateTime().getHour() < 24) {
                            evening.set(0, evening.get(0) + (int) (playback.getListeningTime() / 1000));
                        }
                    }
                    //1 January, 2 February, ... 12 December
                    //converts listening times from milliseconds to seconds
                    if (playback.getDateTime().getMonth().getValue() == 1
                            && playback.getDateTime().getYear() >= from.getYear() && playback.getDateTime().getYear() <= until.getYear()) {
                        months.set(1, months.get(1) + (playback.getListeningTime() / 1000));
                    } else if (playback.getDateTime().getMonth().getValue() == 2
                            && playback.getDateTime().getYear() >= from.getYear() && playback.getDateTime().getYear() <= until.getYear()) {
                        months.set(2, months.get(2) + (playback.getListeningTime() / 1000));
                    } else if (playback.getDateTime().getMonth().getValue() == 3
                            && playback.getDateTime().getYear() >= from.getYear() && playback.getDateTime().getYear() <= until.getYear()) {
                        months.set(3, months.get(3) + (playback.getListeningTime() / 1000));
                    } else if (playback.getDateTime().getMonth().getValue() == 4
                            && playback.getDateTime().getYear() >= from.getYear() && playback.getDateTime().getYear() <= until.getYear()) {
                        months.set(4, months.get(4) + (playback.getListeningTime() / 1000));
                    } else if (playback.getDateTime().getMonth().getValue() == 5
                            && playback.getDateTime().getYear() >= from.getYear() && playback.getDateTime().getYear() <= until.getYear()) {
                        months.set(5, months.get(5) + (playback.getListeningTime() / 1000));
                    } else if (playback.getDateTime().getMonth().getValue() == 6
                            && playback.getDateTime().getYear() >= from.getYear() && playback.getDateTime().getYear() <= until.getYear()) {
                        months.set(6, months.get(6) + (playback.getListeningTime() / 1000));
                    } else if (playback.getDateTime().getMonth().getValue() == 7
                            && playback.getDateTime().getYear() >= from.getYear() && playback.getDateTime().getYear() <= until.getYear()) {
                        months.set(7, months.get(7) + (playback.getListeningTime() / 1000));
                    } else if (playback.getDateTime().getMonth().getValue() == 8
                            && playback.getDateTime().getYear() >= from.getYear() && playback.getDateTime().getYear() <= until.getYear()) {
                        months.set(8, months.get(8) + (playback.getListeningTime() / 1000));
                    } else if (playback.getDateTime().getMonth().getValue() == 9
                            && playback.getDateTime().getYear() >= from.getYear() && playback.getDateTime().getYear() <= until.getYear()) {
                        months.set(9, months.get(9) + (playback.getListeningTime() / 1000));
                    } else if (playback.getDateTime().getMonth().getValue() == 10
                            && playback.getDateTime().getYear() >= from.getYear() && playback.getDateTime().getYear() <= until.getYear()) {
                        months.set(10, months.get(10) + (playback.getListeningTime() / 1000));
                    } else if (playback.getDateTime().getMonth().getValue() == 11
                            && playback.getDateTime().getYear() >= from.getYear() && playback.getDateTime().getYear() <= until.getYear()) {
                        months.set(11, months.get(11) + (playback.getListeningTime() / 1000));
                    } else if (playback.getDateTime().getMonth().getValue() == 12
                            && playback.getDateTime().getYear() >= from.getYear() && playback.getDateTime().getYear() <= until.getYear()) {
                        months.set(12, months.get(12) + (playback.getListeningTime() / 1000));
                    }
                    if (playback.getDateTime().getYear() >= from.getYear() && playback.getDateTime().getYear() <= until.getYear()) {
                        months.set(0, months.get(0) + (playback.getListeningTime() / 1000));
                    }
                    //sums up the yearly listening time in seconds
                    yearlyListeningTime += playback.getListeningTime() / 1000;
                }
            }
            //adds the current total listening time to the right year in minutes
            years.set(yearList.indexOf(String.valueOf(currentYear)), years.get(yearList.indexOf(String.valueOf(currentYear))) + (yearlyListeningTime / 60));
            totalPlaybacks.set(playbackCounter);
        }
        //calculates the total listening time with the total day time data in minutes before averaging
        totalListeningTime.set((int) ((night.get(0) + morning.get(0) + afternoon.get(0) + evening.get(0)) / 60));
        //calculates the average listening time for the day times in minutes
        int[] days = dayCounter(from, until);
        for (int i = 0; i < 8; i++) {
            if (night.get(i) > 0) night.set(i, night.get(i) / days[i] / 60);
            if (morning.get(i) > 0) morning.set(i, morning.get(i) / days[i] / 60);
            if (afternoon.get(i) > 0) afternoon.set(i, afternoon.get(i) / days[i] / 60);
            if (evening.get(i) > 0) evening.set(i, evening.get(i) / days[i] / 60);
        }
        //calculates the average listening time for the months in minutes
        int[] monthCounter = individualMonthCounter(from, until);
        for (int i = 1; i < 13; i++) {
            if (months.get(i) > 0) months.set(i, months.get(i) / 60 / monthCounter[i]);
        }
        if (months.get(0) > 0) months.set(0, months.get(0) / 60 / totalMonthCounter(from, until));
    }

    /**
     * Counts the number of each weekday in a given period
     *
     * @param from  io.github.picorion.start date
     * @param until end date
     * @return number of each weekday
     */
    private static int[] dayCounter(LocalDate from, LocalDate until) {
        //total days, mondays, tuesdays ...
        int[] days = {0, 0, 0, 0, 0, 0, 0, 0};
        while (from.isBefore(until)) {
            days[0]++;
            days[from.getDayOfWeek().getValue()]++;
            from = from.plusDays(1);
        }
        return days;
    }

    /**
     * Counts the individual months of a time period
     *
     * @param from  io.github.picorion.start date
     * @param until end date
     * @return array containing the individual number of months
     */
    private static int[] individualMonthCounter(LocalDate from, LocalDate until) {
        LocalDate current = from;
        current = current.withDayOfMonth(1);
        int[] monthCounter = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        while (current.isBefore(until) || current.isEqual(until)) {
            monthCounter[current.getMonth().getValue()]++;
            current = current.plusMonths(1);
        }
        return monthCounter;
    }

    /**
     * Counts the months of a given time period
     *
     * @param from  io.github.picorion.start date
     * @param until end date
     * @return number of months
     */
    private static int totalMonthCounter(LocalDate from, LocalDate until) {
        return 13 - from.getMonth().getValue() + until.getMonth().getValue() + ((until.getYear() - from.getYear() - 1) * 12);
    }

    /**
     * Returns an array containing the years when data is available
     *
     * @return String ArrayList
     */
    public static List<String> getYears(LocalDate from, LocalDate until) {
        ArrayList<String> years = new ArrayList<>();
        for (PlaybackDatabase database : Data.playbackDatabase) {
            int year = database.getYearMonth().getYear();
            if (!years.contains(String.valueOf(year)) && (from.getYear() <= year || until.getYear() >= year)) {
                years.add(String.valueOf(year));
            }
        }
        Collections.sort(years);
        return years;
    }

}
