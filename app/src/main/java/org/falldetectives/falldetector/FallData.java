package org.falldetectives.falldetector;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FallData {
    private long timestamp;
    private boolean isFalseAlarm;
    private String userName;

    public FallData(long timestamp, boolean isFalseAlarm, String userName) {
        this.timestamp = timestamp;
        this.isFalseAlarm = isFalseAlarm;
        this.userName = userName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isFalseAlarm() {
        return isFalseAlarm;
    }

    public String getUserName() {
        return userName;
    }
    public String getFormattedTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    public String getFalseAlarmStatus() {
        return isFalseAlarm ? "False alarm" : "Real fall";
    }
}
