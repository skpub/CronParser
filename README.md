# CronParser
Parse the cron settings and tell you the next scheduled task execution time.

cron の設定をパースし、次の実行時刻を教えてくれます。

```java
Cron cron = new Cron("0 0 * * *");
CronNavigator cronNav = new CronNavigator(cron);
System.out.println(cronNav.getDateTime());
System.out.println(cronNav.tick());
// Output: 2024-01-31T00:00
// Output: 2024-02-01T00:00
```