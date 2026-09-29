public class MotorcycleSpot extends Spot {

    public MotorcycleSpot(String spotId) {
        super(spotId, 1.0);
    }

    @Override
    public boolean canFit(Car car) {
        return car.getSize() == Size.MOTORCYCLE;
    }
}