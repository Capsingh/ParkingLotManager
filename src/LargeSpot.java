public class LargeSpot extends Spot {

    public LargeSpot(String spotId) {
        super(spotId, 3.5);
    }

    @Override
    public boolean canFit(Car car) {
        return true; // large spot can hold anything
    }
}