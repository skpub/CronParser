package org.sk_dev;

public class Main {
    public static void main (String[] args) {
        Cron cron = new Cron("3,2,7 3 3-9 3 10");
        System.out.println(cron.toString());
    }
}