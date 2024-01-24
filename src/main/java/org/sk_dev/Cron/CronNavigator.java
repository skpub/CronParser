package org.sk_dev.Cron;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Set;

public class CronNavigator {
    private Cron cron;
    private int year;
    private DialWithHand<Byte> min;
    private DialWithHand<Byte> hour;
    private DayDial day;
    private DialWithHand<Byte> month;
    private LocalDate currentDate;


    CronNavigator(Cron cron) {
        this.cron = cron;
        LocalDateTime now = LocalDateTime.now().plusMinutes(1);
        LocalDateTime roundNow = LocalDateTime.of(
            now.getYear(),
            now.getMonth(),
            now.getDayOfMonth(),
            now.getHour(),
            now.getMinute()
        );

        this.year   = now.getYear();
        this.min    = new SimpleDial(this.cron.min,     (byte) now.getMinute());
        this.hour   = new SimpleDial(this.cron.hour,    (byte) now.getHour());
        this.month  = new SimpleDial(this.cron.month,   (byte) now.getMonth().getValue());
        this.day    = new DayDial(
            this.cron.day, this.cron.week,
            (byte) now.getDayOfMonth(), (byte) now.getDayOfWeek().getValue()
        );
    }

    public String toString() {
        LocalDateTime nowHandDate =  LocalDateTime.of(
            year, month.hand(), day.hand(), hour.hand(), min.hand()
        );
        return nowHandDate.format(
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
        ) + "(" + DayOfWeek.of(day.weekHand()) + ")";
    }
}
