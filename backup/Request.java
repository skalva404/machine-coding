package kalva.mc.elevator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class Request implements Comparable<Request> {

    private InternalRequest internalRequest;
    private ExternalRequest externalRequest;

    @Override
    public int compareTo(Request req) {
        return Integer.compare(this.internalRequest().destinationFloor(), req.internalRequest().destinationFloor());
    }
}
