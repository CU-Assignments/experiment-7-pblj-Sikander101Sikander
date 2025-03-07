import java.sql.*;
import java.util.Scanner;

public class ProductDatabase {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/product";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Shivansh@123";

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in);
             Connection dbConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
             
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbConn.setAutoCommit(false);
            System.out.println("✔️ Connected to the database!");

            while (true) {
                System.out.println("\nChoose an operation:");
                System.out.println("1. Insert Product");
                System.out.println("2. View Products");
                System.out.println("3. Update Product Price");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = input.nextInt();

                switch (choice) {
                    case 1 -> insertProduct(dbConn, input);
                    case 2 -> viewProducts(dbConn);
                    case 3 -> updatePrice(dbConn, input);
                    case 4 -> deleteProduct(dbConn, input);
                    case 5 -> {
                        System.out.println(" Exiting program...");
                        return;
                    }
                    default -> System.out.println("❌ Invalid choice. Try again!");
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL JDBC Driver Not Found!");
        } catch (SQLException e) {
            System.out.println("❌ Database Connection Error!");
        }
    }

    private static void insertProduct(Connection dbConn, Scanner input) throws SQLException {
        try {
            System.out.print("Enter Product ID: ");
            int id = input.nextInt();
            input.nextLine(); // Consume newline

            System.out.print("Enter Product Name: ");
            String name = input.nextLine();

            System.out.print("Enter Price: ");
            double price = input.nextDouble();

            System.out.print("Enter Quantity: ");
            int qty = input.nextInt();

            String sql = "INSERT INTO Product (ProductID, ProductName, Price, Quantity) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.setString(2, name);
                stmt.setDouble(3, price);
                stmt.setInt(4, qty);
                stmt.executeUpdate();
                dbConn.commit();
                System.out.println("✔️ Product added successfully!");
            }
        } catch (SQLException e) {
            dbConn.rollback();
            System.out.println("❌ Error inserting product. Transaction rolled back!");
        }
    }

    private static void viewProducts(Connection dbConn) throws SQLException {
        String sql = "SELECT * FROM Product";
        try (Statement stmt = dbConn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nProduct Records:");
            while (rs.next()) {
                System.out.printf("ProductID: %d, Name: %s, Price: %.2f, Quantity: %d%n",
                        rs.getInt("ProductID"), rs.getString("ProductName"), rs.getDouble("Price"), rs.getInt("Quantity"));
            }
        }
    }

    private static void updatePrice(Connection dbConn, Scanner input) throws SQLException {
        try {
            System.out.print("Enter Product ID to Update: ");
            int id = input.nextInt();

            System.out.print("Enter New Price: ");
            double price = input.nextDouble();

            String sql = "UPDATE Product SET Price = ? WHERE ProductID = ?";
            try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
                stmt.setDouble(1, price);
                stmt.setInt(2, id);
                int rows = stmt.executeUpdate();
                dbConn.commit();
                System.out.println(rows > 0 ? "✔️ Price updated successfully!" : "❌ Product not found!");
            }
        } catch (SQLException e) {
            dbConn.rollback();
            System.out.println("❌ Error updating product. Transaction rolled back!");
        }
    }

    private static void deleteProduct(Connection dbConn, Scanner input) throws SQLException {
        try {
            System.out.print("Enter Product ID to Delete: ");
            int id = input.nextInt();

            String sql = "DELETE FROM Product WHERE ProductID = ?";
            try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int rows = stmt.executeUpdate();
                dbConn.commit();
                System.out.println(rows > 0 ? "✔️ Product deleted successfully!" : "❌ Product not found!");
            }
        } catch (SQLException e) {
            dbConn.rollback();
            System.out.println("❌ Error deleting product. Transaction rolled back!");
        }
    }
}
