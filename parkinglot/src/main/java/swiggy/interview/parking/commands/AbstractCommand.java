package swiggy.interview.parking.commands;

import swiggy.interview.parking.Command;
import swiggy.interview.parking.db.ParkingSlotCarMapping;

abstract class AbstractCommand<T> implements Command<T> {

    private ParkingSlotCarMapping slotCarMapping;

    AbstractCommand() {
    }

    public void setSlotCarMapping(ParkingSlotCarMapping slotCarMapping) {
        this.slotCarMapping = slotCarMapping;
    }

    ParkingSlotCarMapping parkingSlotEntity() {
        return this.slotCarMapping;
    }
}
