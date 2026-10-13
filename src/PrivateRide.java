public class PrivateRide extends Ride implements IPrivateRide {

    private IRider rider;

    public PrivateRide(String from, IDateTime departure,IDateTime arrival, String to,IDriver assignedDriver, IRider selectedRider) {

        super(from, departure, arrival, to, assignedDriver);
        rider = selectedRider;
    }

    public IRider getRider() {
        return rider;
    }

    public void setRider(IRider newRider) {
        rider = newRider;
    }

    public boolean hasRider(int riderId) {
        return rider.getId() == riderId;
    }

    public String toString() {
        return "Private " + super.toString()
                + " | Rider " + rider.getId()
                + " " + rider.getName();
    }
}