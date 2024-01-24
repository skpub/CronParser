package org.sk_dev.Cron;

interface DialWithHand<T extends Comparable<T>> {
    T hand();
    boolean tick();
    boolean next(T referenceValue);
}
