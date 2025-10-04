import java.util.HashMap;
import java.util.Map;

public class Library {
    private Map<String, Book> books;
    private Map<String, User> users;

    public Library() {
        books = new HashMap<>();
        users = new HashMap<>();
    }

    public void addBook(Book book) {
        books.put(book.getId(), book);
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public Book getBook(String bookId) {
        return books.get(bookId);
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public void borrowBook(String userId, String bookId) {
        Book book = books.get(bookId);
        User user = users.get(userId);

        if (book == null) {
            System.out.println("Book not found!");
        } else if (user == null) {
            System.out.println("User not found!");
        } else if (book.isBorrowed()) {
            System.out.println("Book is already borrowed!");
        } else {
            book.borrow();
            System.out.println(user.getName() + " borrowed " + book.getTitle());
        }
    }

    public void returnBook(String bookId) {
        Book book = books.get(bookId);

        if (book == null) {
            System.out.println("Book not found!");
        } else if (!book.isBorrowed()) {
            System.out.println("Book is not borrowed!");
        } else {
            book.returnBook();
            System.out.println(book.getTitle() + " has been returned.");
        }
    }

    public void listBooks() {
        books.values().forEach(System.out::println);
    }

    public void listUsers() {
        users.values().forEach(System.out::println);
    }
}
