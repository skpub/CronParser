package org.sk_dev;

import java.util.Arrays;
import java.util.Optional;

public class Cron {
    TimeField mins;
    TimeField hours;
    TimeField days;
    TimeField weeks;
    TimeField months;

    public Cron(String cronStr) {
        mins =      new TimeField((byte)60, (byte)0, (byte)59);
        hours =     new TimeField((byte)24, (byte)0, (byte)23);
        days =      new TimeField((byte)31, (byte)1, (byte)31);
        weeks =     new TimeField((byte)0,  (byte)0, (byte)6);
        months =    new TimeField((byte)12, (byte)1, (byte)12);


    }

    private void parse(String cronStr) {
        Arrays.stream(cronStr.split(",")).is
    }
}
