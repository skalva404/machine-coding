package kalva.mc.elevator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class ExternalRequest {

    private int sourceFloor;
    private Direction directionToGo;
}
