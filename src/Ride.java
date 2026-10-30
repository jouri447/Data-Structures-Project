/**
 * Abstract ride: pickup/drop-off locations and times plus the assigned driver.
 * Cannot be created directly; use PrivateRide or SharedRide.
 * Pickup and drop-off times are final, so they cannot change after creation.
 */
public abstract class Ride implements IRide {

    // Counts all rides created, used to give each ride a unique ID
    private static int rideCounter = 0;

    private final int rideId;
    private String startLocation;
    private final IDateTime startTime;
    private final IDateTime endTime;
    private String endLocation;
    private IDriver assignedDriver;

    // Creates a ride with the next unique ride ID
    public Ride(String from, IDateTime departure,
                IDateTime arrival, String to, IDriver driverInfo) {

        rideCounter++;
        rideId = rideCounter;

        startLocation = from;
        startTime = departure;
        endTime = arrival;
        endLocation = to;
        assignedDriver = driverInfo;
    }

    // Returns the unique ride ID
    public int getRideId() {
        return rideId;
    }

    // Returns the pickup location
    public String getPickupLocation() {
        return startLocation;
    }

    // Changes the pickup location
    public void setPickupLocation(String newLocation) {
        startLocation = newLocation;
    }

    // Returns the pickup date/time
    public IDateTime getPickupTime() {
        return startTime;
    }

    // Returns the drop-off date/time
    public IDateTime getDropoffTime() {
        return endTime;
    }

    // Returns the drop-off location
    public String getDropoffLocation() {
        return endLocation;
    }

    // Changes the drop-off location
    public void setDropoffLocation(String newLocation) {
        endLocation = newLocation;
    }

    // Returns the driver assigned to this ride
    public IDriver getDriver() {
        return assignedDriver;
    }

    // Changes the driver assigned to this ride
    public void setDriver(IDriver newDriver) {
        assignedDriver = newDriver;
    }

    // Compares rides alphabetically by pickup location
    public int compareTo(IRide other) {
        return startLocation.compareTo(other.getPickupLocation());
    }

    // Each ride type checks its own rider(s)
    public abstract boolean hasRider(int riderId);

    // Returns the ride details as one line
    public String toString() {
        return "Ride " + rideId
                + " | " + startLocation + " " + startTime.format()
                + " -> " + endLocation + " " + endTime.format()
                + " | Driver " + assignedDriver.getId()
                + " " + assignedDriver.getName();
    }
}