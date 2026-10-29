
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
        if (vehiclePlate == null ||
            !vehiclePlate.matches("[A-Z]{3}[0-9]{4}")) {

            throw new IllegalArgumentException(
                "Vehicle plate must be 3 uppercase letters followed by 4 digits");
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