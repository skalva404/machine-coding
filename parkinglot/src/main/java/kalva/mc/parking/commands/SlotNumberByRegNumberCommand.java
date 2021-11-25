package kalva.mc.parking.commands;

import kalva.mc.parking.ErrorConstants;
import kalva.mc.parking.ParkingError;
import kalva.mc.parking.model.ParkingSlot;

import java.util.List;

public class SlotNumberByRegNumberCommand extends AbstractCommand<ParkingSlot> {

    private String regNum;
    private ParkingSlot slot;

    public SlotNumberByRegNumberCommand(List<String> args) throws ParkingError {
        if (null == args || 1 != args.size()) {
            throw new ParkingError(ErrorConstants.INVALID_ARGS.message());
        }
        regNum = args.get(0);
    }

    @Override
    public void execute() throws ParkingError {
        if (null != slot) {
            return;
        }
        synchronized (parkingSlotEntity()) {
            slot = parkingSlotEntity().regNumToSlotIndex().get(regNum);
        }
    }

    @Override
    public String format() {
        if (null == slot) {
            return ErrorConstants.DATA_NOT_FOUND.message();
        }
        return slot.id().toString();
    }

    @Override
    public ParkingSlot result() {
        return slot;
    }
}
