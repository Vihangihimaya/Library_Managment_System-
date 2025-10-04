import java.sql.*;
import java.util.Scanner;

public class LibraryManagementSystem {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Create tables
            String createBooksTable = "CREATE TABLE IF NOT EXISTS books (" +
                    "book_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "title VARCHAR(255) NOT NULL, " +
                    "author VARCHAR(255) NOT NULL, " +
                    "publisher VARCHAR(255), " +
                    "year_published INT" +
                    ")";
            String createMembersTable = "CREATE TABLE IF NOT EXISTS members (" +
                    "member_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "email VARCHAR(255) UNIQUE NOT NULL, " +
                    "phone VARCHAR(20)" +
                    ")";
            String createLoansTable = "CREATE TABLE IF NOT EXISTS loans (" +
                    "loan_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "book_id INT, " +
                    "member_id INT, " +
                    "loan_date DATE, " +
                    "return_date DATE, " +
                    "FOREIGN KEY (book_id) REFERENCES books(book_id), " +
                    "FOREIGN KEY (member_id) REFERENCES members(member_id)" +
                    ")";

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createBooksTable);
                statement.executeUpdate(createMembersTable);
                statement.executeUpdate(createLoansTable);
                System.out.println("Tables created successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Add User");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. List Books");
            System.out.println("6. List Users");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    addUser();
                    break;
                case 3:
                    borrowBook();
                    break;
                case 4:
                    returnBook();
                    break;
                case 5:
                    listBooks();
                    break;
                case 6:
                    listUsers();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void addBook() {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        System.out.print("Enter book publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter year published: ");
        int yearPublished = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        try (Connection connection = DatabaseConnection.getConnection()) {
            String insertBookSql = "INSERT INTO books (title, author, publisher, year_published) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertBookSql)) {
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, author);
                preparedStatement.setString(3, publisher);
                preparedStatement.setInt(4, yearPublished);
                preparedStatement.executeUpdate();
                System.out.println("Book added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addUser() {
        System.out.print("Enter user name: ");
        String name = scanner.nextLine();
        System.out.print("Enter user email: ");
        String email = scanner.nextLine();
        System.out.print("Enter user phone: ");
        String phone = scanner.nextLine();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String insertUserSql = "INSERT INTO members (name, email, phone) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserSql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, phone);
                preparedStatement.executeUpdate();
                System.out.println("User added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void borrowBook() {
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        System.out.print("Enter book ID: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        try (Connection connection = DatabaseConnection.getConnection()) {
            String borrowBookSql = "INSERT INTO loans (book_id, member_id, loan_date) VALUES (?, ?, CURDATE())";
            try (PreparedStatement preparedStatement = connection.prepareStatement(borrowBookSql)) {
                preparedStatement.setInt(1, bookId);
                preparedStatement.setInt(2, userId);
                preparedStatement.executeUpdate();
                System.out.println("Book borrowed successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void returnBook() {
        System.out.print("Enter book ID: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        try (Connection connection = DatabaseConnection.getConnection()) {
            String returnBookSql = "UPDATE loans SET return_date = CURDATE() WHERE book_id = ? AND return_date IS NULL";
            try (PreparedStatement preparedStatement = connection.prepareStatement(returnBookSql)) {
                preparedStatement.setInt(1, bookId);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Book returned successfully.");
                } else {
                    System.out.println("Book return failed. Make sure the book ID is correct and the book is currently borrowed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listBooks() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String listBooksSql = "SELECT * FROM books";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(listBooksSql)) {
                System.out.println("Books in library:");
                while (resultSet.next()) {
                    int id = resultSet.getInt("book_id");
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    String publisher = resultSet.getString("publisher");
                    int yearPublished = resultSet.getInt("year_published");
                    System.out.println(id + ": " + title + " by " + author + " (" + publisher + ", " + yearPublished + ")");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listUsers() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String listUsersSql = "SELECT * FROM members";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(listUsersSql)) {
                System.out.println("Users in library:");
                while (resultSet.next()) {
                    int id = resultSet.getInt("member_id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String phone = resultSet.getString("phone");
                    System.out.println(id + ": " + name + " (" + email + ", " + phone + ")");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
