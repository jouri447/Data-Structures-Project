import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RideSharingSystem implements IRideSharingSystem {

    private RiderList riders;
    private DriverList drivers;
    private RideList rides;

    // Creates the lists needed to store riders, drivers, and rides.
    public RideSharingSystem() {
        riders = new RiderList();
        drivers = new DriverList();
        rides = new RideList();
    }

    // Loads riders from a CSV file (riderId,name,email,phoneNumber,homeCity)
    public boolean loadRidersFromCSV(String ridersFilePath) {
        try {
            Scanner file = new Scanner(new File(ridersFilePath));
            boolean allLoaded = true;

            // Reads the file line by line.
            while (file.hasNextLine()) {
                String line = file.nextLine().trim();

                // Skips empty lines.
                if (line.isEmpty()) {
                    continue;
                }

                String[] f = line.split(",");

                try {
                    // The Rider constructor validates the phone number.
                    IRider rider = new Rider(Integer.parseInt(f[0].trim()), f[1].trim(), f[2].trim(), f[3].trim(), f[4].trim());

                    // Marks the loading as failed if the rider was not added.
                    if (!addRider(rider)) {
                        allLoaded = false;
                    }
                }
                catch (Exception e) {
                    // Continues reading if a line has invalid data.
                    allLoaded = false;
                }
            }

            file.close();

            // Returns true if all riders were loaded successfully.
            return allLoaded;
        }
        catch (FileNotFoundException e) {
            // Returns false if the file cannot be found.
            return false;
        }
    }

    // Adds a rider; fails if the rider is invalid or the ID/email already exists
    public boolean addRider(IRider rider) {
        // Rejects a missing rider or missing name, email, or home city.
        if (rider == null || isBlank(rider.getName()) || isBlank(rider.getEmail()) || isBlank(rider.getHomeCity())) {
            return false;
        }

        // RiderList rejects duplicate IDs and emails.
        return riders.add(rider);
    }

    // Returns true if the text is null or only spaces
    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    // Searches for a rider by ID
    public IRider searchRiderById(int riderId) {
        return riders.findById(riderId);
    }

    // Searches for a rider by email
    public IRider searchRiderByEmail(String email) {
        return riders.findByEmail(email);
    }

    // Searches for all riders whose full name exactly matches the given name
    public LinkedList<IRider> searchRidersByName(String fullName) {
        return riders.findByName(fullName);
    }

    // Searches for all riders from the given home city
    public LinkedList<IRider> searchRidersByHomeCity(String homeCity) {
        return riders.findByHomeCity(homeCity);
    }

    // Returns all riders sorted by ID
    public LinkedList<IRider> getAllRiders() {
        return riders.getAll();
    }

// Loads rides from a CSV file and schedules each ride
public boolean loadRidesFromCSV(String ridesFilePath) {
try {
Scanner file = new Scanner(new File(ridesFilePath));
boolean allLoaded = true;

// Reads the file line by line.
while (file.hasNextLine()) {
String[] f = file.nextLine().split(",");

try {
// Splits the pickup date and time into separate values.
String[] pickup = f[2].split("[/ :]");

// Creates the pickup time using the DateTime constructor.
IDateTime pickupTime = new DateTime(Integer.parseInt(pickup[2]), Integer.parseInt(pickup[0]), Integer.parseInt(pickup[1]), Integer.parseInt(pickup[3]), Integer.parseInt(pickup[4]));

// Splits the dropoff date and time into separate values.
String[] dropoff = f[3].split("[/ :]");

// Creates the dropoff time using the DateTime constructor.
IDateTime dropoffTime = new DateTime(Integer.parseInt(dropoff[2]), Integer.parseInt(dropoff[0]), Integer.parseInt(dropoff[1]), Integer.parseInt(dropoff[3]), Integer.parseInt(dropoff[4]));

// Gets the driver ID and separates the rider IDs.
int driverId = Integer.parseInt(f[5]);
String[] ids = f[6].split(";");

boolean scheduled = false;

// A private ride has only one rider.
if (f[0].equals("PRIVATE")) {
int riderId = Integer.parseInt(ids[0]);

// Checks and schedules the private ride.
scheduled = schedulePrivateRide(f[1], pickupTime, dropoffTime, f[4], riderId, driverId);
}
else if (f[0].equals("SHARED")) {

// Converts the rider IDs from String to int.
int[] riderIds = new int[ids.length];

for (int i = 0; i < ids.length; i++) {
riderIds[i] = Integer.parseInt(ids[i]);
}

// Checks and schedules the shared ride.
scheduled = scheduleSharedRide(f[1], pickupTime, dropoffTime, f[4], riderIds, driverId);
}

// Marks the loading as failed if a ride was not scheduled.
if (!scheduled) {
allLoaded = false;
}
}
catch (Exception e) {
// Continues reading if a line has invalid data.
allLoaded = false;
}
}

file.close();

// Returns true if all rides were loaded successfully.
return allLoaded;
}
catch (FileNotFoundException e) {
// Returns false if the file cannot be found.
return false;
}
}

// Removes a rider and updates all rides involving them
public boolean removeRider(int riderId) {

// Checks if the rider exists.
IRider rider = riders.findById(riderId);

if (rider == null) {
return false;
}

// Gets all rides to check which ones involve this rider.
LinkedList<IRide> allRides = rides.getAllAlphabetically();

if (!allRides.empty()) {
allRides.findFirst();

while (true) {
IRide ride = allRides.retrieve();

// Checks if the rider is part of this ride.
if (ride.hasRider(riderId)) {

if (ride instanceof IPrivateRide) {

// Removes the private ride completely.
rides.removeRideById(ride.getRideId());
}
else if (ride instanceof ISharedRide) {
ISharedRide sharedRide = (ISharedRide) ride;

// Removes the rider from the shared ride.
sharedRide.removeParticipantById(riderId);

// Removes the shared ride if no participants remain.
if (sharedRide.isEmpty()) {
rides.removeRideById(ride.getRideId());
}
}
}

// Stops after checking the last ride.
if (allRides.last()) {
break;
}

allRides.findNext();
}
}

// Removes the rider from the rider list.
return riders.removeById(riderId);
}

// Removes a driver and all rides assigned to them
public boolean removeDriver(int driverId) {

// Checks if the driver exists
IDriver driver = drivers.findById(driverId);

if (driver == null) {
return false;
}

// Gets all rides to check which ones belong to this driver
LinkedList<IRide> allRides = rides.getAllAlphabetically();

if (!allRides.empty()) {
allRides.findFirst();

while (true) {
IRide ride = allRides.retrieve();

// Checks if the ride belongs to this driver
if (ride.getDriver().getId() == driverId) {

// Removes the ride from the ride list
rides.removeRideById(ride.getRideId());
}

// Stops after checking the last ride
if (allRides.last()) {
break;
}

allRides.findNext();
}
}

// Removes the driver from the driver list
return drivers.removeById(driverId);
}

// Searches for all rides starting from a given pickup location
public LinkedList<IRide> searchRidesByPickupLocation(String pickupLocation) {

// Uses RideList to find rides with the matching pickup location
return rides.findByPickupLocation(pickupLocation);
}

// Searches for all rides involving a rider with the given name
public LinkedList<IRide> searchRidesByRiderName(String riderName) {
// Uses RideList to find all rides involving this rider
return rides.findByRiderName(riderName);
}

// Returns participants from all shared rides without duplicates
public LinkedList<IRider> getSharedRideParticipants(String pickupLocation) {
// Creates an empty list to store the participants
LinkedList<IRider> result = new LinkedList<IRider>();

// Finds all rides starting from the given location
LinkedList<IRide> found = rides.findByPickupLocation(pickupLocation);

// Returns an empty list if no rides were found
if (found.empty()) {
return result;
}

// Goes through all matching rides
found.findFirst();
while (true) {
IRide ride = found.retrieve();

// Checks if the ride is shared
if (ride instanceof ISharedRide) {
ISharedRide sharedRide = (ISharedRide) ride;

// Gets the participants of the shared ride
LinkedList<IRider> participants = sharedRide.getParticipants();

if (!participants.empty()) {
participants.findFirst();
while (true) {
IRider rider = participants.retrieve();
boolean exists = false;

// Checks if the rider is already in the result list
if (!result.empty()) {
result.findFirst();
while (true) {
if (result.retrieve().getId() == rider.getId()) {
exists = true;
break;
}
if (result.last()) {
break;
}
result.findNext();
}
}

// Adds the rider only if not already included
if (!exists) {
result.insert(rider);
}

// Moves to the next participant
if (participants.last()) {
break;
}
participants.findNext();
}
}
}

// Moves to the next ride
if (found.last()) {
break;
}
found.findNext();
}

// Returns all participants without duplicates
return result;
}

// Returns all rides sorted alphabetically by pickup location
public LinkedList<IRide> getAllRidesAlphabetically() {
// Gets the sorted rides from RideList
return rides.getAllAlphabetically();
}
    
    
    
    

}