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
// Creates the pickup and dropoff times from the text "MM/DD/YYYY HH:MM".
IDateTime pickupTime = new DateTime(f[2]);
IDateTime dropoffTime = new DateTime(f[3]);

// Gets the driver ID and separates the rider IDs.
int driverId = Integer.parseInt(f[5].trim());
String[] ids = f[6].trim().split(";");

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

// Removes the private ride completely (also from the driver's history).
deleteRide(ride);
}
else if (ride instanceof ISharedRide) {
ISharedRide sharedRide = (ISharedRide) ride;

// Removes the rider from the shared ride.
sharedRide.removeParticipantById(riderId);

// Removes the shared ride if no participants remain.
if (sharedRide.isEmpty()) {
deleteRide(ride);
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

// Removes the ride from the ride list and from the riders' histories
deleteRide(ride);
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

    // Loads drivers from a CSV file (driverId,name,phoneNumber,vehiclePlate,vehicleType)
    public boolean loadDriversFromCSV(String driversFilePath) {
        try {
            Scanner file = new Scanner(new File(driversFilePath));
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
                    // Converts the text (e.g. "SUV") to the VehicleType enum.
                    VehicleType type = VehicleType.valueOf(f[4].trim());

                    // The Driver constructor validates the phone number and plate.
                    IDriver driver = new Driver(Integer.parseInt(f[0].trim()), f[1].trim(), f[2].trim(), f[3].trim(), type);

                    // Marks the loading as failed if the driver was not added.
                    if (!addDriver(driver)) {
                        allLoaded = false;
                    }
                }
                catch (Exception e) {
                    // Continues reading if a line has invalid data.
                    allLoaded = false;
                }
            }

            file.close();

            // Returns true if all drivers were loaded successfully.
            return allLoaded;
        }
        catch (FileNotFoundException e) {
            // Returns false if the file cannot be found.
            return false;
        }
    }

    // Adds a driver; fails if the driver is invalid or the ID/plate already exists
    public boolean addDriver(IDriver driver) {
        // Rejects a missing driver or a missing name.
        if (driver == null || isBlank(driver.getName())) {
            return false;
        }

        // DriverList rejects duplicate IDs and duplicate vehicle plates.
        return drivers.add(driver);
    }

    // Searches for a driver by ID
    public IDriver searchDriverById(int driverId) {
        return drivers.findById(driverId);
    }

    // Searches for a driver by vehicle plate
    public IDriver searchDriverByVehiclePlate(String vehiclePlate) {
        return drivers.findByVehiclePlate(vehiclePlate);
    }

    // Searches for all drivers with the given vehicle type
    public LinkedList<IDriver> searchDriversByVehicleType(VehicleType vehicleType) {
        return drivers.findByVehicleType(vehicleType);
    }

    // Returns all drivers sorted by ID
    public LinkedList<IDriver> getAllDrivers() {
        return drivers.getAll();
    }

    // Schedules a private ride for one rider with one driver
    public boolean schedulePrivateRide(String pickupLocation, IDateTime pickupTime, IDateTime dropoffTime, String dropoffLocation, int riderId, int driverId) {

        // Checks that the times are valid (pickup must be before dropoff).
        if (!validTimes(pickupTime, dropoffTime)) {
            return false;
        }

        // The rider and the driver must exist.
        IRider rider = riders.findById(riderId);
        IDriver driver = drivers.findById(driverId);

        if (rider == null || driver == null) {
            return false;
        }

        // The ride must not overlap with the rider's or the driver's other rides.
        if (riderHasConflict(riderId, pickupTime, dropoffTime) || driverHasConflict(driverId, pickupTime, dropoffTime)) {
            return false;
        }

        // Creates the ride and adds it to the ride list.
        IPrivateRide ride = new PrivateRide(pickupLocation, pickupTime, dropoffTime, dropoffLocation, driver, rider);
        rides.addRide(ride);

        // Saves the ride in the rider's and the driver's ride history.
        rider.getRideHistory().insert(ride);
        driver.getRideHistory().insert(ride);

        return true;
    }

    // Schedules a shared ride for several riders with one driver
    public boolean scheduleSharedRide(String pickupLocation, IDateTime pickupTime, IDateTime dropoffTime, String dropoffLocation, int[] riderIds, int driverId) {

        // Checks that the times are valid and there are at least two riders.
        if (!validTimes(pickupTime, dropoffTime) || riderIds == null || riderIds.length < 2) {
            return false;
        }

        // The driver must exist and must not have an overlapping ride.
        IDriver driver = drivers.findById(driverId);

        if (driver == null || driverHasConflict(driverId, pickupTime, dropoffTime)) {
            return false;
        }

        // Checks every rider BEFORE creating the ride, so nothing is added if one fails.
        for (int i = 0; i < riderIds.length; i++) {

            // Every rider must exist.
            if (riders.findById(riderIds[i]) == null) {
                return false;
            }

            // The same rider cannot be listed twice.
            for (int j = 0; j < i; j++) {
                if (riderIds[j] == riderIds[i]) {
                    return false;
                }
            }

            // Every rider must be free at this time.
            if (riderHasConflict(riderIds[i], pickupTime, dropoffTime)) {
                return false;
            }
        }

        // Creates the shared ride and adds every rider as a participant.
        ISharedRide ride = new SharedRide(pickupLocation, pickupTime, dropoffTime, dropoffLocation, driver);

        for (int i = 0; i < riderIds.length; i++) {
            IRider rider = riders.findById(riderIds[i]);
            ride.addParticipant(rider);

            // Saves the ride in each rider's history.
            rider.getRideHistory().insert(ride);
        }

        // Adds the ride to the ride list and the driver's history.
        rides.addRide(ride);
        driver.getRideHistory().insert(ride);

        return true;
    }

    // Deletes a ride from the ride list and from the history of its driver and riders
    private void deleteRide(IRide ride) {
        rides.removeRideById(ride.getRideId());

        // Removes the ride from the driver's history.
        removeFromHistory(ride.getDriver().getRideHistory(), ride.getRideId());

        if (ride instanceof IPrivateRide) {
            // Removes the ride from the rider's history.
            IRider rider = ((IPrivateRide) ride).getRider();
            removeFromHistory(rider.getRideHistory(), ride.getRideId());
        }
        else if (ride instanceof ISharedRide) {
            // Removes the ride from every remaining participant's history.
            LinkedList<IRider> participants = ((ISharedRide) ride).getParticipants();

            if (!participants.empty()) {
                participants.findFirst();

                while (true) {
                    removeFromHistory(participants.retrieve().getRideHistory(), ride.getRideId());

                    if (participants.last()) {
                        break;
                    }

                    participants.findNext();
                }
            }
        }
    }

    // Removes the ride with this ID from a person's ride history
    private void removeFromHistory(LinkedList<IRide> history, int rideId) {
        if (history.empty()) {
            return;
        }

        history.findFirst();

        while (true) {
            if (history.retrieve().getRideId() == rideId) {
                history.remove();
                return;
            }

            if (history.last()) {
                return;
            }

            history.findNext();
        }
    }

    // Returns true if both times exist and the pickup is before the dropoff
    private boolean validTimes(IDateTime pickupTime, IDateTime dropoffTime) {
        return pickupTime != null && dropoffTime != null && pickupTime.compareTo(dropoffTime) < 0;
    }

    // Two rides overlap when  pickup1 < dropoff2  AND  pickup2 < dropoff1
    private boolean overlaps(IRide ride, IDateTime pickupTime, IDateTime dropoffTime) {
        return ride.getPickupTime().compareTo(dropoffTime) < 0 && pickupTime.compareTo(ride.getDropoffTime()) < 0;
    }

    // Returns true if the rider already has a ride that overlaps with this time
    private boolean riderHasConflict(int riderId, IDateTime pickupTime, IDateTime dropoffTime) {
        LinkedList<IRide> allRides = rides.getAllAlphabetically();

        if (allRides.empty()) {
            return false;
        }

        allRides.findFirst();

        while (true) {
            IRide ride = allRides.retrieve();

            // Checks only the rides this rider is part of.
            if (ride.hasRider(riderId) && overlaps(ride, pickupTime, dropoffTime)) {
                return true;
            }

            if (allRides.last()) {
                break;
            }

            allRides.findNext();
        }

        return false;
    }

    // Returns true if the driver already has a ride that overlaps with this time
    private boolean driverHasConflict(int driverId, IDateTime pickupTime, IDateTime dropoffTime) {
        LinkedList<IRide> allRides = rides.getAllAlphabetically();

        if (allRides.empty()) {
            return false;
        }

        allRides.findFirst();

        while (true) {
            IRide ride = allRides.retrieve();

            // Checks only the rides assigned to this driver.
            if (ride.getDriver().getId() == driverId && overlaps(ride, pickupTime, dropoffTime)) {
                return true;
            }

            if (allRides.last()) {
                break;
            }

            allRides.findNext();
        }

        return false;
    }

}