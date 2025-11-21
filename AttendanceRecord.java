public class AttendanceRecord {
    private int day;
    private boolean isPresent;
    private int month;
    private int year;
    private String status; 
    
    public AttendanceRecord(int day, boolean isPresent, int month, int year, String status) {
            this.day = day;
            this.isPresent = isPresent;
            this.month = month;
            this.year = year; 
            this.status = status; 
    }

    public int getDay() { return day; }
    public boolean isPresent() { return isPresent; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
    public String getStatus() { return status; }
}
