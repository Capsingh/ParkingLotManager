import java.time.LocalDateTime;
import java.util.*;

public class ParkingLot implements ParkingOperations {
    private final Map<String, Spot> allSpots;        // spotId -> Spot
    private final Set<String> activeSpotIds;          // occupied spot IDs
    private final Map<String, String> carLocations;   // licensePlate -> spotId
    private final List<Transaction> transactionLog;

    public ParkingLot() {
        allSpots = new HashMap<>();
        activeSpotIds = new HashSet<>();
        carLocations = new HashMap<>();
        transactionLog = new ArrayList<>();
    }

    public void addSpot(Spot spot) {
        allSpots.put(spot.getSpotId(), spot);
    }

    @Override
    public void parkCar(Car car) throws LotFullException {
        Spot spot = findAvailableSpot(car);

        if (spot == null) {
            throw new LotFullException("No available spot for size: " + car.getSize());
        }

        spot.setOccupied(true);
        activeSpotIds.add(spot.getSpotId());
        carLocations.put(car.getLicensePlate(), spot.getSpotId());

        Transaction t = new Transaction(car.getLicensePlate(), spot.getSpotId(), LocalDateTime.now());
        transactionLog.add(t);

        System.out.println("Parked " + car.getLicensePlate() + " at spot " + spot.getSpotId());
    }

    @Override
    public void removeCar(String licensePlate) throws SpotNotFoundException {
        String spotId = carLocations.get(licensePlate);

        if (spotId == null) {
            throw new SpotNotFoundException("No parked car found with plate: " + licensePlate);
        }

        Spot spot = allSpots.get(spotId);
        spot.setOccupied(false);
        activeSpotIds.remove(spotId);
        carLocations.remove(licensePlate);

        // find the matching open transaction and close it out
        for (Transaction t : transactionLog) {
            if (t.getLicensePlate().equals(licensePlate) && t.getExitTime() == null) {
                double hoursParked = 1.0; // simplified — real calc would diff entryTime vs now
                double amount = hoursParked * spot.getPricePerHour();
                t.closeOut(LocalDateTime.now(), amount);
                System.out.println("Removed " + licensePlate + " from spot " + spotId + ". Charged $" + amount);
                break;
            }
        }
    }

    private Spot findAvailableSpot(Car car) {
        for (Spot spot : allSpots.values()) {
            if (!spot.isOccupied() && spot.canFit(car)) {
                return spot;
            }
        }
        return null;
    }

    @Override
    public boolean isFull() {
        return activeSpotIds.size() == allSpots.size();
    }

    @Override
    public void printStatus() {
        System.out.println("--- Parking Lot Status ---");
        System.out.println("Occupied: " + activeSpotIds.size() + " / " + allSpots.size());
        for (Spot spot : allSpots.values()) {
            System.out.println(spot.getSpotId() + ": " + (spot.isOccupied() ? "OCCUPIED" : "free"));
        }
    }

    @Override
    public List<Transaction> getTransactionLog() {
        return transactionLog;
    }
}