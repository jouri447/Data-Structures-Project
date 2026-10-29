public class DriverList implements IDriverList {

private LinkedList<IDriver> drivers;

public DriverList() {
drivers = new LinkedList<IDriver>();
}

@Override
public boolean add(IDriver driver) {
if (driver == null) {
return false;
}

if (findById(driver.getId()) != null ||
findByVehiclePlate(driver.getVehiclePlate()) != null) {
return false;
}

// Creates a temporary list to keep drivers sorted by ID.
LinkedList<IDriver> temp = new LinkedList<IDriver>();
boolean added = false;

if (!drivers.empty()) {
drivers.findFirst();

while (true) {
IDriver currentDriver = drivers.retrieve();

// Inserts the new driver before the first larger ID.
if (!added && currentDriver.getId() > driver.getId()) {
temp.insert(driver);
added = true;
}

temp.insert(currentDriver);

if (drivers.last()) {
break;
}

drivers.findNext();
}
}

// Adds the driver at the end if no larger ID was found.
if (!added) {
temp.insert(driver);
}

drivers = temp;
return true;
}

@Override
public IDriver findById(int driverId) {
if (drivers.empty()) {
return null;
}

drivers.findFirst();

while (true) {
if (drivers.retrieve().getId() == driverId) {
return drivers.retrieve();
}

if (drivers.last()) {
break;
}

drivers.findNext();
}

return null;
}

@Override
public LinkedList<IDriver> findByName(String fullName) {
LinkedList<IDriver> result = new LinkedList<IDriver>();

if (drivers.empty()) {
return result;
}

drivers.findFirst();

while (true) {
if (drivers.retrieve().getName().equals(fullName)) {
result.insert(drivers.retrieve());
}

if (drivers.last()) {
break;
}

drivers.findNext();
}

return result;
}

@Override
public IDriver findByVehiclePlate(String vehiclePlate) {
if (drivers.empty()) {
return null;
}

drivers.findFirst();

while (true) {
if (drivers.retrieve().getVehiclePlate().equals(vehiclePlate)) {
return drivers.retrieve();
}

if (drivers.last()) {
break;
}

drivers.findNext();
}

return null;
}

@Override
public LinkedList<IDriver> findByVehicleType(VehicleType vehicleType) {
LinkedList<IDriver> result = new LinkedList<IDriver>();

if (drivers.empty()) {
return result;
}

drivers.findFirst();

while (true) {
if (drivers.retrieve().getVehicleType() == vehicleType) {
result.insert(drivers.retrieve());
}

if (drivers.last()) {
break;
}

drivers.findNext();
}

return result;
}

@Override
public LinkedList<IDriver> getAll() {
LinkedList<IDriver> result = new LinkedList<IDriver>();

if (drivers.empty()) {
return result;
}

drivers.findFirst();

while (true) {
result.insert(drivers.retrieve());

if (drivers.last()) {
break;
}

drivers.findNext();
}

return result;
}

@Override
public boolean removeById(int driverId) {
if (drivers.empty()) {
return false;
}

drivers.findFirst();

while (true) {
if (drivers.retrieve().getId() == driverId) {
drivers.remove();
return true;
}

if (drivers.last()) {
break;
}

drivers.findNext();
}

return false;
}

@Override
public boolean removeByVehiclePlate(String vehiclePlate) {
if (drivers.empty()) {
return false;
}

drivers.findFirst();

while (true) {
if (drivers.retrieve().getVehiclePlate().equals(vehiclePlate)) {
drivers.remove();
return true;
}

if (drivers.last()) {
break;
}

drivers.findNext();
}

return false;
}

@Override
public int removeByName(String fullName) {
int count = 0;

if (drivers.empty()) {
return count;
}

drivers.findFirst();

// Builds a new list without drivers matching the name.
LinkedList<IDriver> temp = new LinkedList<IDriver>();

while (true) {
IDriver driver = drivers.retrieve();

if (driver.getName().equals(fullName)) {
count++;
} else {
temp.insert(driver);
}

if (drivers.last()) {
break;
}

drivers.findNext();
}

drivers = temp;
return count;
}

@Override
public int removeByVehicleType(VehicleType vehicleType) {
int count = 0;

if (drivers.empty()) {
return count;
}

drivers.findFirst();

// Builds a new list without drivers matching the vehicle type.
LinkedList<IDriver> temp = new LinkedList<IDriver>();

while (true) {
IDriver driver = drivers.retrieve();

if (driver.getVehicleType() == vehicleType) {
count++;
} else {
temp.insert(driver);
}

if (drivers.last()) {
break;
}

drivers.findNext();
}

drivers = temp;
return count;
}

@Override
public int size() {
int count = 0;

if (drivers.empty()) {
return count;
}

drivers.findFirst();

while (true) {
count++;

if (drivers.last()) {
break;
}

drivers.findNext();
}

return count;
}
}