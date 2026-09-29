public class HandicapSpot extends Spot {

    public HandicapSpot(String spotId) {
        super(spotId, 2.0);
    }

    @Override
    public boolean canFit(Car car) {
        return true; // size-wise fits anything; access rules would go here later
    }
}