
/**
 * A single driver in the ride-sharing system.
 * Adds the vehicle plate and vehicle type to the common Person fields.
 * Drivers are compared by ID.
 */
public class Driver extends Person implements IDriver {

    private String vehiclePlate;
    private VehicleType vehicleType;

    // Creates a driver; the plate and type are validated by their setters
    public Driver(int id, String name, String phoneNumber,
                  String vehiclePlate, VehicleType vehicleType) {

        super(id, name, phoneNumber);
        setVehiclePlate(vehiclePlate);
        setVehicleType(vehicleType);
    }

    // Returns the vehicle plate
    @Override
    public String getVehiclePlate() {
        return vehiclePlate;
    }

    // Plate must be exactly 3 uppercase letters then 4 digits (e.g. ABC1234)
    @Override
    public void setVehiclePlate(String vehiclePlate) {
        if (vehiclePlate == null || vehiclePlate.length() != 7) {
            throw new IllegalArgumentException(
                "Vehicle plate must be 3 uppercase letters followed by 4 digits");
        }

        for (int i = 0; i < 7; i++) {
            char c = vehiclePlate.charAt(i);

            // The first 3 characters must be uppercase letters.
            if (i < 3 && (c < 'A' || c > 'Z')) {
                throw new IllegalArgumentException(
                    "Vehicle plate must be 3 uppercase letters followed by 4 digits");
            }

            // The last 4 characters must be digits.
            if (i >= 3 && (c < '0' || c > '9')) {
                throw new IllegalArgumentException(
                    "Vehicle plate must be 3 uppercase letters followed by 4 digits");
            }
        }

        this.vehiclePlate = vehiclePlate;
    }

    // Returns the vehicle type
    @Override
    public VehicleType getVehicleType() {
        return vehicleType;
    }

    // Sets the vehicle type; it cannot be null
    @Override
    public void setVehicleType(VehicleType vehicleType) {
        if (vehicleType == null) {
            throw new IllegalArgumentException(
                "Vehicle type cannot be null");
        }

        this.vehicleType = vehicleType;
    }

    // Negative, zero or positive as this ID is smaller, equal or larger
    @Override
    public int compareTo(IDriver other) {
        return Integer.compare(getId(), other.getId());
    }

    // Returns the driver details as one line
    @Override
    public String toString() {
        return super.toString() +
               ", Plate: " + vehiclePlate +
               ", Type: " + vehicleType;
    }
}