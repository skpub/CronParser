package org.sk_dev.Cron;

import java.util.*;

class TimeField {
    private final byte min;
    private final byte max;
    final BitSet dial;
    // Bucket data.
    // ex.  0b10010011
    //      -> 7, 4, 1, 0

    public byte getMin() {
        return this.min;
    }

    public byte getMax() {
        return this.max;
    }

    TimeField(byte min, byte max, String str) {
        this.min = min;
        this.max = max;
        this.dial = new BitSet(this.max-this.min);
        str2TF(str);
    }

    private void str2TF(String str) throws IllegalArgumentException, NumberFormatException {
        if (str.equals("*")) {
            this.dial.flip(0, this.max-this.min+1);
        } else {
            // str: "2,*/10,7-9,0-30/3"
            Arrays.stream(str.split(","))
                .forEach(bunch -> {
                    // bunch: "2", "*/10", "7-9", "0-30/3"
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
                                dial.set(i-this.min);
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
                                    dial.set(i-this.min);
                                }
                            }
                        }
                    } else if (bunch.contains("/")) {
                        // bunch: "*/10"
                        byte mod;
                        try {
                            mod = Byte.parseByte(bunch.split("/")[1]);
                        } catch (NumberFormatException e) {
                            throw new NumberFormatException();
                        }
                        for (byte i = 0; i <= this.max; i += mod) {
                            if (i < this.min) continue;
                            dial.set(i-this.min);
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
                            System.err.println(
                                "expected range is [" + this.min + "," + this.max +"].\n"
                                + "but " + v + " were passed."
                            );
                            throw new IllegalArgumentException();
                        } else {
                            dial.set(v-this.min);
                        }
                    }
                });
        }
    }


    byte quasiMetric(byte from, byte to) {
        return DialWithHand.MinNonNegReminder(
            to - from,
            this.size()
        );
    }

    public String toString() {
        String temp = this.dial.stream()
            .mapToObj(v -> String.valueOf(v+this.min))
            .reduce("", (acc,v)-> acc + v + ",");
        return temp.substring(0, temp.lastIndexOf(","));
    }

    public byte getBiggestElem() {
        return (byte) (this.dial.previousSetBit(this.max-this.min) + this.min);
    }

    public byte size() {
        return (byte) (this.max - this.min + 1);
    }

    public byte getSmallestElem() {
        return (byte) (this.dial.nextSetBit(0) + this.min);
    }
}
