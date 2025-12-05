package org.udesa.biblioteca;

import java.util.Objects;


public class Book {
    private String author;
    private String name;
    private String genre;

    public Book( String author, String name, String genre ) {
        this.author = author;
        this.name = name;
        this.genre = genre;
    }

    public int hashCode() {
        return Objects.hash( author, genre, name );
    }

    public boolean equals( Object obj ) {
        return obj != null &&
               ( this == obj ||
                 ( getClass() == obj.getClass() &&
                   ( (Book) obj ).author == author &&
                   ( (Book) obj ).genre == genre &&
                   ( (Book) obj ).name == name ) );
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

}
