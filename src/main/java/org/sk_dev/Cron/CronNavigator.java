package org.sk_dev.Cron;

import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class CronNavigator {
    class DayDial implements DialWithHand<Byte>{
        private final TimeField dayM;
        private final TimeField dayW;

        // !!! real value - min;
        private Byte dayHand;
        // !!! real value - min;
        private Byte weekHand;

        DayDial(TimeField dayM, TimeField dayW, byte dayHand, byte weekHand) {
            this.dayM = dayM;
            this.dayW = dayW;
            this.dayHand = dayHand;
            this.weekHand = weekHand;
        }

        @Override
        public Byte hand() {
            return (byte) (this.dayHand + this.dayM.getMin());
        }

        public byte weekHand() {
            return this.weekHand;
        }

        @Override
        public boolean tick() {
            // Of course preDay is real value - min;
            byte preDay = this.dayHand;
            byte dayMNext = (byte) (this.dayM.dial.stream()
                .filter(v -> v > this.dayHand)              // elements bigger than current dayHand.
                .filter(v -> this.isValidDate((byte) v))  // valid Dates (excludes 04/31, 2020/02/28 or etc.)
                .findFirst()
                .orElse(this.dayM.getSmallestElem()));

            byte dayWNext = (byte) (this.dayW.dial.stream()
                .filter(v -> v > this.weekHand)         // elements bigger than current weekHand.
                .filter(v -> this.isValidDate((byte) (
                    this.dayHand + dayW.quasiMetric(this.weekHand, (byte) v) )))
                .findFirst()
                .orElse(this.dayW.getSmallestElem()));

            byte dayMSub = dayM.quasiMetric(this.dayHand,  dayMNext);
            byte dayWSub = dayW.quasiMetric(this.weekHand, dayWNext);

            System.out.println("MNxt :" + dayMNext);
            System.out.println("WNxt :" + dayWNext);
            System.out.println("MSub : " + dayMSub);
            System.out.println("WSub : " + dayWSub);

            boolean carry = false;
            if (dayMSub > dayWSub) {
                // Adopt the next day of "Week" according to the cron setting.
                if (this.isValidDate(this.dayHand + dayWSub)) {
                    this.dayHand = (byte) (this.dayHand + dayWSub);
                } else {
                    // if it's 2020/04/29 and dayWsub= 6 then
                    // this.dayHand  + dayWSub  -   this.realMax()
                    // 29            +  6       -   30              = 5 -> 2020/05/05
                    this.dayHand = (byte) (this.dayHand + dayWSub - this.realMax());
                }
                this.weekHand = dayWNext;
            } else {
                // Adopt the next day of "Month" according to the cron setting.
                this.dayHand = dayMNext;
                System.out.println("\npre: " + this.weekHand);
                System.out.println("dayMSub = " + dayMSub);
                this.weekHand = DialWithHand.MinNonNegReminder(this.weekHand + dayMSub, this.dayW.size());
                System.out.println("\npost:" + this.weekHand);
            }
            return preDay >= this.dayHand;
        }

        @Override
        public boolean next(Byte referenceValue) {
            return false;
        }

        private byte realMax() {
            int y = CronNavigator.this.year;
            byte m = CronNavigator.this.month.hand();
            return (byte) (y % 4 == 0 & ! (y % 100 == 0 & y % 400 != 0) & m == 2 ?
                28 : m == 1 | m == 3 | m == 5 | m == 7 | m == 8 | m == 10 | m == 12 ?
                    31:
                    30
            );
        }

        private boolean isValidDate(int dayMinusMin) {
            return dayMinusMin + this.dayM.getMin() <= realMax();
        }

        public void reset() {
            this.weekHand = (byte) (this.weekHand + this.dayW.quasiMetric(this.dayHand, this.dayM.getSmallestElem()) % this.dayW.size());
            this.dayHand = this.dayM.getSmallestElem();
        }
        // INNER CLASS      DayDial         end.
        // OUTER CLASS      CronNavigator   start.
    }
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
            (byte) (now.getDayOfMonth() - 1), (byte) (now.getDayOfWeek().getValue() - 1)
        );
    }

    public String toString() {
        LocalDateTime nowHandDate =  LocalDateTime.of(
            year, month.hand(), day.hand(), hour.hand(), min.hand()
        );
        return nowHandDate.format(
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
        ) + "(" + DayOfWeek.of(day.weekHand() + this.cron.week.getMin()) + ")";
    }

    public int getYear() {
        return this.year;
    }

    public void tick() {
        if (this.min.tick()) {
            if (this.hour.tick()) {
                this.min.reset();
                if (this.day.tick()) {
                    this.hour.reset();
                    if (this.month.tick()) {
                        this.year++;
                        this.day.reset();
                    }
                } else {
                }
            }
        }
    }
}
