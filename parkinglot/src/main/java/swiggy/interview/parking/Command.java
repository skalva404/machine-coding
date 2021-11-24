package swiggy.interview.parking;

import swiggy.interview.parking.db.ParkingSlotCarMapping;

/**
 * Any command interacts with ParkingLotService should implement Command interface,
 * All commands are steful, if you want to execute same command multiple times
 * createTable multiple instances of the same command. Executing same instance of command
 * multiple times doesn't change any state, it is idempotent.
 *
 * @param <T>
 */
public interface Command<T> {

    void execute() throws ParkingError;

    void setSlotCarMapping(ParkingSlotCarMapping slotCarMapping);

    String format();

    T result();
}
