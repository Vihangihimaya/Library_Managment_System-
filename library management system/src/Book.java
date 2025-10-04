public class Book {
    private String id;
    private String title;
    private boolean isBorrowed;

    public Book(String id, String title) {
        this.id = id;
        this.title = title;
        this.isBorrowed = false;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void borrow() {
        isBorrowed = true;
    }

    public void returnBook() {
        isBorrowed = false;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", isBorrowed=" + isBorrowed +
                '}';
    }
}
