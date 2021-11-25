package kalva.mc.parking.commands;

import kalva.mc.parking.Command;
import kalva.mc.parking.db.ParkingSlotCarMapping;

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
