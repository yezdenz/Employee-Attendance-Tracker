import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Company {
    private ArrayList<Employee> employees = new ArrayList<>();
    private HashMap<Integer, Employee> empMap = new HashMap<>();
    private final String fileName = "C:\\Users\\hoody\\OneDrive\\Documents\\NetBeansProjects\\Final Exam Lab\\build\\classes\\employees.txt";

    public ArrayList<Employee> getEmployees() { return employees; }
    public HashMap<Integer, Employee> getEmployeeMap() { return empMap; }

    public void addEmployee(Employee emp) {
        try {
            employees.add(emp);
            empMap.put(emp.getEmployeeID(), emp);
        } catch (Exception e) {
            System.out.println("Error adding employee.");
        }
    }

    public Employee getEmployee(int id) {
        try {
            return empMap.get(id);
        } catch (Exception e) {
            System.out.println("Error getting employee.");
            return null;
        }
    }

    public void saveToFile() {
        try {
            File file = new File(fileName);
            file.getParentFile().mkdirs(); 

            try (FileWriter writer = new FileWriter(file)) {
                for (Employee emp : employees) {
                    writer.write("Employee ID: " + emp.getEmployeeID() + "\n");
                    writer.write("Employee Name: " + emp.getName() + "\n");
                    writer.write("Employee Daily Salary: " + String.format("%.2f", emp.getSalaryPerDay()) + "\n");

                    ArrayList<AttendanceRecord> attendance = emp.getAttendanceList();


                    for (AttendanceRecord a : attendance) {
                        writer.write("(" + String.format("%04d-%02d", a.getYear(), a.getMonth()) + "), "
                                + "Day " + a.getDay() + ", " + a.getStatus() + "\n");
                    }

                    writer.write("\n"); 
                }
            }

            System.out.println("Data saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }



    public void loadFromFile() {
        try (FileReader reader = new FileReader(fileName);
             Scanner sc = new Scanner(reader)) {

            employees.clear();
            empMap.clear();
            Employee emp = null;

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("Employee ID:")) {
                    if (emp != null) 
                        addEmployee(emp);
                    int id = Integer.parseInt(line.substring(13).trim());
                        emp = new Employee(id, "", 0);
                } 
                else if (line.startsWith("Employee Name:")) {
                        emp = new Employee(emp.getEmployeeID(), line.substring(15).trim(), 
                            emp.getSalaryPerDay());
                } 
                else if (line.startsWith("Employee Daily Salary:")) {
                        emp = new Employee(emp.getEmployeeID(), emp.getName(),
                            Double.parseDouble(line.substring(22).trim()));
                } 
                else if (line.startsWith("(") && line.contains("Day")) {

            
                    String[] parts = line.split(",");
                    String ym = parts[0].trim().replace("(", "").replace(")", "");
                    int year = Integer.parseInt(ym.split("-")[0]);
                    int month = Integer.parseInt(ym.split("-")[1]);
                    int day = Integer.parseInt(parts[1].trim().split(" ")[1]);
                    String status = parts[2].trim();
                    boolean isPresent = status.equals("P");

                    boolean exists = false;
                    for (AttendanceRecord record : emp.getAttendanceList()) {
                        if (record.getYear() == year && record.getMonth() == month && record.getDay() == day) {
                            exists = true;
                            break;
                        }
                    }

                    if (!exists) {
                        emp.addAttendance(new AttendanceRecord(day, isPresent, month, year, status));
                    }
                }
            }

            if (emp != null) addEmployee(emp);
            System.out.println("Data loaded successfully.");

        } catch (IOException e) {
            System.out.println("File not found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading file.");
        }
    }
}
