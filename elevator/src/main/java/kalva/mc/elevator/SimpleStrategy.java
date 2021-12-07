package kalva.mc.elevator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class SimpleStrategy implements Strategy {

    private Set<ElivatorRequest> upward;

    private Set<ElivatorRequest> downward;

    private Integer totalUpPending;
    private Integer totalDownPending;

    @Override
    public ElivatorRequest run() {
        ElivatorRequest returnRequest = null;
        if (totalUpPending > totalDownPending) {
            if (!upward.isEmpty()) {
                returnRequest = upward.iterator().next();
                upward.remove(returnRequest);
            }
        }
        if (null == returnRequest) {
            if (!downward.isEmpty()) {
                returnRequest = downward.iterator().next();
                downward.remove(returnRequest);
            }
        }
        return returnRequest;
    }
}
