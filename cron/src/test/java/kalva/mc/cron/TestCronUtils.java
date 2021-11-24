package kalva.mc.cron;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Date;

import static kalva.mc.cron.CronUtils.convertToTime;

public class TestCronUtils {

    @Test
    void simpleTest() {

        System.out.println(ZonedDateTime.now());
        Date localDateTime = convertToTime("0 30 10 * * ? *", new Date());
        for (int i = 1; i <= 10; i++) {
            System.out.println(localDateTime);
            localDateTime = convertToTime("0 30 10 * * ? *", localDateTime);
        }
    }
}
