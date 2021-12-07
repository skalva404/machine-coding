package kalva.mc.elevator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public class InternalRequest {
    private int destinationFloor;
}
