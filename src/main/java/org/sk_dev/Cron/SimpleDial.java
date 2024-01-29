package org.sk_dev.Cron;

class SimpleDial implements DialWithHand<Byte> {
    private TimeField v;
    private Byte hand;
    SimpleDial(TimeField v, Byte hand) {
        this.v = v;
        this.hand = hand;
    }
    @Override
    public Byte hand() {
        return this.hand;
    }

    @Override
    public int tick() {
        if (this.v.getBiggestElem() <= this.hand + 1) {
            this.hand = this.v.getSmallestElem();
            return 1;
            // Carry-over occurred.
        } else { // NO carry-over occurred.
            this.hand = (byte) this.v.dial.stream()
                .filter(v -> v > this.hand)
                .findFirst()
                .orElseThrow();
            return 0;
        }
    }

    public void reset() {
        this.hand = this.v.getSmallestElem();
    }

    @Override
    public boolean next(Byte referenceValue) {
        return false;
    }

    @Override
    public boolean isValid() {
        return this.v.dial.get(this.hand);
    }
}
