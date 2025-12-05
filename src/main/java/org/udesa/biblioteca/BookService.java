package org.udesa.biblioteca;

import java.util.function.Supplier;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    @Autowired private BookRepository repo;

    @Transactional(readOnly = true)
    public BookEntity find(String author, String name, String genre, Supplier<BookEntity> supplier) {
        return repo.findByAuthorAndNameAndGenre(author, name, genre)
                .orElseGet(supplier);
    }

    public BookEntity save(BookEntity entity) {
        return repo.save(entity);
    }

    public void delete(BookEntity entity) {
        repo.delete(entity);
    }

    // Métodos pasamanos para las búsquedas
    public long countAvailable() {
        return repo.countByState("Available");
    }

    public List<BookEntity> findAvailableByGenre(String genre) {
        return repo.findByGenreAndState(genre, "Available");
    }

    public List<BookEntity> findAvailableByAuthor(String author) {
        return repo.findByAuthorAndState(author, "Available");
    }
}