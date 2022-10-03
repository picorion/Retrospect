package io.github.picorion.model;

/**
 * Object that contains the data of individual platforms listened from
 * <p>
 * <strong>only used for io.github.picorion.analysis of extended data</strong>
 */
public class PlatformData {

    private final String description;
    private long time;  //must be divided by thousand to prevent overflow before adding

    public PlatformData(String description, long time) {
        this.description = description;
        this.time = time;
    }

    public String getDescription() { return description; }

    public void addTime(long time) { this.time += time; }
    public long getTime() { return time; }

}
