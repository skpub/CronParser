package org.sk_dev;

import java.util.*;

public class TimeField {
    private byte min;
    private byte max;
    private byte size;
    private Optional<SortedSet<Byte>> pool;

    public TimeField(byte size, byte min, byte max) {
        this.min = min;
        this.max = max;
        this.size = size;
        pool = Optional.empty();
    }

    public void add(byte elem) throws IllegalArgumentException {
        if (elem < min || max < elem) {
            throw new IllegalArgumentException(
                "expected [" + min + "-" + max + "] range value but the argument was " + elem);
        } else {
            pool.ifPresentOrElse(
                p -> p.add(elem),
                () -> {
                    pool = Optional.of(new TreeSet<Byte>() {
                        {
                            add(elem);
                        }
                    });
                }
            );
        }
    }
}
