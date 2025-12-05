package org.udesa.biblioteca;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    // Helper para crear entidades rápidas
    private BookEntity newEntity(String title) {
        return new BookEntity("Author", title, "Genre");
    }

    @Test
    public void testSaveAndFind() {
        // 1. Guardamos usando el servicio
        BookEntity saved = bookService.save(newEntity("JPA for Dummies"));
        assertNotNull(saved.getId()); // Verifica que se generó ID (Persistencia real)

        // 2. Buscamos usando el supplier (lógica del servicio)
        BookEntity found = bookService.find("Author", "JPA for Dummies", "Genre",
                () -> { throw new RuntimeException("Should be found!"); });

        assertEquals(saved.getId(), found.getId());
    }

    @Test
    public void testFindThrowsExceptionWhenMissing() {
        // Probamos que el supplier del servicio funcione
        assertThrows(RuntimeException.class, () -> {
            bookService.find("Ghost", "Ghost", "Ghost",
                    () -> { throw new RuntimeException("Not Found Error"); });
        });
    }

    @Test
    public void testCountAvailable() {
        // Guardamos 2 libros default (Available)
        bookService.save(newEntity("Book 1"));
        bookService.save(newEntity("Book 2"));

        assertEquals(2, bookService.countAvailable());
    }

    @Test
    public void testFindAvailableByGenre() {
        BookEntity b1 = newEntity("Java Intro");
        b1.setGenre("Tech");
        bookService.save(b1);

        BookEntity b2 = newEntity("Python Intro");
        b2.setGenre("Tech");
        bookService.save(b2);

        BookEntity b3 = newEntity("History of Rome");
        b3.setGenre("History");
        bookService.save(b3);

        // Filtramos por Tech
        List<BookEntity> techBooks = bookService.findAvailableByGenre("Tech");

        assertEquals(2, techBooks.size());
        assertTrue(techBooks.stream().anyMatch(b -> b.getName().equals("Java Intro")));
        assertTrue(techBooks.stream().noneMatch(b -> b.getGenre().equals("History")));
    }

    @Test
    public void testDelete() {
        BookEntity book = bookService.save(newEntity("To Delete"));
        Long id = book.getId();

        bookService.delete(book);

        // Verificamos que ya no esté
        // Usamos el find con supplier que devuelve null para verificar
        BookEntity result = bookService.find("Author", "To Delete", "Genre", () -> null);
        assertNull(result);
    }
}
