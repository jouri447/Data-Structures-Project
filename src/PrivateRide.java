/**
 * A ride with exactly one rider.
 */
public class PrivateRide extends Ride implements IPrivateRide {

    private IRider rider;

    // Creates a private ride for one rider
    public PrivateRide(String from, IDateTime departure,IDateTime arrival, String to,IDriver assignedDriver, IRider selectedRider) {

        super(from, departure, arrival, to, assignedDriver);
        rider = selectedRider;
    }

    // Returns the rider of this ride
    public IRider getRider() {
        return rider;
    }

    // Changes the rider of this ride
    public void setRider(IRider newRider) {
        rider = newRider;
    }

    // Returns true if this rider is the ride's rider
    public boolean hasRider(int riderId) {
        return rider.getId() == riderId;
    }

    // Returns the ride details including the rider
    public String toString() {
        return "Private " + super.toString()
                + " | Rider " + rider.getId()
                + " " + rider.getName();
    }
}