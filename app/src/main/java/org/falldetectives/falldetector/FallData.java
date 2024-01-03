package org.falldetectives.falldetector;

public class FallData {
    private long timestamp;
    private boolean isFalseAlarm;

    public FallData(long timestamp, boolean isFalseAlarm) {
        this.timestamp = timestamp;
        this.isFalseAlarm = isFalseAlarm;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isFalseAlarm() {
        return isFalseAlarm;
    }
}
