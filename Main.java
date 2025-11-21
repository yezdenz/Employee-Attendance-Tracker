import java.util.HashMap;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Company company = new Company();

    public static void main(String[] args) {
        company.loadFromFile();

        while (true) {
            System.out.println("\n==========Employee Attendance Tracker==========");
            System.out.println("1. Add a new employee");
            System.out.println("2. Mark attendance");
            System.out.println("3. Calculate monthly salary");
            System.out.println("4. Generate attendance report");
            System.out.println("5. Exit");
            System.out.println("===============================================");
            System.out.print("Choose an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1: addEmployee(); break;
                    case 2: markAttendance(); break;
                    case 3: calculateSalary(); break;
                    case 4: generateReport(); break;
                    case 5:
                        company.saveToFile();
                        System.out.println("Exiting program.");
                        System.exit(0);
                    default: System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input.");
            }
        }
    }

    private static void addEmployee() {
        try {
            System.out.print("\nEnter Employee ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Salary per Day: ");
            double sal = Double.parseDouble(scanner.nextLine());

            if (company.getEmployee(id) != null) {
                System.out.println("Employee already exists.");
                return;
            }
            company.addEmployee(new Employee(id, name, sal));
            System.out.println("Employee successfully added.");
        } catch (Exception e) {
            System.out.println("There was an error in adding the employee. Try again.");
        }
    }

    private static void markAttendance() {
        try {
            System.out.print("\nEnter Employee ID: ");
            int attendanceID = Integer.parseInt(scanner.nextLine());
            Employee emp = company.getEmployee(attendanceID);
            if (emp == null) { 
                System.out.println("Employee was not found."); 
                return; 
            }

            int year = 0, month = 0;
            while (true) {
                try {
                    System.out.print("Enter Year: ");
                    year = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter Month (1-12): ");
                    month = Integer.parseInt(scanner.nextLine());
                    if (month < 1 || month > 12) { System.out.println("Invalid month."); continue; }
                    break;
                } catch (Exception e) { System.out.println("Invalid input."); }
            }

            boolean alreadyRecorded = false;
            for (AttendanceRecord record : emp.getAttendanceList()) {
                if (record.getMonth() == month && record.getYear() == year) {
                    alreadyRecorded = true;
                    break;
                }
            }
            if (alreadyRecorded) {
                System.out.println("Attendance for " + year + "-" + String.format("%02d", month) + " already exists. Cannot mark again.");
                return;
            }

            int daysInMonth = 31;
            switch (month) {
                case 4: case 6: case 9: case 11: daysInMonth = 30; break;
                case 2:
                    if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) daysInMonth = 29;
                    else daysInMonth = 28;
                    break;
            }

            System.out.println("Mark attendance for " + daysInMonth + " days:");
            System.out.println("Enter 'P' for Present, 'A' for Absent, 'W' for Weekdays.");
            for (int day = 1; day <= daysInMonth; day++) {
                while (true) {
                    try {
                        System.out.print("Day " + day + ": ");
                        String status = scanner.nextLine().toUpperCase();
                        if (!status.equals("P") && !status.equals("A") && !status.equals("W")) {
                            System.out.println("Invalid input."); 
                            continue;
                        }
                        boolean isPresent = status.equals("P");
                        emp.addAttendance(new AttendanceRecord(day, isPresent, month, year, status));
                        break;
                    } catch (Exception e) { System.out.println("Error recording day."); }
                }
            }
            System.out.println("Attendance recorded successfully for " + year + "-" + String.format("%02d", month));
        } catch (Exception e) {
            System.out.println("There was an error in marking the attendance. Try again.");
        }
    }

    private static void calculateSalary() {
        try {
                System.out.print("\nEnter Employee ID: ");
                int empSalaryID = Integer.parseInt(scanner.nextLine());
                Employee empSalary = company.getEmployee(empSalaryID);
                if (empSalary == null) { 
                    System.out.println("Employee was not found."); 
                    return; 
                }

                int year = 0, month = 0;
                while (true) {
                    try {
                        System.out.print("Enter Year: ");
                        year = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter Month (1-12): ");
                        month = Integer.parseInt(scanner.nextLine());
                        if (month < 1 || month > 12) { 
                            System.out.println("Invalid month."); 
                            continue; 
                        }
                        break;
                    } catch (Exception e) { 
                        System.out.println("Invalid input."); 
                    }
                }

                int present = 0;
                for (AttendanceRecord attendance : empSalary.getAttendanceList()) {
                    if (attendance.getMonth() == month && attendance.getYear() == year 
                        && attendance.getStatus().equals("P")) {
                        present++;
                    }
                }
                Employee employee = company.getEmployee(empSalaryID);
                System.out.println("\nEmployee Name: " + employee.getName());
                double salary = present * empSalary.getSalaryPerDay();
                System.out.println("Monthly Salary for " + year + "-" + String.format("%02d", month) + " = " + salary);

            } catch (Exception e) { 
                System.out.println("There was an error in calculating the salary. Try again."); 
            }
        }

    private static void generateReport() {
        try {
            for (Employee employee : company.getEmployeeMap().values()) {
                System.out.println("\nEmployee Name: " + employee.getName());
                System.out.println("Employee ID: " + employee.getEmployeeID());

                int month = 0;
                int year = 0;
                int presentCount = 0;
                int absentCount = 0;
                int weekdayCount = 0;

                for (AttendanceRecord record : employee.getAttendanceList()) {

                    if (record.getYear() != year || record.getMonth() != month) {
                        if (year != 0) {
                            System.out.println("Month: " + year + "-" + (month < 10 ? "0" : "") + month);
                            System.out.println("Present = " + presentCount);
                            System.out.println("Absent = " + absentCount);
                            System.out.println("Weekdays = " + weekdayCount);
                        }
                        year = record.getYear();
                        month = record.getMonth();
                        presentCount = 0;
                        absentCount = 0;
                        weekdayCount = 0;
                    }

                    switch (record.getStatus()) {
                        case "P": presentCount++; break;
                        case "A": absentCount++; break;
                        case "W": weekdayCount++; break;
                    }
                }

                if (year != 0) {
                    System.out.println("Month: " + year + "-" + (month < 10 ? "0" : "") + month);
                    System.out.println("Present = " + presentCount);
                    System.out.println("Absent = " + absentCount);
                    System.out.println("Weekdays = " + weekdayCount);
                }
            }
        } catch (Exception e) {
            System.out.println("There was an error in generating the report. Try again.");
        }

    }
}
