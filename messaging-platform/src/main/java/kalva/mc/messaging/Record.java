package kalva.mc.messaging;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class Record {

    private Long offsetId;
    private final byte[] body;

    public Record(Long offsetId, byte[] body) {
        this.offsetId = offsetId;
        this.body = body;
    }

    public Record(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return offsetId + "=>" + new String(body);
    }
}
