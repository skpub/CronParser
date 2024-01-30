package org.sk_dev.Cron;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CronNavigatorTest {
    @Test
    void TestCronNavigator() {
        Cron cron = new Cron("*/30 */8 */7 */4 1,4");
        System.out.println("cron: " + cron.toString());

        CronNavigator cronNav = new CronNavigator(cron);
        System.out.println("now: " + cronNav);

        for (int i = 0;i<500;i++) {
            if (cronNav.getYear() == LocalDate.now().getYear() + 1)
                break;
            cronNav.tick();
            System.out.println("now + " + i + ": " + cronNav);
        }
    }
    @Test
    void CronNavigatorBoundaryCheck() {
        Cron cron = new Cron("0,59 0,23 1,31 * 1,7");
        System.out.println("cron: " + cron.toString());

        CronNavigator cronNav = new CronNavigator(cron);
        System.out.println("now: " + cronNav);

        for (int i = 0; i<500;i++) {
            if (cronNav.getYear() == LocalDate.now().getYear() + 1)
                break;
            cronNav.tick();
            System.out.println("now + " + i + ": " + cronNav);
        }
    }
    @Test
    void sampleTest() {
        Cron cron = new Cron("0 0 * * *");
        CronNavigator cronNav = new CronNavigator(cron);
        System.out.println(cronNav.getDateTime());
        System.out.println(cronNav.tick());
    }
}