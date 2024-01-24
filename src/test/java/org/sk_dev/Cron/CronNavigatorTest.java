package org.sk_dev.Cron;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CronNavigatorTest {
    @Test
    void TestCronNavigator() {
        Cron cron = new Cron("0 /6 /3 0,3, *");
        System.out.println("cron: " + cron.toString());

        CronNavigator cronNav = new CronNavigator(cron);
        System.out.println("now: " + cronNav);
    }
}