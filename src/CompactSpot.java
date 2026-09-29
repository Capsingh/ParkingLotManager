public class CompactSpot extends Spot {

    public CompactSpot(String spotId) {
        super(spotId, 2.0);
    }

    @Override
    public boolean canFit(Car car) {
        return car.getSize() == Size.COMPACT;
    }
}