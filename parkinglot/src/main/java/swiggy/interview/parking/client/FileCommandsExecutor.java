package swiggy.interview.parking.client;

import swiggy.interview.parking.Service;
import swiggy.interview.parking.Utils;
import swiggy.interview.parking.db.ParkingSlotCarMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileCommandsExecutor implements Service {

    private String commandsFile;

    public FileCommandsExecutor(String commandsFile) {
        this.commandsFile = commandsFile;
    }

    @Override
    public FileCommandsExecutor start() throws IOException {
        final ParkingSlotCarMapping[] table = {null};
        try (Stream<String> stream = Files.lines(Paths.get(commandsFile))) {
            stream.forEach(cmd -> table[0] = Utils.runCommand(cmd, table[0]));
        }
        return this;
    }

    @Override
    public void close() throws Exception {

    }
}
