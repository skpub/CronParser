package org.sk_dev.Cron;

class ClockHand implements ClockWithHand<Byte> {
    private TimeField v;
    private Byte hand;
    ClockHand(TimeField v) {
        this.v = v;
        this.hand = this.v.getSmallestElem();
    }
    @Override
    public Byte hand() {
        return this.hand;
    }

    @Override
    public boolean tick() {
        if (this.v.getBiggestElem() < this.hand) {
            this.hand = this.v.getSmallestElem();
            return true;
            // Carry-over occurred.
        } else { // NO carry-over occurred.
            this.hand = (byte) this.v.dial.stream()
                .filter(v -> v>this.hand)
                .findFirst()
                .orElseThrow();
            return false;
        }
    }

    @Override
    public boolean next(Byte referenceValue) {
        return false;
    }
}
