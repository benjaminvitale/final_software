package org.udesa.biblioteca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class Library {

    // Constantes de Error (Delegadas a la clase Book)
    public static final String BookUnavailable = Book.BookUnavailable;
    public static final String BookNotRented = Book.BookNotRented;
    public static final String BookAlreadyRegistered = "book already registered";
    public static final String CannotRemove = "cannot remove, book not registered";

    @Autowired private BookService bookService;

    public void addBook(BookInfo info) {
        // Validación estricta: Si existe, error.
        bookService.find(info.getAuthor(), info.getName(), info.getGenre(),
                () -> {
                    return null;
                }
        );


        boolean exists = bookService.find(info.getAuthor(), info.getName(), info.getGenre(), () -> null) != null;

        if (exists) throw new RuntimeException(BookAlreadyRegistered);

        bookService.save(new BookEntity(info.getAuthor(), info.getName(), info.getGenre()));
    }

    public void rentBook(BookInfo info) {
        // Busca entidad. Si no existe -> Error Unavailable
        BookEntity entity = getEntityOrThrow(info, BookUnavailable);

        // Delega lógica al estado
        bookService.save(entity.rent());
    }

    public void returnBook(BookInfo info) {
        BookEntity entity = getEntityOrThrow(info, BookNotRented);
        bookService.save(entity.returnBook());
    }

    public void removeBook(BookInfo info) {
        BookEntity entity = getEntityOrThrow(info, CannotRemove);
        bookService.delete(entity);
    }

    // --- CONSULTAS ---

    public int available() {
        return (int) bookService.countAvailable();
    }

    public List<BookInfo> searchAvailableByGenre(String genre) {
        return toInfos(bookService.findAvailableByGenre(genre));
    }

    public List<BookInfo> searchAvailableByAuthor(String author) {
        return toInfos(bookService.findAvailableByAuthor(author));
    }

    // --- HELPERS ---

    private BookEntity getEntityOrThrow(BookInfo info, String errorMsg) {
        return bookService.find(info.getAuthor(), info.getName(), info.getGenre(),
                () -> { throw new RuntimeException(errorMsg); }
        );
    }

    private List<BookInfo> toInfos(List<BookEntity> entities) {
        return entities.stream()
                .map(e -> new BookInfo(e.getAuthor(), e.getName(), e.getGenre()))
                .collect(Collectors.toList());
    }
}