package org.sk_dev;
import org.sk_dev.Cron.Cron;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CronNavigator {
    private Cron cron;
    private int year;
    private byte month;
    private byte day;
    private byte week;
    private byte hour;
    private byte minute;
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
        this.month  = (byte) now.getMonth().getValue();
        this.day    = (byte) now.getDayOfMonth();
        this.week   = (byte) now.getDayOfWeek().getValue();
        this.hour   = (byte) now.getHour();
        this.minute = (byte) now.getMinute();
    }
}
