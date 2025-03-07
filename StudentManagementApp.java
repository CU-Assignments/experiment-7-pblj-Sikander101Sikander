import java.sql.*;
import java.util.Scanner;

public class StudentManagementApp {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/college";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Shivansh@123";

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in);
             Connection dbConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
             
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✔️ Connected to the database!");

            while (true) {
                System.out.println("\nStudent Management System:");
                System.out.println("1. Add Student");
                System.out.println("2. View Students");
                System.out.println("3. Update Marks");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int option = input.nextInt();

                switch (option) {
                    case 1 -> addStudent(dbConn, input);
                    case 2 -> viewStudents(dbConn);
                    case 3 -> updateMarks(dbConn, input);
                    case 4 -> deleteStudent(dbConn, input);
                    case 5 -> {
                        System.out.println(" Exiting... Thank you!");
                        return;
                    }
                    default -> System.out.println("❌ Invalid choice! Try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Database Error!");
        }
    }

    private static void addStudent(Connection dbConn, Scanner input) throws SQLException {
        System.out.print("Enter Student ID: ");
        int id = input.nextInt();
        input.nextLine(); // Consume newline

        System.out.print("Enter Name: ");
        String name = input.nextLine();

        System.out.print("Enter Department: ");
        String dept = input.nextLine();

        System.out.print("Enter Marks: ");
        double marks = input.nextDouble();

        String sql = "INSERT INTO students (StudentID, Name, Department, Marks) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setString(3, dept);
            stmt.setDouble(4, marks);
            stmt.executeUpdate();
            System.out.println("✔️ Student added successfully!");
        }
    }

    private static void viewStudents(Connection dbConn) throws SQLException {
        String sql = "SELECT * FROM students";
        try (Statement stmt = dbConn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nStudent List:");
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Dept: %s, Marks: %.2f%n",
                        rs.getInt("StudentID"), rs.getString("Name"), rs.getString("Department"), rs.getDouble("Marks"));
            }
        }
    }

    private static void updateMarks(Connection dbConn, Scanner input) throws SQLException {
        System.out.print("Enter Student ID to update marks: ");
        int id = input.nextInt();

        System.out.print("Enter new Marks: ");
        double marks = input.nextDouble();

        String sql = "UPDATE students SET Marks = ? WHERE StudentID = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setDouble(1, marks);
            stmt.setInt(2, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "✔️ Marks updated successfully!" : "❌ Student not found!");
        }
    }

    private static void deleteStudent(Connection dbConn, Scanner input) throws SQLException {
        System.out.print("Enter Student ID to delete: ");
        int id = input.nextInt();

        String sql = "DELETE FROM students WHERE StudentID = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "✔️ Student deleted successfully!" : "❌ Student not found!");
        }
    }
}
