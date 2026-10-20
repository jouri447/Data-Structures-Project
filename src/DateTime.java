/**
 * A single point in time (year, month, day, hour, minute).
 * Fields are set only in the constructor and cannot be modified.
 * Ordered chronologically so rides can be checked for overlap.
 */
public class DateTime implements IDateTime {

    private final int year;
    private final int month;
    private final int day;
    private final int hour;
    private final int minute;

    public DateTime(int year, int month, int day, int hour, int minute) {
        if (month < 1 || month > 12 || day < 1 || day > 31
                || hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Invalid date/time values");
        }
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    // Builds a DateTime from text in the CSV format "MM/DD/YYYY HH:MM"
    public DateTime(String text) {
        this(Integer.parseInt(text.trim().substring(6, 10)),   // year
             Integer.parseInt(text.trim().substring(0, 2)),    // month
             Integer.parseInt(text.trim().substring(3, 5)),    // day
             Integer.parseInt(text.trim().substring(11, 13)),  // hour
             Integer.parseInt(text.trim().substring(14, 16))); // minute
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    // Returns "MM/DD/YYYY HH:MM"
    public String format() {
        return String.format("%02d/%02d/%04d %02d:%02d", month, day, year, hour, minute);
    }

    // Compare by year, then month, then day, then hour, then minute
    public int compareTo(IDateTime other) {
        if (year != other.getYear())
            return Integer.compare(year, other.getYear());
        if (month != other.getMonth())
            return Integer.compare(month, other.getMonth());
        if (day != other.getDay())
            return Integer.compare(day, other.getDay());
        if (hour != other.getHour())
            return Integer.compare(hour, other.getHour());
        return Integer.compare(minute, other.getMinute());
    }

    public String toString() {
        return format();
    }
}
