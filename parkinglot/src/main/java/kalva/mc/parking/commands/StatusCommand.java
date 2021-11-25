package kalva.mc.parking.commands;

import kalva.mc.parking.ErrorConstants;
import kalva.mc.parking.ParkingError;
import kalva.mc.parking.model.ParkingSlot;

import java.util.List;

public class StatusCommand extends AbstractCommand<List<ParkingSlot>> {

    private List<ParkingSlot> parkingSlotList;

    public StatusCommand(List<String> args) throws ParkingError {
        if (null == args || 0 != args.size()) {
            throw new ParkingError(ErrorConstants.ZERO_ARG_ERROR.message());
        }
    }

    @Override
    public void execute() throws ParkingError {
        if (null != parkingSlotList) {
            return;
        }
        synchronized (parkingSlotEntity()) {
            parkingSlotList = parkingSlotEntity().occupiedSlots();
        }
    }

    @Override
    public String format() {
        if (null == this.parkingSlotList) {
            return ErrorConstants.DATA_NOT_FOUND.message();
        }
        StringBuilder builder = new StringBuilder();
        builder.append(CommandConstants.STATUS_HEADER.message());
        parkingSlotList.sort((thisObj, thatObj) -> thisObj.id().compareTo(thatObj.id()));
        for (ParkingSlot slot : parkingSlotList) {
            if (null == slot.car()) {
                continue;
            }
            builder
                    .append("\n")
                    .append(String.format(CommandConstants.STATUS_BODY_FORMAT.message(),
                            slot.id(), slot.car().regNumber(), slot.car().color()));
        }
        return builder.toString();
    }

    @Override
    public List<ParkingSlot> result() {
        return parkingSlotList;
    }
}
