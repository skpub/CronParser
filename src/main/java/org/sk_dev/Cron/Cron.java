package org.sk_dev.Cron;

public class Cron {
    TimeField min;
    TimeField hour;
    TimeField day;
    TimeField week;
    TimeField month;


    public Cron(String cronStr) {
        String[] cronSettings = cronStr.split(" ");
        if (cronSettings.length != 5) {
            throw new IllegalArgumentException();
        } else {
            min     = new TimeField((byte)0, (byte)59, cronSettings[0]);
            hour    = new TimeField((byte)0, (byte)23, cronSettings[1]);
            day     = new TimeField((byte)1, (byte)31, cronSettings[2]);
            month   = new TimeField((byte)1, (byte)12, cronSettings[3]);
            week    = new TimeField((byte)1, (byte)7,  cronSettings[4]);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(min.toString());
        sb.append(" ");
        sb.append(hour.toString());
        sb.append(" ");
        sb.append(day.toString());
        sb.append(" ");
        sb.append(month.toString());
        sb.append(" ");
        sb.append(week.toString());
        sb.append(" ");
        return sb.toString();
    }
}
