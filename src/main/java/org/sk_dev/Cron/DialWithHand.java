package org.sk_dev.Cron;

public interface DialWithHand<T extends Comparable<T>> {
    T hand();
    int tick();
    boolean isValid();

    static byte MinNonNegReminder(int a, byte b) {
        return (byte) (
            a < 0 ?
                a % b + b :
                a % b
        );
    }

    void reset();
}
