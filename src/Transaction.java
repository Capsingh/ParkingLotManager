import java.time.LocalDateTime;

public class Transaction {
    private final String licensePlate;
    private final String spotId;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double amountCharged;

    public Transaction(String licensePlate, String spotId, LocalDateTime entryTime) {
        this.licensePlate = licensePlate;
        this.spotId = spotId;
        this.entryTime = entryTime;
    }

    public void closeOut(LocalDateTime exitTime, double amountCharged) {
        this.exitTime = exitTime;
        this.amountCharged = amountCharged;
    }

    public String getLicensePlate() { return licensePlate; }
    public String getSpotId() { return spotId; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public double getAmountCharged() { return amountCharged; }

    @Override
    public String toString() {
        return String.format("%s | Spot %s | In: %s | Out: %s | $%.2f",
                licensePlate, spotId, entryTime,
                exitTime == null ? "still parked" : exitTime, amountCharged);
    }
}