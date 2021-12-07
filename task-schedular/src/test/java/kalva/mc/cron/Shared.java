package kalva.mc.cron;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Shared {

    public static List<AtomicBoolean> executed = new ArrayList<>();
    public static Integer count = 0;
    public static Integer n = 10;

    static {
        for (int i = 0; i < n; i++) {
            executed.add(new AtomicBoolean(false));
        }
    }

    public static void reset() {
        for (int i = 0; i < n; i++) {
            executed.add(new AtomicBoolean(false));
        }
    }
}
