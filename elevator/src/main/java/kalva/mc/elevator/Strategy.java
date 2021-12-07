package kalva.mc.elevator;

import java.util.Set;

public interface Strategy {

    Set<ElivatorRequest> upward();

    Set<ElivatorRequest> downward();

    ElivatorRequest run();
}
