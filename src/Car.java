public class Car {
    private final String licensePlate;
    private final Size size;

    public Car(String licensePlate, Size size) {
        this.licensePlate = licensePlate;
        this.size = size;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public Size getSize() {
        return size;
    }
}