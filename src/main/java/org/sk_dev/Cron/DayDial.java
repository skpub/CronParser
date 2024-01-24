package org.sk_dev.Cron;

public class DayDial implements DialWithHand<Byte>{
    private final TimeField dayM;
    private final TimeField dayW;

    private Byte dayHand;
    private Byte weekHand;

    DayDial(TimeField dayM, TimeField dayW, byte dayHand, byte weekHand) {
        this.dayM = dayM;
        this.dayW = dayW;
        this.dayHand = dayHand;
        this.weekHand = weekHand;
    }

    @Override
    public Byte hand() {
        return this.dayHand;
    }

    public byte weekHand() {
        return this.weekHand;
    }

    @Override
    public boolean tick() {
        byte dayMNext = (this.dayM.getBiggestElem() < this.dayHand) ?
            this.dayM.getSmallestElem() :
            (byte) this.dayM.dial.stream()
                .filter(v -> v > this.dayHand)
                .findFirst()
                .orElseThrow();

        byte dayWNext = (this.dayW.getBiggestElem() < this.weekHand) ?
            this.dayW.getSmallestElem() :
            (byte) this.dayW.dial.stream()
                .filter(v -> v > this.weekHand)
                .findFirst()
                .orElseThrow();

        byte dayMSub = dayM.quasiMetric(this.dayHand,  dayMNext);
        byte dayWSub = dayW.quasiMetric(this.weekHand, dayWNext);

        if (dayMSub > dayWSub) {
            // Adopt the next day of "Week" according to the cron setting.
            this.dayHand = (byte) ((dayHand + dayWSub) % this.dayM.size());
            this.weekHand = (byte) ((weekHand + dayWSub) % this.dayW.size());
            return this.dayHand + dayWSub >= this.dayW.size();
        } else {
            // Adopt the next day of "Month" according to the cron setting.
            this.dayHand = (byte) ((dayHand + dayMSub) % (this.dayM.size()));
            this.weekHand = (byte) ((weekHand + dayMSub) % this.dayW.size());
            return this.dayHand + dayMSub >= this.dayM.size();
        }
    }

    @Override
    public boolean next(Byte referenceValue) {
        return false;
    }
}
