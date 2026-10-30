import java.util.Scanner;

public class Main {

public static void main(String[] args) {

Scanner input = new Scanner(System.in);
RideSharingSystem system = new RideSharingSystem();

// Load CSV files before displaying the menu.
system.loadRidersFromCSV("riders_100.csv");
system.loadDriversFromCSV("drivers_30.csv");
system.loadRidesFromCSV("rides_40.csv");

int choice = -1;

do {

System.out.println("\n===== Ride Sharing System =====");
System.out.println("1. List all riders");
System.out.println("2. Search for riders by home city");
System.out.println("3. Search for riders by name");
System.out.println("4. Add a rider");
System.out.println("5. List all drivers");
System.out.println("6. Search for drivers by vehicle type");
System.out.println("7. Add a driver");
System.out.println("8. List all rides alphabetically by pickup location");
System.out.println("9. Search for rides by pickup location");
System.out.println("10. Search for rides by rider name");
System.out.println("11. List the participants of a shared ride by pickup location");
System.out.println("12. Add a ride");
System.out.println("0. Exit");

System.out.print("Enter your choice: ");

// Catches wrong input (e.g. letters instead of a number) so the program does not crash.
try {
choice = Integer.parseInt(input.nextLine().trim());

switch (choice) {
case 1: {
// Gets all riders sorted by ID.
LinkedList<IRider> allRiders = system.getAllRiders();

// Checks if there are no riders.
if (allRiders.empty()) {
System.out.println("No riders found.");
} else {

// Starts from the first rider.
allRiders.findFirst();

while (true) {

// Prints the current rider.
System.out.println(allRiders.retrieve());

// Stops after printing the last rider.
if (allRiders.last()) {
break;
}

// Moves to the next rider.
allRiders.findNext();
}
}

break;
}

case 2: {
// Asks the user for the home city.
System.out.print("Enter home city: ");
String homeCity = input.nextLine().trim();

// Searches for riders from this home city.
LinkedList<IRider> foundRiders = system.searchRidersByHomeCity(homeCity);

// Checks if no riders were found.
if (foundRiders.empty()) {
System.out.println("No riders found.");
} else {

// Starts from the first matching rider.
foundRiders.findFirst();

while (true) {

// Prints the current rider.
System.out.println(foundRiders.retrieve());

// Stops after the last rider.
if (foundRiders.last()) {
break;
}

// Moves to the next rider.
foundRiders.findNext();
}
}

break;
}

case 3: {
// Asks the user for the rider's full name.
System.out.print("Enter rider full name: ");
String riderName = input.nextLine().trim();

// Searches for riders with exactly this full name.
LinkedList<IRider> foundRiders = system.searchRidersByName(riderName);

// Checks if no riders were found.
if (foundRiders.empty()) {
System.out.println("No riders found.");
} else {

// Starts from the first matching rider.
foundRiders.findFirst();

while (true) {

// Prints the current rider.
System.out.println(foundRiders.retrieve());

// Stops after the last rider.
if (foundRiders.last()) {
break;
}

// Moves to the next rider.
foundRiders.findNext();
}
}

break;
}

case 4: {
// Gets the rider's information from the user.
System.out.print("Enter rider ID: ");
int riderId = Integer.parseInt(input.nextLine().trim());

System.out.print("Enter rider full name: ");
String riderName = input.nextLine().trim();

System.out.print("Enter rider email: ");
String riderEmail = input.nextLine().trim();

System.out.print("Enter rider phone (10 digits): ");
String riderPhone = input.nextLine().trim();

System.out.print("Enter home city: ");
String homeCity = input.nextLine().trim();

try {
// Creates the rider (throws an exception if the phone number is invalid).
IRider rider = new Rider(riderId, riderName, riderEmail, riderPhone, homeCity);

// Adds the rider (fails if the ID or email already exists).
if (system.addRider(rider)) {
System.out.println("Rider added successfully.");
} else {
System.out.println("Failed to add rider: ID or email already exists.");
}

} catch (IllegalArgumentException e) {
System.out.println("Failed to add rider: " + e.getMessage());
}

break;
}

    case 5: {
    // List all drivers
    LinkedList<IDriver> allDrivers = system.getAllDrivers();

    if (allDrivers.empty()) {
        System.out.println("No drivers found.");
    } else {
        allDrivers.findFirst();

        while (true) {
            System.out.println(allDrivers.retrieve());

            if (allDrivers.last()) {
                break;
            }

            allDrivers.findNext();
        }
    }

    break;
}

case 6: {
    // Search drivers by vehicle type
    System.out.println("Available vehicle types:");
    System.out.println("SEDAN");
    System.out.println("LUXURY_SEDAN");
    System.out.println("SUV");
    System.out.println("VAN");

    System.out.print("Enter vehicle type: ");
    String typeText = input.nextLine().trim().toUpperCase();

    try {
        VehicleType type = VehicleType.valueOf(typeText);

        LinkedList<IDriver> foundDrivers =
                system.searchDriversByVehicleType(type);

        if (foundDrivers.empty()) {
            System.out.println("No drivers found.");
        } else {
            foundDrivers.findFirst();

            while (true) {
                System.out.println(foundDrivers.retrieve());

                if (foundDrivers.last()) {
                    break;
                }

                foundDrivers.findNext();
            }
        }

    } catch (IllegalArgumentException e) {
        System.out.println("Invalid vehicle type.");
    }

    break;
}

case 7: {
    // Add a driver
    System.out.print("Enter driver ID: ");
    int driverId = Integer.parseInt(input.nextLine());

    System.out.print("Enter driver name: ");
    String driverName = input.nextLine();

    System.out.print("Enter driver phone: ");
    String driverPhone = input.nextLine();

    System.out.print("Enter vehicle plate: ");
    String vehiclePlate = input.nextLine();

    System.out.print(
            "Enter vehicle type (SEDAN, LUXURY_SEDAN, SUV, VAN): ");
    String typeText = input.nextLine().trim().toUpperCase();

    try {
        VehicleType vehicleType = VehicleType.valueOf(typeText);

        IDriver driver = new Driver(
                driverId,
                driverName,
                driverPhone,
                vehiclePlate,
                vehicleType
        );

        boolean added = system.addDriver(driver);

        if (added) {
            System.out.println("Driver added successfully.");
        } else {
            System.out.println("Failed to add driver: ID or plate already exists.");
        }

    } catch (IllegalArgumentException e) {
        // Shows the real reason: invalid vehicle type, phone number, or plate.
        System.out.println("Failed to add driver: " + e.getMessage());
    }

    break;
}

case 8: {
// Gets all rides sorted alphabetically by pickup location.
LinkedList<IRide> allRides = system.getAllRidesAlphabetically();

// Checks if there are no rides.
if (allRides.empty()) {
System.out.println("No rides found.");
} else {

// Starts from the first ride.
allRides.findFirst();

while (true) {

// Prints the current ride.
System.out.println(allRides.retrieve());

// Stops after printing the last ride.
if (allRides.last()) {
break;
}

// Moves to the next ride.
allRides.findNext();
}
}

break;
}

case 9: {
// Asks the user for the pickup location.
System.out.print("Enter pickup location: ");
String pickupLocation = input.nextLine();

// Searches for rides with the given pickup location.
LinkedList<IRide> foundRides = system.searchRidesByPickupLocation(pickupLocation);

// Checks if no matching rides were found.
if (foundRides.empty()) {
System.out.println("No rides found.");
} else {

// Starts from the first matching ride.
foundRides.findFirst();

while (true) {

// Prints the current ride.
System.out.println(foundRides.retrieve());

// Stops after the last ride.
if (foundRides.last()) {
break;
}

// Moves to the next ride.
foundRides.findNext();
}
}

break;
}

case 10: {
// Asks the user for the rider's full name.
System.out.print("Enter rider full name: ");
String riderName = input.nextLine();

// Searches for rides by rider name.
LinkedList<IRide> foundRides = system.searchRidesByRiderName(riderName);

// Checks if no rides were found.
if (foundRides.empty()) {
System.out.println("No rides found.");
} else {

// Starts from the first ride.
foundRides.findFirst();

while (true) {

// Prints the current ride.
System.out.println(foundRides.retrieve());

// Stops after the last ride.
if (foundRides.last()) {
break;
}

// Moves to the next ride.
foundRides.findNext();
}
}

break;
}

case 11: {
// Asks the user for the pickup location.
System.out.print("Enter pickup location: ");
String pickupLocation = input.nextLine();

// Gets the participants of the shared ride.
LinkedList<IRider> participants = system.getSharedRideParticipants(pickupLocation);

// Checks if no participants were found.
if (participants.empty()) {
System.out.println("No participants found.");
} else {

// Starts from the first participant.
participants.findFirst();

while (true) {

// Prints the current participant.
System.out.println(participants.retrieve());

// Stops after the last participant.
if (participants.last()) {
break;
}

// Moves to the next participant.
participants.findNext();
}
}

break;
}

case 12: {
// Asks the user to choose the ride type.
System.out.println("1. Private Ride");
System.out.println("2. Shared Ride");
System.out.print("Enter ride type: ");
int rideType = Integer.parseInt(input.nextLine());

// Checks if the ride type is valid.
if (rideType != 1 && rideType != 2) {
System.out.println("Invalid ride type.");
break;
}

// Gets the pickup location.
System.out.print("Enter pickup location: ");
String pickupLocation = input.nextLine();

// Gets the dropoff location.
System.out.print("Enter dropoff location: ");
String dropoffLocation = input.nextLine();

// Gets the pickup date and time.
System.out.print("Enter pickup date and time (MM/DD/YYYY HH:MM): ");
String pickupText = input.nextLine();

// Gets the dropoff date and time.
System.out.print("Enter dropoff date and time (MM/DD/YYYY HH:MM): ");
String dropoffText = input.nextLine();

IDateTime pickupTime;
IDateTime dropoffTime;

// Stops if a date is not in the MM/DD/YYYY HH:MM format.
try {
pickupTime = new DateTime(pickupText);
dropoffTime = new DateTime(dropoffText);
} catch (Exception e) {
System.out.println("Invalid date. Use the format MM/DD/YYYY HH:MM (e.g. 10/28/2026 08:45).");
break;
}

// Gets the driver ID.
System.out.print("Enter driver ID: ");
int driverId = Integer.parseInt(input.nextLine());

boolean added = false;

if (rideType == 1) {

// Gets the rider ID for a private ride.
System.out.print("Enter rider ID: ");
int riderId = Integer.parseInt(input.nextLine());

// Schedules a private ride.
added = system.schedulePrivateRide(
pickupLocation,
pickupTime,
dropoffTime,
dropoffLocation,
riderId,
driverId
);

} else {

// Gets the number of participants.
System.out.print("Enter number of riders: ");
int count = Integer.parseInt(input.nextLine());

if (count < 2) {
System.out.println("A shared ride requires at least two riders.");
break;
}

int[] riderIds = new int[count];

// Gets the ID of each participant.
for (int i = 0; i < count; i++) {
System.out.print("Enter rider ID " + (i + 1) + ": ");
riderIds[i] = Integer.parseInt(input.nextLine());
}

// Schedules a shared ride.
added = system.scheduleSharedRide(
pickupLocation,
pickupTime,
dropoffTime,
dropoffLocation,
riderIds,
driverId
);
}

// Displays the result.
if (added) {
System.out.println("Ride added successfully.");
} else {
System.out.println("Failed to add ride.");
}

break;
}

case 0:
System.out.println("Goodbye!");
break;

default:
System.out.println("Invalid choice.");
}

} catch (NumberFormatException e) {
// A number was expected but the user typed something else.
System.out.println("Invalid input: please enter a number.");
} catch (Exception e) {
// Any other wrong input (e.g. a date not in the MM/DD/YYYY HH:MM format).
System.out.println("Invalid input: please check the format and try again.");
}

} while (choice != 0);

input.close();
}
}