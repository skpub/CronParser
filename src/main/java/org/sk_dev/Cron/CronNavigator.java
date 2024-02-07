package org.sk_dev.Cron;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class CronNavigator {
    class DayDial implements DialWithHand<Byte>{
        private final TimeField dayM;
        private final TimeField dayW;
        private final TimeField monthField;

        private LocalDate date;
        // !!! real value - min;
        private Byte dayHand;
        // !!! real value - min;
        private Byte weekHand;

        DayDial(TimeField dayM, TimeField dayW, TimeField monthField, LocalDate date) {
            this.dayM = dayM;
            this.dayW = dayW;
            this.monthField = monthField;
            this.date = date;
        }

        @Override
        public boolean isValid() {
            return this.dayM.dial.get(this.date.getDayOfMonth() - this.dayM.getMin()) |
                this.dayW.dial.get(this.date.getDayOfWeek().getValue() - this.dayW.getMin());
        }

        @Override
        public Byte hand() {
            return (byte) (this.date.getDayOfMonth() - this.dayM.getMin());
        }

        public DayOfWeek weekHand() {
            return this.date.getDayOfWeek();
        }

        @Override
        public int tick() {
            LocalDate i = this.date.plusDays(1);
            int carries = 0;

            for (;; i = i.plusDays(1)) {
                int iMonth = i.getMonth().getValue();
                if (!this.monthField.dial.get(iMonth - this.monthField.getMin())) {
                    i = i.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1);
                    continue;
                }
                int iDayOfMonth = i.getDayOfMonth();
                int iDayOfWeek = i.getDayOfWeek().getValue();
                if (this.dayM.dial.get(iDayOfMonth - this.dayM.getMin()) |
                    this.dayW.dial.get(iDayOfWeek - this.dayW.getMin())
                ) {
                    carries = i.getMonth().getValue()
                        - this.date.getMonth().getValue();
                    this.date = i;
                    break;
                }
            }
            return carries;
        }

        public void reset() {
            this.weekHand = (byte) (this.weekHand + this.dayW.quasiMetric(this.dayHand, this.dayM.getSmallestElem()) % this.dayW.size());
            this.dayHand = this.dayM.getSmallestElem();
        }
        // INNER CLASS      DayDial         end.
        // OUTER CLASS      CronNavigator   start.
    }
    private Cron cron;
    private DialWithHand<Byte> min;
    private DialWithHand<Byte> hour;
    private DayDial date;

    public CronNavigator(Cron cron) {
        this.cron = cron;
        LocalDateTime now = LocalDateTime.now().plusMinutes(1);
        LocalDateTime roundNow = LocalDateTime.of(
            now.getYear(),
            now.getMonth(),
            now.getDayOfMonth(),
            now.getHour(),
            now.getMinute()
        );

        this.min    = new SimpleDial(this.cron.min,     (byte) now.getMinute());
        this.hour   = new SimpleDial(this.cron.hour,    (byte) now.getHour());
        this.date   = new DayDial(
            this.cron.day, this.cron.week, this.cron.month,
            LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth())
        );
        this.tick();
    }

    public String toString() {
        LocalDateTime nowHandDate =  LocalDateTime.of(
            date.date, LocalTime.of(hour.hand(), min.hand())
        );
        return nowHandDate.format(
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
        ) + "(" + this.date.date.getDayOfWeek() + ")";
    }

    public int getYear() {
        return this.date.date.getYear();
    }

    public LocalDate getDate() {
        return LocalDate.of(
            this.date.date.getYear(),
            this.date.date.getMonth().getValue(),
            this.date.date.getDayOfMonth()
        );
    }

    public LocalTime getTime() {
        return LocalTime.of(
            this.hour.hand(),
            this.min.hand()
        );
    }

    public LocalDateTime getDateTime() {
        return LocalDateTime.of(
            this.getDate(),
            this.getTime()
        );
    }

    public LocalDateTime tick() {
        int hourCarry = 0;
        int minCarry = this.min.tick();
        if (minCarry > 0) {
            hourCarry = this.hour.tick();
        }
        else {
            if (!this.hour.isValid()) {
                hourCarry = this.hour.tick();
            }
        }

        if (hourCarry > 0) {
            this.date.tick();
        }
        else {
            if (!this.date.isValid()) {
                this.date.tick();
            }
        }

        return this.getDateTime();
    }
}
