public abstract class Ride implements IRide {

    private static int rideCounter = 0;

    private final int rideId;
    private String startLocation;
    private final IDateTime startTime;
    private final IDateTime endTime;
    private String endLocation;
    private IDriver assignedDriver;

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

    public int getRideId() {
        return rideId;
    }

    public String getPickupLocation() {
        return startLocation;
    }

    public void setPickupLocation(String newLocation) {
        startLocation = newLocation;
    }

    public IDateTime getPickupTime() {
        return startTime;
    }

    public IDateTime getDropoffTime() {
        return endTime;
    }

    public String getDropoffLocation() {
        return endLocation;
    }

    public void setDropoffLocation(String newLocation) {
        endLocation = newLocation;
    }

    public IDriver getDriver() {
        return assignedDriver;
    }

    public void setDriver(IDriver newDriver) {
        assignedDriver = newDriver;
    }

    public int compareTo(IRide other) {
        return startLocation.compareTo(other.getPickupLocation());
    }

    public abstract boolean hasRider(int riderId);

    public String toString() {
        return "Ride " + rideId
                + " | " + startLocation + " " + startTime.format()
                + " -> " + endLocation + " " + endTime.format()
                + " | Driver " + assignedDriver.getId()
                + " " + assignedDriver.getName();
    }
}