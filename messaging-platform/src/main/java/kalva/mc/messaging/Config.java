package kalva.mc.messaging;

import lombok.Builder;

@Builder
public class Config {

    private Integer serverPort = 5454;
    private Integer fileBufferSize = 100;
}
