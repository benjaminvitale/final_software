package org.udesa.biblioteca;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Library {
  public static String BookUnavailable = "book unavailable";
  public static String BookNotRented = "book not rented";
  public static String BookAlreadyRegistered = "book already registered";
  public static String CannotRemove = "cannot remove, book not registered";
  
  private List<Book> books;
  private List<Book> rented;

  public Library() {
    books = new ArrayList<>();
    rented = new ArrayList<>();
  }

  public void addBook( Book book ) {
    if ( existsInLibrary( book ) ) { 
      throw new RuntimeException( BookAlreadyRegistered );
    }
    books.add( book );
  }

  private boolean existsInLibrary( Book book ) {
    return books.contains( book ) || rented.contains( book );
  }
  
  public void rentBook( Book book ) {
    if (!books.contains( book )) { 
      throw new RuntimeException( BookUnavailable );
    }
    rented.add( book );
    books.remove( book );
  }

  public void returnBook( Book book ) {
    if (!rented.contains( book )) { 
      throw new RuntimeException( BookNotRented );
    }
    books.add( book );
    rented.remove( book );
  }

  public void removeBook( Book book ) {
    if ( ! existsInLibrary( book ) ) { 
      throw new RuntimeException( CannotRemove ); 
    }

    books.remove( book );
    rented.remove( book );
  }

  public List<Book> searchAvailableByGenre( String genre ) {
    return filter( (each) -> each.getGenre().equals( genre ) );
  }

  public List<Book> searchAvailableByAuthor( String author ) {
    return filter( (each) -> each.getAuthor().equals( author ) );
  }

  private List<Book> filter( Predicate<? super Book> predicate ) {
    return books.stream()
                .filter( predicate )
                .collect( Collectors.toList() );
  }

  public int available() {
    return books.size();
  }
}
