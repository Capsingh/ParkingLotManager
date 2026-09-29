import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ParkingLot lot = new ParkingLot();
        setupSpots(lot);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handlePark(lot, scanner);
                    break;
                case "2":
                    handleRemove(lot, scanner);
                    break;
                case "3":
                    lot.printStatus();
                    break;
                case "4":
                    printTransactionLog(lot);
                    break;
                case "5":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-5.");
            }
        }

        scanner.close();
    }

    private static void setupSpots(ParkingLot lot) {
        lot.addSpot(new CompactSpot("C1"));
        lot.addSpot(new CompactSpot("C2"));
        lot.addSpot(new LargeSpot("L1"));
        lot.addSpot(new MotorcycleSpot("M1"));
        lot.addSpot(new HandicapSpot("H1"));
    }

    private static void printMenu() {
        System.out.println("\n=== Parking Lot Manager ===");
        System.out.println("1. Park a car");
        System.out.println("2. Remove a car");
        System.out.println("3. View lot status");
        System.out.println("4. View transaction log");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }

    private static void handlePark(ParkingLot lot, Scanner scanner) {
        System.out.print("Enter license plate: ");
        String plate = scanner.nextLine().trim();

        if (plate.isEmpty()) {
            System.out.println("License plate cannot be empty.");
            return;
        }

        Size size = readSize(scanner);
        if (size == null) return; // invalid input already reported

        Car car = new Car(plate, size);

        try {
            lot.parkCar(car);
        } catch (LotFullException e) {
            System.out.println("Could not park: " + e.getMessage());
        }
    }

    private static void handleRemove(ParkingLot lot, Scanner scanner) {
        System.out.print("Enter license plate to remove: ");
        String plate = scanner.nextLine().trim();

        try {
            lot.removeCar(plate);
        } catch (SpotNotFoundException e) {
            System.out.println("Could not remove: " + e.getMessage());
        }
    }

    private static Size readSize(Scanner scanner) {
        System.out.print("Enter size (MOTORCYCLE / COMPACT / LARGE): ");
        String input = scanner.nextLine().trim().toUpperCase();

        try {
            return Size.valueOf(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid size entered.");
            return null;
        }
    }

    private static void printTransactionLog(ParkingLot lot) {
        System.out.println("--- Transaction Log ---");
        for (Transaction t : lot.getTransactionLog()) {
            System.out.println(t);
        }
    }
}