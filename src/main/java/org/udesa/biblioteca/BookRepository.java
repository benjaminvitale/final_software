package org.udesa.biblioteca;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    Optional<BookEntity> findByAuthorAndNameAndGenre(String author, String name, String genre);

    // Métodos para soportar las búsquedas de Library
    List<BookEntity> findByGenreAndState(String genre, String state);
    List<BookEntity> findByAuthorAndState(String author, String state);
    long countByState(String state);
}
