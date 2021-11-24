package swiggy.interview.parking.commands;

import swiggy.interview.parking.ErrorConstants;
import swiggy.interview.parking.ParkingError;
import swiggy.interview.parking.service.ParkingLotService;

import java.util.List;

public class CreateCommand extends AbstractCommand<ParkingLotService> {

    private Long size;
    private ParkingLotService plService;

    public CreateCommand(List<String> args) throws ParkingError {
        if (null == args || 1 != args.size()) {
            throw new ParkingError(ErrorConstants.INVALID_ARGS.message());
        }
        try {
            size = Long.parseLong(args.get(0));
        } catch (Exception e) {
            throw new ParkingError(ErrorConstants.INVALID_ARGS.message() + args.get(0));
        }
    }

    @Override
    public void execute() throws ParkingError {
        if (null != plService) {
            return;
        }
        plService = new ParkingLotService(size).start();
    }

    @Override
    public String format() {
        if (null == plService) {
            return ErrorConstants.DATA_NOT_FOUND.message();
        }
        return String.format(CommandConstants.CREATE.message(),
                plService.parkingSlotTable().totalSlots().size());
    }

    @Override
    public ParkingLotService result() {
        return plService;
    }
}
