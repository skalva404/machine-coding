package swiggy.interview.parking;

import swiggy.interview.parking.commands.CreateCommand;
import swiggy.interview.parking.db.ParkingSlotCarMapping;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static <T> T newInstance(Class<T> theClass, List<String> args)
            throws ParkingError {
        T result;
        try {
            Constructor<T> constructor = theClass.getDeclaredConstructor(List.class);
            if (constructor == null) {
                throw new ParkingError(ErrorConstants.COMMAND_CREATE_ERROR.message() + theClass);
            }
            if (null == args) {
                args = new ArrayList<>();
            }
            result = constructor.newInstance(args);
        } catch (Exception e) {
            throw new ParkingError(ErrorConstants.COMMAND_CREATE_ERROR.message() + theClass, e);
        }
        return result;
    }

    public static ParkingSlotCarMapping runCommand(String cmd, ParkingSlotCarMapping table) {
        String[] args = cmd.split(" ");
        String[] params = new String[args.length - 1];
        System.arraycopy(args, 1, params, 0, args.length - 1);
        try {
            CommandType commandType = CommandType.valueOf(args[0]);
            Command command = (Command) Utils.newInstance(commandType.getImplementation(),
                    Arrays.asList(params));
            if (!commandType.equals(CommandType.create_parking_lot)) {
                command.setSlotCarMapping(table);
            }
            command.execute();
            if (commandType.equals(CommandType.create_parking_lot)) {
                table = ((CreateCommand) command).result().parkingSlotTable();
            }
            System.out.println(command.format());
        } catch (ParkingError parkingError) {
            System.out.println(parkingError.getMessage());
        }
        return table;
    }
}
