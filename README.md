# Parking Lot Manager

A console-based Java application simulating a parking lot: cars park, leave, get billed, and every transaction is logged. Built as a capstone project to combine OOP design, the Collections Framework, control flow, and exception handling into one working system.

## Project Structure

```
ParkingLotManager/
├── Size.java                  (enum)
├── Car.java
├── Spot.java                  (abstract class)
├── CompactSpot.java
├── LargeSpot.java
├── MotorcycleSpot.java
├── HandicapSpot.java
├── Transaction.java
├── ParkingOperations.java     (interface)
├── LotFullException.java
├── SpotNotFoundException.java
├── ParkingLot.java
└── Main.java
```

13 files total: 1 enum, 1 interface, 1 abstract class, 4 concrete `Spot` subclasses, 3 plain classes, 2 custom exceptions, 1 entry point.

## How the Pieces Wire Together

### 1. `Size` (enum)
The foundation. A fixed set of car sizes (`MOTORCYCLE`, `COMPACT`, `LARGE`) used by both `Car` and every `Spot` subclass to decide fit. Using an enum instead of a raw `String` means invalid sizes are caught at compile time (or via `Size.valueOf()` at input time) instead of silently causing bugs.

### 2. `Car`
A simple, immutable data holder — `licensePlate` and `size`, both `final`. Every other class either creates a `Car`, reads its fields, or passes it around.

### 3. `Spot` (abstract class) → `CompactSpot`, `LargeSpot`, `MotorcycleSpot`, `HandicapSpot`
`Spot` defines what every parking spot **has** (`spotId`, `isOccupied`, `pricePerHour` — shared state) and one thing every spot must decide for itself: `abstract boolean canFit(Car car)`.

Each subclass overrides `canFit()` differently:
- `CompactSpot` → only fits `Size.COMPACT`
- `MotorcycleSpot` → only fits `Size.MOTORCYCLE`
- `LargeSpot` / `HandicapSpot` → fit anything

`Spot` itself can never be instantiated directly (`new Spot(...)` won't compile) — only its concrete subclasses can. This is the project's core polymorphism: `ParkingLot` calls `spot.canFit(car)` without ever checking *which* subclass it's dealing with, and Java runs the correct overridden version automatically.

### 4. `Transaction`
A receipt: which car, which spot, when it entered, when it left, how much it was charged. Created the moment a car parks (`exitTime` and `amountCharged` start empty), completed later via `closeOut()` when the car leaves. Overrides `toString()` so the transaction log prints cleanly.

### 5. `ParkingOperations` (interface)
The contract for anything that wants to *be* a parking lot: `parkCar`, `removeCar`, `isFull`, `printStatus`, `getTransactionLog`. No implementation, no state — just method signatures. `ParkingLot` implements this. The benefit: `Main` could work against any `ParkingOperations` implementation (a mock for testing, a database-backed version, etc.) without changing a line, as long as the contract is honored.

### 6. `LotFullException`, `SpotNotFoundException` (custom checked exceptions)
Both `extend Exception` (checked, not unchecked) so the compiler *forces* any caller of `parkCar()`/`removeCar()` to handle failure explicitly — a full lot or a bogus license plate can never be silently ignored.

### 7. `ParkingLot` (implements `ParkingOperations`)
Where everything meets. Holds three collections, each chosen for what it's actually good at:

| Field | Type | Purpose |
|---|---|---|
| `allSpots` | `Map<String, Spot>` (`HashMap`) | instant lookup: spot ID → `Spot` object |
| `activeSpotIds` | `Set<String>` (`HashSet`) | fast "is this spot occupied?" check, no duplicates, order irrelevant |
| `carLocations` | `Map<String, String>` (`HashMap`) | reverse lookup: license plate → spot ID (needed so `removeCar` can find a car by plate alone) |
| `transactionLog` | `List<Transaction>` (`ArrayList`) | full append-only history, read in order |

**`parkCar(Car car)`**: scans `allSpots` for the first free spot where `canFit(car)` is true, marks it occupied, records the car's location, opens a new `Transaction`. Throws `LotFullException` if nothing fits.

**`removeCar(String licensePlate)`**: looks up the car's spot via `carLocations`, frees it, finds the matching open `Transaction` and closes it out with a charge. Throws `SpotNotFoundException` if the plate isn't currently parked.

*Known simplification:* billing currently uses a flat 1-hour charge rather than the real elapsed time between park and removal — a placeholder left for a future pass (would use `Duration.between(entryTime, now)`).

### 8. `Main`
The entry point and only place the user directly interacts with. Sets up the lot's spots, then runs a `while` loop showing a menu, reading input via `Scanner`, and dispatching with a `switch` statement. Wraps every call to `parkCar()`/`removeCar()` in `try/catch` — this is deliberate: `ParkingLot` *throws* problems, `Main` *catches and displays* them. Also validates size input (`Size.valueOf()`) so a typo can't crash the program.

## Design Decisions Worth Knowing

- **`abstract class` for `Spot`, `interface` for `ParkingOperations`** — not interchangeable choices. `Spot` subtypes share real state (`spotId`, `pricePerHour`), so abstract class fits. `ParkingOperations` is a pure contract with no shared state, so interface fits.
- **Checked exceptions**, not unchecked — the compiler forces every caller to handle a full lot or a missing car, rather than letting either fail silently.
- **`carLocations` map exists purely to make `removeCar` possible** by license plate alone — without it there'd be no way to find which spot a given car occupies.
- **Transaction lookup in `removeCar` is O(n)** (loops the full log to find the open transaction), not O(1). A `Map<String, Transaction>` of active transactions would be faster, but was left out to keep `ParkingLot`'s state to exactly three collections as scoped. A documented, intentional tradeoff — not an oversight.

## Running It

Compile and run from the project directory:

```
javac *.java
java Main
```
