import java.util.Scanner;

public class Main {

public static void main(String[] args) {

Scanner input = new Scanner(System.in);
RideSharingSystem system = new RideSharingSystem();

// Load CSV files before displaying the menu.
system.loadRidersFromCSV("riders_100.csv");
system.loadDriversFromCSV("drivers_30.csv");
system.loadRidesFromCSV("rides_40.csv");

int choice;

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
choice = Integer.parseInt(input.nextLine());

switch (choice) {

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
String[] pickup = pickupText.split("[/ :]");

IDateTime pickupTime = new DateTime(
Integer.parseInt(pickup[2]),
Integer.parseInt(pickup[0]),
Integer.parseInt(pickup[1]),
Integer.parseInt(pickup[3]),
Integer.parseInt(pickup[4])
);

// Gets the dropoff date and time.
System.out.print("Enter dropoff date and time (MM/DD/YYYY HH:MM): ");
String dropoffText = input.nextLine();
String[] dropoff = dropoffText.split("[/ :]");

IDateTime dropoffTime = new DateTime(
Integer.parseInt(dropoff[2]),
Integer.parseInt(dropoff[0]),
Integer.parseInt(dropoff[1]),
Integer.parseInt(dropoff[3]),
Integer.parseInt(dropoff[4])
);

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

} while (choice != 0);

input.close();
}
}