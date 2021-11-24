package fk.actor;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class Config {
    private Integer mailBoxSize = 100;
    private Integer threadPoolSize = 10;
}
