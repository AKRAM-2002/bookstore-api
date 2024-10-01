package com.example.bookstoreapi.service;

import com.example.bookstoreapi.model.Book;
import com.example.bookstoreapi.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setIsbn("1234567890");
        testBook.setGenre("Test Genre");
        testBook.setPublicationYear(Year.of(2024));
    }

    @Test
    void whenGetAllBooks_thenBooksListShouldBeReturned() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(testBook));
        
        List<Book> found = bookService.getAllBooks();
        
        assertNotNull(found);
        assertEquals(1, found.size());
        assertEquals(testBook.getTitle(), found.get(0).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void whenGetBookById_thenBookShouldBeReturned() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        
        Optional<Book> found = bookService.getBookById(1L);
        
        assertTrue(found.isPresent());
        assertEquals(testBook.getTitle(), found.get().getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void whenSaveBook_thenBookShouldBeSaved() {
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        
        Book saved = bookService.saveBook(testBook);
        
        assertNotNull(saved);
        assertEquals(testBook.getTitle(), saved.getTitle());
        verify(bookRepository, times(1)).save(testBook);
    }
}