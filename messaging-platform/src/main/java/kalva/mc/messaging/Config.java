package kalva.mc.messaging;

import java.util.HashMap;
import java.util.Map;

public class Config {

    private Map<String, String> config = new HashMap<>();

    public void addConfig(String key, String value) {
        config.put(key, value);
    }

    public String getConfig(String key) {
        return config.get(key);
    }

}
