package org.sk_dev;

import java.util.Optional;

public class Main {
    public static void main (String[] args) {
        Cron cron = new Cron("/10 7 12-16 * 1,2,3,4");
        System.out.println(cron.toString());
    }
}