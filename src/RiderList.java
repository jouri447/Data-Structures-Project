/**
 * Linked list of riders kept in sorted order by rider ID.
 * Uses the LinkedList (ADT List) implementation from the lectures.
 */
public class RiderList implements IRiderList {

    private LinkedList<IRider> riders;
    private int count;   // number of riders stored

    public RiderList() {
        riders = new LinkedList<IRider>();
        count = 0;
    }

    // Inserts the rider in sorted order by ID; fails if the ID already exists
    public boolean add(IRider rider) {
        if (rider == null)
            return false;

        if (riders.empty()) {
            riders.insert(rider);
            count++;
            return true;
        }

        // Move to the first rider whose ID is >= the new ID (or to the last rider)
        riders.findFirst();
        while (riders.last() == false && riders.retrieve().compareTo(rider) < 0)
            riders.findNext();

        int cmp = riders.retrieve().compareTo(rider);
        if (cmp == 0)
            return false;   // duplicate ID

        if (cmp > 0) {
            // insert() only adds after current, so to add before current:
            // put the new rider in the current node, then insert the old rider after it
            IRider old = riders.retrieve();
            riders.update(rider);
            riders.insert(old);
        } else {
            // All IDs are smaller, add at the end
            riders.insert(rider);
        }
        count++;
        return true;
    }

    public IRider findById(int riderId) {
        if (riders.empty() == false) {
            riders.findFirst();
            while (riders.last() == false) {
                if (riders.retrieve().getId() == riderId)
                    return riders.retrieve();
                riders.findNext();
            }
            if (riders.retrieve().getId() == riderId)
                return riders.retrieve();
        }
        return null;
    }

    public LinkedList<IRider> findByName(String fullName) {
        LinkedList<IRider> result = new LinkedList<IRider>();
        if (riders.empty() == false) {
            riders.findFirst();
            while (riders.last() == false) {
                if (riders.retrieve().getName().equals(fullName))
                    result.insert(riders.retrieve());
                riders.findNext();
            }
            if (riders.retrieve().getName().equals(fullName))
                result.insert(riders.retrieve());
        }
        return result;
    }

    public IRider findByEmail(String email) {
        if (riders.empty() == false) {
            riders.findFirst();
            while (riders.last() == false) {
                if (riders.retrieve().getEmail().equals(email))
                    return riders.retrieve();
                riders.findNext();
            }
            if (riders.retrieve().getEmail().equals(email))
                return riders.retrieve();
        }
        return null;
    }

    public LinkedList<IRider> findByHomeCity(String homeCity) {
        LinkedList<IRider> result = new LinkedList<IRider>();
        if (riders.empty() == false) {
            riders.findFirst();
            while (riders.last() == false) {
                if (riders.retrieve().getHomeCity().equals(homeCity))
                    result.insert(riders.retrieve());
                riders.findNext();
            }
            if (riders.retrieve().getHomeCity().equals(homeCity))
                result.insert(riders.retrieve());
        }
        return result;
    }

    // Returns a copy of all riders in ID order (one pass, O(n))
    public LinkedList<IRider> getAll() {
        LinkedList<IRider> result = new LinkedList<IRider>();
        if (riders.empty() == false) {
            riders.findFirst();
            while (riders.last() == false) {
                result.insert(riders.retrieve());
                riders.findNext();
            }
            result.insert(riders.retrieve());
        }
        return result;
    }

    public boolean removeById(int riderId) {
        if (riders.empty() == false) {
            riders.findFirst();
            while (riders.last() == false) {
                if (riders.retrieve().getId() == riderId) {
                    riders.remove();
                    count--;
                    return true;
                }
                riders.findNext();
            }
            if (riders.retrieve().getId() == riderId) {
                riders.remove();
                count--;
                return true;
            }
        }
        return false;
    }

    public boolean removeByEmail(String email) {
        if (riders.empty() == false) {
            riders.findFirst();
            while (riders.last() == false) {
                if (riders.retrieve().getEmail().equals(email)) {
                    riders.remove();
                    count--;
                    return true;
                }
                riders.findNext();
            }
            if (riders.retrieve().getEmail().equals(email)) {
                riders.remove();
                count--;
                return true;
            }
        }
        return false;
    }

    // Removes EVERY rider with this exact full name; returns how many were removed
    public int removeByName(String fullName) {
        int removed = 0;
        int total = count;   // visit each of the n riders exactly once

        if (riders.empty())
            return 0;

        riders.findFirst();
        for (int i = 0; i < total; i++) {
            if (riders.retrieve().getName().equals(fullName)) {
                riders.remove();   // current moves to the next rider by itself
                removed++;
            } else if (riders.last() == false) {
                riders.findNext();
            }
        }
        count -= removed;
        return removed;
    }

    // Removes EVERY rider from this home city; returns how many were removed
    public int removeByHomeCity(String homeCity) {
        int removed = 0;
        int total = count;   // visit each of the n riders exactly once

        if (riders.empty())
            return 0;

        riders.findFirst();
        for (int i = 0; i < total; i++) {
            if (riders.retrieve().getHomeCity().equals(homeCity)) {
                riders.remove();   // current moves to the next rider by itself
                removed++;
            } else if (riders.last() == false) {
                riders.findNext();
            }
        }
        count -= removed;
        return removed;
    }

    public int size() {
        return count;
    }
}
