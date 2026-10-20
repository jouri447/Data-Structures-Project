/**
 * Abstract base class for every person in the system (riders and drivers).
 * Holds the fields shared by Rider and Driver so they are written once.
 * Person cannot be instantiated directly.
 */
public abstract class Person implements IPerson {

    private final int id;                    // unique ID, cannot change after creation
    private String name;
    private String phoneNumber;
    private LinkedList<IRide> rideHistory;   // rides this person is part of

    public Person(int id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        setPhoneNumber(phoneNumber);          // validates the format
        rideHistory = new LinkedList<IRide>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Phone number must be exactly 10 digits, otherwise IllegalArgumentException
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != 10) {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits: " + phoneNumber);
        }
        for (int i = 0; i < phoneNumber.length(); i++) {
            char c = phoneNumber.charAt(i);
            if (c < '0' || c > '9') {
                throw new IllegalArgumentException("Phone number must contain digits only: " + phoneNumber);
            }
        }
        this.phoneNumber = phoneNumber;
    }

    public LinkedList<IRide> getRideHistory() {
        return rideHistory;
    }

    public String toString() {
        return id + " | " + name + " | " + phoneNumber;
    }
}
