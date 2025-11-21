import java.util.ArrayList;

public class Employee {
    private int employeeID;
    private String name;
    private double salaryPerDay;
    private ArrayList<AttendanceRecord> attendanceList = new ArrayList<>();

    public Employee(int employeeID, String name, double salaryPerDay) {
            this.employeeID = employeeID;
            this.name = name;
            this.salaryPerDay = salaryPerDay;
    }

    public int getEmployeeID() { return employeeID; }
    public String getName() { return name; }
    public double getSalaryPerDay() { return salaryPerDay; }
    public ArrayList<AttendanceRecord> getAttendanceList() { return attendanceList; }

    public void addAttendance(AttendanceRecord record) {
        try {
            attendanceList.add(record);
        } catch (Exception e) {
            System.out.println("Error adding attendance record.");
        }
    }
}
