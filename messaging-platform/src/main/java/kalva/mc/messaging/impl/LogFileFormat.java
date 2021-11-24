package kalva.mc.messaging.impl;

import java.nio.file.Path;

public class LogFileFormat {

    private Path path;

    public String metaFileFormat() {
        return path.toString() + "/" + "metadata.info";
    }

    public String dataFileFormat(Long from, Long to) {
        return path.toString() + "/" + from + "-" + (from + to) + ".log";
    }
}
