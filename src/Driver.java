
public class Driver extends Person implements IDriver {

    private String vehiclePlate;
    private VehicleType vehicleType;

    public Driver(int id, String name, String phoneNumber,
                  String vehiclePlate, VehicleType vehicleType) {

        super(id, name, phoneNumber);
        setVehiclePlate(vehiclePlate);
        setVehicleType(vehicleType);
    }

    @Override
    public String getVehiclePlate() {
        return vehiclePlate;
    }

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

    @Override
    public VehicleType getVehicleType() {
        return vehicleType;
    }

    @Override
    public void setVehicleType(VehicleType vehicleType) {
        if (vehicleType == null) {
            throw new IllegalArgumentException(
                "Vehicle type cannot be null");
        }

        this.vehicleType = vehicleType;
    }

    @Override
    public int compareTo(IDriver other) {
        return Integer.compare(getId(), other.getId());
    }

    @Override
    public String toString() {
        return super.toString() +
               ", Plate: " + vehiclePlate +
               ", Type: " + vehicleType;
    }
}