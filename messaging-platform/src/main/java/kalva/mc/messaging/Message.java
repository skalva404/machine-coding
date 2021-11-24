package kalva.mc.messaging;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class Message {

    private Long offsetId;
    private byte[] body;

    public Message(Long offsetId, byte[] body) {
        this.offsetId = offsetId;
        this.body = body;
    }

    public Message(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return offsetId + "=>" + new String(body);
    }
}
