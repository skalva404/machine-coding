package swiggy.interview.parking.model;

public class Car {

    private String regNumber;
    private String color;

    private Car() {
    }

    public Car(String regNumber, String color) {
        this.regNumber = regNumber;
        this.color = color;
    }

    public String regNumber() {
        return regNumber;
    }

    public String color() {
        return color;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        Car thatCar = (Car) that;
        return regNumber.equals(thatCar.regNumber) && color.equals(thatCar.color);
    }

    @Override
    public int hashCode() {
        int result = regNumber.hashCode();
        result = 31 * result + color.hashCode();
        return result;
    }
}
