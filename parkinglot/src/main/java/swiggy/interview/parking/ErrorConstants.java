package swiggy.interview.parking;

public enum ErrorConstants implements Messages {

    ZERO_ARG_ERROR("Command not required any arguments..."),
    EMPTY_LOT_SIZE("ParkingLot size can not be empty ..."),
    COMMAND_CREATE_ERROR("Error creating command"),
    INVALID_ARGS("Invalid arguments passed "),
    SLOTS_FULL("Sorry, parking lot is full"),
    COMMAND_ERROR("Command Error"),
    FNF("File not found "),
    DATA_NOT_FOUND("Not found");

    private String message;

    ErrorConstants(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
