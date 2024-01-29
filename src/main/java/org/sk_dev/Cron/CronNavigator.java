package org.sk_dev.Cron;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

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
            System.out.println("PRE  dayHand:  " + this.dayHand);
            System.out.println("PRE  weekHand: " + this.weekHand);
            byte dayOfMonthNext = (byte) this.dayM.dial.stream()
                .filter(v -> v > this.dayHand)
                .filter(v -> this.isValidDate(v))
                .findFirst()
                .orElse(this.dayM.getMin());
            byte dayOfMonthSub = this.dayM.quasiMetric(this.dayHand, dayOfMonthNext);

            // Search for the first valid day of the week between this.dayHand and dayOfMonthNext;
            byte candidateOffset = Byte.MAX_VALUE;
            for (byte i = 1; i <= dayOfMonthSub; i++) {
                byte targetDayOfMonth = DialWithHand.MinNonNegReminder(
                    this.dayHand + i,
                    this.realMax()
                );
                byte targetDayOfWeek = DialWithHand.MinNonNegReminder(
                    this.dayOfWeek(targetDayOfMonth),
                    this.dayW.size()
                );
                System.out.println("targetDayOfMonth " + targetDayOfMonth);
                System.out.println("targetDayOfWeek " + targetDayOfWeek);
                if (this.dayW.dial.get(targetDayOfWeek)) {
                    System.out.println("candidate: " + i);
                    candidateOffset = i;
                    break;
                }
            }
            byte idealOffset = (byte) Integer.min(candidateOffset, dayOfMonthSub);
            boolean carry = this.dayHand + idealOffset > this.realMax() - 1;
            this.dayHand = DialWithHand.MinNonNegReminder(
                this.dayHand + idealOffset,
                this.realMax()
            );
            this.weekHand = DialWithHand.MinNonNegReminder(
                this.weekHand + idealOffset,
                this.dayW.size()
            );
            System.out.println("POST dayHand:  " + this.dayHand);
            System.out.println("POST weekHand: " + this.weekHand);
            return carry;
        }

        private byte dayOfWeek(byte day) {
            return (byte) (LocalDate.of(CronNavigator.this.year,
                    CronNavigator.this.month.hand(),
                    day + 1)
                .getDayOfWeek().getValue() - 1
            );
        }

        @Override
        public boolean next(Byte referenceValue) {
            return false;
        }

        private byte realMax() {
            int y = CronNavigator.this.year;
            byte m = CronNavigator.this.month.hand();
            return (byte) (y % 4 == 0 & ! (y % 100 == 0 & y % 400 != 0) & m == 2 ?
                29 : m == 1 | m == 3 | m == 5 | m == 7 | m == 8 | m == 10 | m == 12 ?
                    31 : m == 2 ?
                        28:
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
        ) + "(" + DayOfWeek.of(day.weekHand() + 1) + ")";
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
