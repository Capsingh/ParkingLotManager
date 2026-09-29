public abstract class Spot {
    protected final String spotId;
    protected boolean isOccupied;
    protected final double pricePerHour;

    public Spot(String spotId, double pricePerHour) {
        this.spotId = spotId;
        this.pricePerHour = pricePerHour;
        this.isOccupied = false;
    }

    public abstract boolean canFit(Car car);

    public String getSpotId() {
        return spotId;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        this.isOccupied = occupied;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }
}