public class SharedRide extends Ride implements ISharedRide {

    private LinkedList<IRider> riders;

    public SharedRide(String from, IDateTime departure,IDateTime arrival, String to,IDriver assignedDriver) {

        super(from, departure, arrival, to, assignedDriver);
        riders = new LinkedList<IRider>();
    }

    public LinkedList<IRider> getParticipants() {
        return riders;
    }

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

    public boolean isEmpty() {
        return riders.empty();
    }

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