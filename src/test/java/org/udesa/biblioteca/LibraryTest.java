package org.udesa.biblioteca;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LibraryTest {

    @Autowired private Library library;
    @Autowired private BookRepository repo;

    @Test public void testNewLibraryHasNoAuthors() {
        assertTrue( library.searchAvailableByAuthor( "Anonimous" ).isEmpty() );
    }

    @Test public void testNewLibraryHasNoGenres() {
        assertTrue( library.searchAvailableByGenre( "SiFy" ).isEmpty() );
    }

    @Test public void testNewLibraryHasNoStock() {
        assertEquals( 0, library.available() );
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
        Library lib = libraryWithJavaForDummies();
        assertEquals( 1, lib.available() );

        lib.removeBook( javaForDummies() );
        assertEquals( 0, lib.available() );
    }

    @Test public void testLibraryWhitNoBooktoRemove() {
        // Library ya está inyectada y limpia por @Transactional
        assertThrowsLike( Library.CannotRemove, () -> library.removeBook( javaForDummies() ) );
        assertEquals( 0, library.available() );
    }

    @Test public void testLibraryWhitTwiceABook() {
        Library lib = libraryWithJavaForDummies();

        assertThrowsLike( Library.BookAlreadyRegistered, () -> lib.addBook( javaForDummies() ) );
        assertEquals( 1, lib.available() );
    }

    @Test public void testFiltersAuthorsOnLibrary() {
        Library lib = fullLibrary();
        assertEquals( 3, lib.available() );
        assertOnBooks( lib.searchAvailableByAuthor( "Martin Fowler" ), true, false, true );
    }

    @Test public void testFiltersGenreOnLibrary() {
        Library lib = fullLibrary();
        assertEquals( 3, lib.available() );
        assertOnBooks( lib.searchAvailableByGenre( "IT" ), true, true, false );
    }

    private void assertOnBooks( List<BookInfo> it, boolean java, boolean python, boolean tolkien ) {
        assertEquals( java, it.contains( javaForDummies() ) );
        assertEquals( python, it.contains( pythonForDummies() ) );
        assertEquals( tolkien, it.contains( tolkienUniverse() ) );
    }

    @Test public void testSucessfulRent() {
        assertEquals( 0, libraryWithRentedJava().available() );
    }

    @Test public void testUnexistentRent() {
        assertThrowsLike( Library.BookUnavailable, () -> library.rentBook( javaForDummies() ) );
    }

    @Test public void testRentTwice() {
        assertThrowsLike( Library.BookUnavailable, () -> libraryWithRentedJava().rentBook( javaForDummies() ) );
    }

    @Test public void testRestoreRented() {
        Library lib = libraryWithRentedJava();
        lib.returnBook( javaForDummies() );
        assertEquals( 1, lib.available() );
    }

    @Test public void testRestoreUnrented() {
        Library lib = libraryWithJavaForDummies();
        assertThrowsLike( Library.BookNotRented, () -> lib.returnBook( javaForDummies() ) );
        assertEquals( 1, lib.available() );
    }

    @Test public void testLibraryWhitARentedBookRemoved() {
        Library lib = libraryWithRentedJava();


        lib.removeBook( javaForDummies() );

        assertThrowsLike( Library.BookNotRented, () -> lib.returnBook( javaForDummies() ) );
        assertEquals( 0, lib.available() );
    }

    // --- HELPERS ---

    public void assertThrowsLike( String msg, Executable codeToRun ) {
        assertEquals( msg, assertThrows( Exception.class, codeToRun ).getMessage() );
    }

    private Library fullLibrary() {
        Library lib = libraryWithJavaForDummies();
        lib.addBook( pythonForDummies() );
        lib.addBook( tolkienUniverse() );
        return lib;
    }

    private Library libraryWithRentedJava() {
        Library lib = libraryWithJavaForDummies();
        lib.rentBook( javaForDummies() );
        return lib;
    }

    private Library libraryWithJavaForDummies() {
        // Usamos la instancia inyectada 'this.library'
        library.addBook( javaForDummies() );
        return library;
    }

    // CREACIÓN DE DTOs (BookInfo)
    private BookInfo javaForDummies() {
        return new BookInfo( "Martin Fowler", "Java4Dummies", "IT" );
    }

    private BookInfo pythonForDummies() {
        return new BookInfo( "Chamond Liu", "pythonForDummies", "IT" );
    }

    private BookInfo tolkienUniverse() {
        return new BookInfo( "Martin Fowler", "TolkienUniverse", "SiFi" );
    }
}
