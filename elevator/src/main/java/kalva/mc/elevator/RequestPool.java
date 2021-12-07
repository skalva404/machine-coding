package kalva.mc.elevator;

import lombok.Singleton;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Singleton(style = Singleton.Style.HOLDER)
public class RequestPool {

    private static Integer totalUpPending = 0;
    private static Integer totalDownPending = 0;
    private static Map<Integer, Set<ElivatorRequest>> upwards = new HashMap<>();
    private static Map<Integer, Set<ElivatorRequest>> downwards = new HashMap<>();

    public static void addRequest(ElivatorRequest request) {
        switch (request.direction()) {
            case UP -> addUpwardRequest(request);
            case DOWN -> addDownwardRequest(request);
        }
    }

    private static ElivatorRequest getRequest(Integer floor) {
        ElivatorRequest request = new SimpleStrategy(upwards.get(floor), downwards.get(floor),
                totalUpPending, totalDownPending)
                .run();
        if (null == request) {
            return null;
        }
        if (Direction.UP.equals(request.direction())) {
            totalUpPending--;
        } else {
            totalDownPending--;
        }
        return request;
    }

    private static synchronized void addUpwardRequest(ElivatorRequest request) {
        Set<ElivatorRequest> list = upwards.getOrDefault(request.source(), new HashSet<>());
        list.add(request);
        totalUpPending++;
    }

    private static synchronized void addDownwardRequest(ElivatorRequest request) {
        Set<ElivatorRequest> list = downwards.getOrDefault(request.source(), new HashSet<>());
        list.add(request);
        totalDownPending++;
    }
}
