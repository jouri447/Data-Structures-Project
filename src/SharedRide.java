/**
 * A shared ride (carpool) with several riders stored in a LinkedList.
 */
public class SharedRide extends Ride implements ISharedRide {

    private LinkedList<IRider> riders;

    // Creates a shared ride with an empty list of participants
    public SharedRide(String from, IDateTime departure,IDateTime arrival, String to,IDriver assignedDriver) {

        super(from, departure, arrival, to, assignedDriver);
        riders = new LinkedList<IRider>();
    }

    // Returns the list of riders in this ride
    public LinkedList<IRider> getParticipants() {
        return riders;
    }

    // Adds a rider at the end of the list; fails if the rider is already in the ride
    public boolean addParticipant(IRider newRider) {

        if (hasRider(newRider.getId())) {
            return false;
        }

        if (riders.empty()) {
            riders.insert(newRider);
            return true;
        }

        riders.findFirst();

        while (!riders.last()) {
            riders.findNext();
        }

        riders.insert(newRider);
        return true;
    }

    // Removes the rider with this ID; returns false if not found
    public boolean removeParticipantById(int riderId) {

        if (riders.empty()) {
            return false;
        }

        riders.findFirst();

        while (!riders.last()) {

            if (riders.retrieve().getId() == riderId) {
                riders.remove();
                return true;
            }

            riders.findNext();
        }

        if (riders.retrieve().getId() == riderId) {
            riders.remove();
            return true;
        }

        return false;
    }

    // Returns true if no riders are left
    public boolean isEmpty() {
        return riders.empty();
    }

    // Returns true if a rider with this ID is in the ride
    public boolean hasRider(int riderId) {

        if (riders.empty()) {
            return false;
        }

        riders.findFirst();

        while (!riders.last()) {

            if (riders.retrieve().getId() == riderId) {
                return true;
            }

            riders.findNext();
        }

        return riders.retrieve().getId() == riderId;
    }

    // Returns the ride details including all riders
    public String toString() {

        String result = "Shared " + super.toString() + " | Riders: ";

        if (riders.empty()) {
            return result + "none";
        }

        riders.findFirst();

        while (!riders.last()) {

            IRider currentRider = riders.retrieve();

            result = result + currentRider.getId()
                    + " " + currentRider.getName() + ", ";

            riders.findNext();
        }

        IRider currentRider = riders.retrieve();

        return result + currentRider.getId()
                + " " + currentRider.getName();
    }
}