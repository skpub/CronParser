package org.sk_dev.Cron;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CronTest {
    @Test
    void generateCron() {
        //                          min hour day week month
        Cron cron = new Cron("0-5,59 0,6-20/5,23 1/6,31 0-6 *");
        Cron all = new Cron("* * * * *");
        System.out.println(cron.toString());
        Arrays.stream(all.toString().split(" ")).forEach(
            System.out::println
        );
    }
}