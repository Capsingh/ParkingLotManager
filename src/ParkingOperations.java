import java.util.List;

public interface ParkingOperations {
    void parkCar(Car car) throws LotFullException;
    void removeCar(String licensePlate) throws SpotNotFoundException;
    boolean isFull();
    void printStatus();
    List<Transaction> getTransactionLog();
}