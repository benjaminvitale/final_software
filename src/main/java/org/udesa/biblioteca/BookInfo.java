package org.udesa.biblioteca;

import java.util.Objects;

public class BookInfo {
    private String author;
    private String name;
    private String genre;

    public BookInfo(String author, String name, String genre) {
        this.author = author;
        this.name = name;
        this.genre = genre;
    }

    public String getAuthor() { return author; }
    public String getName() { return name; }
    public String getGenre() { return genre; }

    @Override
    public int hashCode() { return Objects.hash(author, genre, name); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BookInfo other = (BookInfo) obj;
        return Objects.equals(author, other.author) &&
                Objects.equals(genre, other.genre) &&
                Objects.equals(name, other.name);
    }
}
