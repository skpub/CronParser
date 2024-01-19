package org.sk_dev;

import java.util.*;
import java.util.stream.Stream;

public class TimeField {
    private byte min;
    private byte max;
    private byte size;

    private RingList<Byte> first;
    private RingList<Byte> current;

    public TimeField(byte size, byte min, byte max, String str) {
        this.min = min;
        this.max = max;
        this.size = size;
        this.first = new RingList<>();
        this.current = this.first;
        str2TF(str);
    }

//    private void add(String str) throws NumberFormatException, IllegalArgumentException {
//        byte v;
//        try {
//            v = Byte.parseByte(str);
//        }
//        catch (NumberFormatException e){
//            throw new NumberFormatException();
//        }
//        if (v < this.min || this.max < v) {
//            throw new IllegalArgumentException();
//        } else {
//            this.current.add(v);
//        }
//    }

    private void str2TF(String str) throws IllegalArgumentException, NumberFormatException {
        switch (str) {
            case "*":
                // this.current has already initialized with Optional.empty(),
                // so,
                break;
            default:
//                System.out.println("\n_START_");
//                System.out.println("    motomoto: " + str);
                // str: "2, /10, 7-9"
                Arrays.stream(str.split(","))
//                    .map(withSpaceMaybe -> withSpaceMaybe.replaceAll(" ", ""))
                    .forEach(bunch -> {
//                        System.out.println("        " + bunch);
                        // bunch: "2", "/10", "7-9"
                        if (bunch.contains("-")) {
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
                                    add(i);
                                }
                            }
                        } else if (bunch.contains("/")) {
                            // bunch: "/10"
                            byte mod;
                            try {
                                mod = Byte.valueOf(bunch.split("/")[1]);
                            } catch (NumberFormatException e) {
                                throw new NumberFormatException();
                            }
                            for (byte i = 0; i < this.max; i += mod) {
                                add(i);
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
                                add(v);
                            }
                        }
                    });
        }
    }

    public String toString() {
        return first.toString();
    }

    private void add(byte v){
        this.first.add(v);
    }
}
