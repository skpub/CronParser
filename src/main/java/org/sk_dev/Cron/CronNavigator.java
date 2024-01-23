package org.sk_dev.Cron;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class CronNavigator {
    private Cron cron;
    private int year;
    private ClockHand min;
    private ClockHand hour;
    private ClockHand day;
    private ClockHand month;
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
        this.min    = new ClockHand(this.cron.min);
        this.hour   = new ClockHand(this.cron.hour);
        this.day    = new ClockHand(this.cron.day);
        this.month  = new ClockHand(this.cron.month);

        byte now_year   = (byte) now.getYear();
        byte now_month  = (byte) now.getMonth().getValue();
        byte now_day    = (byte) now.getDayOfMonth();
        byte now_week   = (byte) now.getDayOfWeek().getValue();
        byte now_hour   = (byte) now.getHour();
        byte now_min    = (byte) now.getMinute();
    }
}
