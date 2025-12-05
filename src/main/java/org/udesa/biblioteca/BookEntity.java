package org.udesa.biblioteca;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class BookEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos del libro (Identity)
    private String author;
    private String name;
    private String genre;

    // El String que guarda qué subclase de 'Book' somos (Available/Rented)
    private String state;

    public BookEntity(String author, String name, String genre) {
        this.author = author;
        this.name = name;
        this.genre = genre;
        this.state = "Available"; // Estado inicial default
    }

    // --- DELEGACIÓN AL ESTADO (Igual que RoomEntity.status()) ---

    public Book status() {
        return Book.findStatus(this.state);
    }

    public BookEntity rent() {
        // Delega la lógica al objeto Book (State)
        return status().rent(this);
    }

    public BookEntity returnBook() {
        return status().returnBook(this);
    }
}
