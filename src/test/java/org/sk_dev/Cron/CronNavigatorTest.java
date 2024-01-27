package org.sk_dev.Cron;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CronNavigatorTest {
    @Test
    void TestCronNavigator() {
        Cron cron = new Cron("*/30 */8 */7 1,4, *");
        System.out.println("cron: " + cron.toString());

        CronNavigator cronNav = new CronNavigator(cron);
        System.out.println("now: " + cronNav);

        for (int i = 0;i<500;i++) {
            if (cronNav.getYear() == 2025) break;
            cronNav.tick();
            System.out.println("now + " + i + ": " + cronNav);
        }
    }
}