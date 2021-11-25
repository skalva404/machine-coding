package kalva.mc.parking;

public class ParkingError extends Exception {

    public ParkingError(String message) {
        super(message);
    }

    public ParkingError(String message, Throwable throwable) {
        super(message, throwable);
    }
}
