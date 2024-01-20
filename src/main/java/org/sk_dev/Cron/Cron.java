package org.sk_dev.Cron;

public class Cron {
    TimeField mins;
    TimeField hours;
    TimeField days;
    TimeField weeks;
    TimeField months;

    public Cron(String cronStr) {
        String[] cronSettings = cronStr.split(" ");
        if (cronSettings.length != 5) {
            throw new IllegalArgumentException();
        } else {
            this.mins =      new TimeField((byte)0, (byte)59, cronSettings[0]);
            this.hours =     new TimeField((byte)0, (byte)23, cronSettings[1]);
            this.days =      new TimeField((byte)1, (byte)31, cronSettings[2]);
            this.weeks =     new TimeField((byte)0, (byte)6,  cronSettings[3]);
            this.months =    new TimeField((byte)1, (byte)12, cronSettings[4]);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(mins.toString());
        sb.append(" ");
        sb.append(hours.toString());
        sb.append(" ");
        sb.append(days.toString());
        sb.append(" ");
        sb.append(weeks.toString());
        sb.append(" ");
        sb.append(months.toString());
        sb.append(" ");
        return sb.toString();
    }
}
