package org.sk_dev.Cron;

interface ClockWithHand<T extends Comparable<T>> {
    T hand();
    boolean tick();
    boolean next(T referenceValue);
}
