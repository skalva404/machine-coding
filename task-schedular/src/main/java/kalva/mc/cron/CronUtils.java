package kalva.mc.cron;

import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

import static com.cronutils.model.CronType.QUARTZ;

public final class CronUtils {

    public static final CronDefinition cronDefinition;
    public static final CronParser parser;

    static {
        cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(QUARTZ);
        parser = new CronParser(cronDefinition);
    }

    public static Date convertToTime(String cronExpression, Date dateTime) {
        ExecutionTime executionTime = ExecutionTime.forCron(parser.parse(cronExpression));
        Optional<ZonedDateTime> zonedDateTime = executionTime.nextExecution(ZonedDateTime
                .from(dateTime.toInstant().atZone(ZoneId.systemDefault())));
        LocalDateTime localDateTime = zonedDateTime.get().toLocalDateTime();
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
