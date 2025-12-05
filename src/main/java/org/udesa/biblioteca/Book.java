package org.udesa.biblioteca;

import java.util.List;
import lombok.SneakyThrows;

public abstract class Book {


    public static final String BookUnavailable = "book unavailable";
    public static final String BookNotRented = "book not rented";


    @SneakyThrows
    public static Book findStatus(String status) {
        String target = status == null ? "Available" : status;

        return List.of(Available.class, Rented.class).stream()
                .filter(clazz -> clazz.getSimpleName().equals(target))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Estado DB corrupto: " + status))
                .getDeclaredConstructor().newInstance();
    }

    public Book() {}


    public abstract BookEntity rent(BookEntity entity);
    public abstract BookEntity returnBook(BookEntity entity);


    protected BookEntity applyToEntity(BookEntity entity) {
        entity.setState(this.getClass().getSimpleName());
        return entity;
    }
}

// --- ESTADOS CONCRETOS ---

class Available extends Book {
    @Override
    public BookEntity rent(BookEntity entity) {
        return new Rented().applyToEntity(entity); // Transición a Rented
    }

    @Override
    public BookEntity returnBook(BookEntity entity) {
        throw new RuntimeException(Book.BookNotRented); // No se puede devolver lo que no se prestó
    }
}

class Rented extends Book {
    @Override
    public BookEntity rent(BookEntity entity) {
        throw new RuntimeException(Book.BookUnavailable); // Ya está prestado
    }

    @Override
    public BookEntity returnBook(BookEntity entity) {
        return new Available().applyToEntity(entity); // Transición a Available
    }
}