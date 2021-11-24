package swiggy.interview.parking.client;

import swiggy.interview.parking.Service;
import swiggy.interview.parking.Utils;
import swiggy.interview.parking.db.ParkingSlotCarMapping;

import java.io.Console;
import java.io.IOException;

public class InteractiveCommandsParser implements Service {

    boolean exit = false;

    @Override
    public InteractiveCommandsParser start() throws IOException {

        ParkingSlotCarMapping table = null;
        Console console = System.console();
        while (!exit) {
            System.out.println("Input:");
            String cmd = console.readLine();
            if ("exit".equalsIgnoreCase(cmd)) {
                exit = true;
                continue;
            }
            System.out.println("Output:");
            try {
                table = Utils.runCommand(cmd, table);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println();
        }
        return this;
    }

    @Override
    public void close() throws Exception {
        exit = false;
    }
}
