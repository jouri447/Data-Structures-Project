/**
 * A single rider in the ride-sharing system.
 * Adds email and home city to the common Person fields.
 * Riders are compared by ID.
 */
public class Rider extends Person implements IRider {

    private String email;
    private String homeCity;

    public Rider(int riderId, String name, String email, String phoneNumber, String homeCity) {
        super(riderId, name, phoneNumber);
        this.email = email;
        this.homeCity = homeCity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    // Negative, zero or positive as this ID is smaller, equal or larger
    public int compareTo(IRider other) {
        return Integer.compare(getId(), other.getId());
    }

    public String toString() {
        return "Rider " + super.toString() + " | " + email + " | " + homeCity;
    }
}
