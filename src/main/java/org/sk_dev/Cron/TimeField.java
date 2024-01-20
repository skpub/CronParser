package org.sk_dev.Cron;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;

class TimeField {
    private final byte min;
    private final byte max;
    private final BitSet field;
    // Bucket data.
    // ex.  0b10010011
    //      -> 7, 4, 1, 0

    TimeField(byte min, byte max, String str) {
        this.min = min;
        this.max = max;
        this.field = new BitSet(this.max-this.min+1);
        str2TF(str);
    }

    private void str2TF(String str) throws IllegalArgumentException, NumberFormatException {
        if (str.equals("*")) {
            this.field.flip(0, this.max-this.min+1);
        } else {
            // str: "2,/10,7-9,0-30/3"
            Arrays.stream(str.split(","))
                .forEach(bunch -> {
                    // bunch: "2", "/10", "7-9", "0-30/3"
                    if (bunch.contains("-")) {
                        // bunch: "7-9", "0-30/3"
                        if (bunch.contains("/")) {
                            // bunch: "0-30/3"
                            byte from, to, mod;
                            try {
                                from = Byte.parseByte(bunch.split("-")[0]);
                                to = Byte.parseByte(bunch.split("-")[1].split("/")[0]);
                                mod = Byte.parseByte(bunch.split("/")[1]);
                            } catch (NumberFormatException e) {
                                throw new NumberFormatException();
                            }
                            for (byte i = from; i <= to; i+=mod) {
                                field.set(i-this.min);
                            }
                        } else {
                            // bunch: "7-9"
                            if (bunch.split("-").length != 2) {
                                throw new IllegalArgumentException();
                            } else {
                                byte from;
                                byte to;
                                try {
                                    from = Byte.parseByte(bunch.split("-")[0]);
                                    to = Byte.parseByte(bunch.split("-")[1]);
                                } catch (NumberFormatException e) {
                                    throw new NumberFormatException();
                                }
                                if (from < this.min || this.max < to) {
                                    throw new IllegalArgumentException();
                                }
                                for (byte i = from; i <= to; i++) {
                                    field.set(i-this.min);
                                }
                            }
                        }
                    } else if (bunch.contains("/")) {
                        // bunch: "/10"
                        byte mod;
                        try {
                            mod = Byte.parseByte(bunch.split("/")[1]);
                        } catch (NumberFormatException e) {
                            throw new NumberFormatException();
                        }
                        for (byte i = this.min; i <= this.max; i += mod) {
                            field.set(i-this.min);
                        }
                    } else {
                        // bunch: "2"
                        byte v;
                        try {
                            v = Byte.parseByte(bunch);
                        } catch (NumberFormatException e) {
                            throw new NumberFormatException();
                        }
                        if (v < this.min || this.max < v) {
                            throw new IllegalArgumentException();
                        } else {
                            field.set(v-this.min);
                        }
                    }
                });
        }
    }

    public String toString() {
        return this.field.stream()
            .mapToObj(v -> String.valueOf(v+this.min))
            .reduce("", (acc,v)-> acc + v + ",");
    }
}
