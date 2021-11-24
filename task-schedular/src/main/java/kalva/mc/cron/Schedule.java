package kalva.mc.cron;

import java.util.Date;

public record Schedule(Date startDate, Long repeatInMills, Integer numberOfTime) {

    public static Schedule withOneTime(Date scheduledTime) {
        return new Schedule(scheduledTime, 0L, 1);
    }

    public static Schedule withRecurring(Date scheduledTime, Long repeatInMills, Integer times) {
        return new Schedule(scheduledTime, repeatInMills, times);
    }

    public static Schedule withDelayed(Long delayedTimeInMillis) {
        Date newDate = new Date(System.currentTimeMillis() + delayedTimeInMillis);
        return new Schedule(newDate, 0L, 1);
    }
}
