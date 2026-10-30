
/**
 * Linked list of rides kept in alphabetical order by pickup location.
 * Uses the LinkedList (ADT List) implementation from the lectures.
 */
public class RideList implements IRideList {

    private LinkedList<IRide> rides;
    private int count;

    // Creates an empty ride list
    public RideList() {
        rides = new LinkedList<IRide>();
        count = 0;
    }

    // Inserts the ride in alphabetical order by pickup location
    public boolean addRide(IRide newRide) {

        if (newRide == null) {
            return false;
        }

        if (rides.empty()) {
            rides.insert(newRide);
            count++;
            return true;
        }

        rides.findFirst();

        if (newRide.compareTo(rides.retrieve()) < 0) {

            IRide firstRide = rides.retrieve();

            rides.insert(firstRide);
            rides.findFirst();
            rides.update(newRide);

            count++;
            return true;
        }

        while (!rides.last()) {

            rides.findNext();

            if (newRide.compareTo(rides.retrieve()) < 0) {

                IRide oldRide = rides.retrieve();

                rides.update(newRide);
                rides.insert(oldRide);

                count++;
                return true;
            }
        }

        rides.insert(newRide);
        count++;
        return true;
    }

    // Removes the ride with this ID; returns false if not found
    public boolean removeRideById(int rideId) {

        if (rides.empty()) {
            return false;
        }

        rides.findFirst();

        while (!rides.last()) {

            if (rides.retrieve().getRideId() == rideId) {
                rides.remove();
                count--;
                return true;
            }

            rides.findNext();
        }

        if (rides.retrieve().getRideId() == rideId) {
            rides.remove();
            count--;
            return true;
        }

        return false;
    }

    // Returns a copy of all rides in alphabetical order (one pass, O(n))
    public LinkedList<IRide> getAllAlphabetically() {

        LinkedList<IRide> result = new LinkedList<IRide>();

        if (rides.empty()) {
            return result;
        }

        rides.findFirst();

        while (!rides.last()) {
            result.insert(rides.retrieve());
            rides.findNext();
        }

        result.insert(rides.retrieve());

        return result;
    }

    // Returns all rides with this pickup location
    public LinkedList<IRide> findByPickupLocation(String pickupLocation) {

        LinkedList<IRide> result = new LinkedList<IRide>();

        if (rides.empty()) {
            return result;
        }

        rides.findFirst();

        while (!rides.last()) {

            if (rides.retrieve().getPickupLocation().equalsIgnoreCase(pickupLocation)) {
                result.insert(rides.retrieve());
            }

            rides.findNext();
        }

        if (rides.retrieve().getPickupLocation().equalsIgnoreCase(pickupLocation)) {
            result.insert(rides.retrieve());
        }

        return result;
    }

    // Returns all rides that include a rider with this full name
    public LinkedList<IRide> findByRiderName(String riderFullName) {

        LinkedList<IRide> result = new LinkedList<IRide>();

        if (rides.empty()) {
            return result;
        }

        rides.findFirst();

        while (!rides.last()) {

            if (containsRiderName(rides.retrieve(), riderFullName)) {
                result.insert(rides.retrieve());
            }

            rides.findNext();
        }

        if (containsRiderName(rides.retrieve(), riderFullName)) {
            result.insert(rides.retrieve());
        }

        return result;
    }

    // Returns true if the ride (private or shared) has a rider with this name
    private boolean containsRiderName(IRide ride, String riderName) {

        if (ride instanceof IPrivateRide) {

            IPrivateRide privateRide = (IPrivateRide) ride;

            return privateRide.getRider().getName().equals(riderName);
        }

        if (ride instanceof ISharedRide) {

            ISharedRide sharedRide = (ISharedRide) ride;

            LinkedList<IRider> participants = sharedRide.getParticipants();

            if (participants.empty()) {
                return false;
            }

            participants.findFirst();

            while (!participants.last()) {

                if (participants.retrieve().getName().equals(riderName)) {
                    return true;
                }

                participants.findNext();
            }

            return participants.retrieve().getName().equals(riderName);
        }

        return false;
    }

    // Returns the number of rides stored
    public int size() {
        return count;
    }
}
