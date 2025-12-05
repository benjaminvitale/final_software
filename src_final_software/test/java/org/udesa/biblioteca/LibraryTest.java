package org.udesa.biblioteca;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class LibraryTest {

  @Test public void testNewLibraryHasNoAuthors() {
    assertTrue( new Library().searchAvailableByAuthor( "Anonimous" ).isEmpty() );
  }

  @Test public void testNewLibraryHasNoGenres() {
    assertTrue( new Library().searchAvailableByGenre( "SiFy" ).isEmpty() );
  }

  @Test public void testNewLibraryHasNoStock() {
    assertEquals( 0, new Library().available() );
  }

  @Test public void testLibraryWhitABookHasStock() {
    assertEquals( 1, libraryWithJavaForDummies().available() );
  }

  @Test public void testLibraryWhitABookFindsItsGenre() {
    assertTrue( libraryWithJavaForDummies()
                  .searchAvailableByGenre( javaForDummies().getGenre() )
                  .contains( javaForDummies() ) );
  }

  @Test public void testLibraryWhitABookFindsItsAuthor() {
    assertTrue( libraryWithJavaForDummies()
                  .searchAvailableByAuthor( javaForDummies().getAuthor() )
                  .contains( javaForDummies() ) );
  }

  @Test public void testLibraryWhitABookRemoved() {
    Library library = libraryWithJavaForDummies();
    assertEquals( 1, library.available() );

    library.removeBook( javaForDummies() );
    assertEquals( 0, library.available() );
  }

  @Test public void testLibraryWhitNoBooktoRemove() {
    Library library = new Library();

    assertThrowsLike( Library.CannotRemove, () -> library.removeBook( javaForDummies() ) );
  
    assertEquals( 0, library.available() );
  }


  @Test public void testLibraryWhitTwiceABook() {
    Library library = libraryWithJavaForDummies();
        
    assertThrowsLike( Library.BookAlreadyRegistered, () -> library.addBook( javaForDummies() ) );
    assertEquals( 1, library.available() );
  }

  @Test public void testFiltersAuthorsOnLibrary() {
    Library library = fullLibrary();

    assertEquals( 3, library.available() );
    assertOnBooks( library.searchAvailableByAuthor( "Martin Fowler" ), true, false, true );
  }

  @Test public void testFiltersGenreOnLibrary() {
    Library library = fullLibrary();

    assertEquals( 3, library.available() );
    assertOnBooks( library.searchAvailableByGenre( "IT" ), true, true, false );
  }

  private void assertOnBooks( List<Book> it, boolean java, boolean python, boolean tolkien ) {
    assertEquals( java, it.contains( javaForDummies() ) );
    assertEquals( python, it.contains( pythonForDummies() ) );
    assertEquals( tolkien, it.contains( tolkienUniverse() ) );
  }

  @Test public void testSucessfulRent() {
    assertEquals( 0, libraryWithRentedJava().available() );
  }

  @Test public void testUnexistentRent() {
    assertThrowsLike( Library.BookUnavailable, () -> new Library().rentBook( javaForDummies() ) );
  }

  @Test public void testRentTwice() {
    assertThrowsLike( Library.BookUnavailable, () -> libraryWithRentedJava().rentBook( javaForDummies() ) );
  }

  @Test public void testRestoreRented() {
    Library library = libraryWithRentedJava();

    library.returnBook( javaForDummies() );
      
    assertEquals( 1, library.available() );
  }


  @Test public void testRestoreUnrented() {
    Library library = libraryWithJavaForDummies();

    assertThrowsLike( Library.BookNotRented, () -> library.returnBook( javaForDummies() ) );
    
    assertEquals( 1, library.available() );
  }

  @Test public void testLibraryWhitARentedBookRemoved() {
    Library library = libraryWithRentedJava();
 
    library.removeBook( javaForDummies() );

    assertThrowsLike( Library.BookNotRented, () -> library.returnBook( javaForDummies() ) );
    assertEquals( 0, library.available() );
  }

  public void assertThrowsLike( String msg, Executable codeToRun ) {
    assertEquals( msg, assertThrows( Exception.class, codeToRun ).getMessage() );
  }

  private Library fullLibrary() {
    Library library = libraryWithJavaForDummies();
    library.addBook( pythonForDummies() );
    library.addBook( tolkienUniverse() );
    return library;
  }
 
  private Library libraryWithRentedJava() {
    Library library = libraryWithJavaForDummies();
    library.rentBook( javaForDummies() );
    return library;
  }

  private Library libraryWithJavaForDummies() {
    Library library = new Library();
    library.addBook( javaForDummies() );
    return library;
  }

  private Book javaForDummies() {
    return new Book( "Martin Fowler", "Java4Dummies", "IT" );
  }

  private Book pythonForDummies() {
    return new Book( "Chamond Liu", "pythonForDummies", "IT" );
  }

  private Book tolkienUniverse() {
    return new Book( "Martin Fowler", "TolkienUniverse", "SiFi" );
  }

}
