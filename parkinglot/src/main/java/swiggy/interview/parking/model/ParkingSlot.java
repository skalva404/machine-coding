package swiggy.interview.parking.model;

/**
 * ParkingSlot class can be further enhanced for size, price and etc.
 */
public class ParkingSlot {

    private Long id;
    private Car car;

    private ParkingSlot() {
    }

    public ParkingSlot(Long id) {
        this.id = id;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void unSetCar() {
        this.car = null;
    }

    public Long id() {
        return id;
    }

    public Car car() {
        return car;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        ParkingSlot thatSlot = (ParkingSlot) that;
        return id.equals(thatSlot.id) && car.equals(thatSlot.car);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + car.hashCode();
        return result;
    }
}
