import java.sql.*;
import java.util.Scanner;

public class EmployeeApp {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/company";
    private static final String DB_USER = "root"; // Change if needed
    private static final String DB_PASSWORD = "Shivansh@123"; 

    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            Connection dbConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✅ Connected to MySQL Database!");

            while (true) {
                System.out.println("\nChoose an operation:");
                System.out.println("1. Insert Employee");
                System.out.println("2. View Employees");
                System.out.println("3. Update Employee Salary");
                System.out.println("4. Delete Employee");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int userChoice = inputScanner.nextInt();

                switch (userChoice) {
                    case 1:
                        addEmployee(dbConnection, inputScanner);
                        break;
                    case 2:
                        displayEmployees(dbConnection);
                        break;
                    case 3:
                        modifyEmployeeSalary(dbConnection, inputScanner);
                        break;
                    case 4:
                        removeEmployee(dbConnection, inputScanner);
                        break;
                    case 5:
                        System.out.println(" Exiting program...");
                        dbConnection.close();
                        inputScanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println(" Invalid choice. Try again!");
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println(" MySQL JDBC Driver Not Found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Database Connection Error!");
            e.printStackTrace();
        }
    }

    private static void addEmployee(Connection dbConnection, Scanner inputScanner) throws SQLException {
        System.out.print("Enter Employee ID: ");
        int employeeID = inputScanner.nextInt();
        inputScanner.nextLine(); // Consume newline

        System.out.print("Enter Employee Name: ");
        String employeeName = inputScanner.nextLine();

        System.out.print("Enter Employee Salary: ");
        double employeeSalary = inputScanner.nextDouble();

        String sqlQuery = "INSERT INTO Employee (EmpID, Name, Salary) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlQuery);
        preparedStatement.setInt(1, employeeID);
        preparedStatement.setString(2, employeeName);
        preparedStatement.setDouble(3, employeeSalary);

        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows > 0) {
            System.out.println(" Employee added successfully!");
        }
    }

    private static void displayEmployees(Connection dbConnection) throws SQLException {
        String sqlQuery = "SELECT * FROM Employee";
        Statement statement = dbConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlQuery);

        System.out.println("\nEmployee Records:");
        while (resultSet.next()) {
            System.out.println("EmpID: " + resultSet.getInt("EmpID") + 
                               ", Name: " + resultSet.getString("Name") +
                               ", Salary: " + resultSet.getDouble("Salary"));
        }
    }

    private static void modifyEmployeeSalary(Connection dbConnection, Scanner inputScanner) throws SQLException {
        System.out.print("Enter Employee ID to Update: ");
        int employeeID = inputScanner.nextInt();

        System.out.print("Enter New Salary: ");
        double updatedSalary = inputScanner.nextDouble();

        String sqlQuery = "UPDATE Employee SET Salary = ? WHERE EmpID = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlQuery);
        preparedStatement.setDouble(1, updatedSalary);
        preparedStatement.setInt(2, employeeID);

        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows > 0) {
            System.out.println("Salary updated successfully!");
        } else {
            System.out.println(" Employee not found!");
        }
    }

    private static void removeEmployee(Connection dbConnection, Scanner inputScanner) throws SQLException {
        System.out.print("Enter Employee ID to Delete: ");
        int employeeID = inputScanner.nextInt();

        String sqlQuery = "DELETE FROM Employee WHERE EmpID = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlQuery);
        preparedStatement.setInt(1, employeeID);

        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows > 0) {
            System.out.println(" Employee deleted successfully!");
        } else {
            System.out.println(" Employee not found!");
        }
    }
}
