package swiggy.interview.parking.commands;

import swiggy.interview.parking.ErrorConstants;
import swiggy.interview.parking.ParkingError;
import swiggy.interview.parking.model.ParkingSlot;

import java.util.List;

import static swiggy.interview.parking.commands.CommandConstants.LEAVE;

public class LeaveCommand extends AbstractCommand<ParkingSlot> {

    private Long slotId;
    private ParkingSlot parkingSlot;

    public LeaveCommand(List<String> args) throws ParkingError {
        if (null == args || 1 != args.size()) {
            throw new ParkingError(ErrorConstants.INVALID_ARGS.message());
        }
        try {
            slotId = Long.parseLong(args.get(0));
        } catch (Exception e) {
            throw new ParkingError(ErrorConstants.INVALID_ARGS.message() + args.get(0));
        }
    }

    @Override
    public void execute() throws ParkingError {
        if (null != parkingSlot) {
            return;
        }
        synchronized (parkingSlotEntity()) {
            this.parkingSlot = parkingSlotEntity().leave(slotId);
        }
    }

    @Override
    public String format() {
        if (null == this.parkingSlot) {
            return ErrorConstants.DATA_NOT_FOUND.message();
        }
        return String.format(LEAVE.message(), parkingSlot.id());
    }

    @Override
    public ParkingSlot result() {
        return parkingSlot;
    }
}
