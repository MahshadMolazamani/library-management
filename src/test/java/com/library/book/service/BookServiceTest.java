package com.library.book.service;

import com.library.book.dto.BookDTO;
import com.library.book.entity.Book;
import com.library.book.mapper.BookMapper;
import com.library.book.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private BookRepository bookRepository;
    private BookMapper bookMapper;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        bookMapper = BookMapper.INSTANCE;
        bookService = new BookService(bookRepository);
    }

    @Test
    void createBook() {
        BookDTO bookDTO = new BookDTO(null, "Book Title", "Genre", new BigDecimal("29.99"), null);
        Book book = bookMapper.toBook(bookDTO);

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Optional<BookDTO> savedBook = bookService.create(bookDTO);
        assertTrue(savedBook.isPresent());
        assertEquals(savedBook.get().title(), bookDTO.title());
    }

    @Test
    void getAllBooks() {
        Book book1 = new Book();
        book1.setTitle("Book Title 1");

        Book book2 = new Book();
        book2.setTitle("Book Title 2");

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        List<BookDTO> books = bookService.getAll();
        assertEquals(2, books.size());
    }

    @Test
    void getBookById() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book Title");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<BookDTO> foundBook = bookService.getById(1L);
        assertTrue(foundBook.isPresent());
        assertEquals("Book Title", foundBook.get().title());
    }

    @Test
    void updateBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Old Title");

        BookDTO bookDTO = new BookDTO(1L, "New Title", "Genre", new BigDecimal("29.99"), null);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Optional<BookDTO> updatedBook = bookService.update(1L, bookDTO);
        assertTrue(updatedBook.isPresent());
        assertEquals("New Title", updatedBook.get().title());
    }

    @Test
    void deleteBook() {
        bookService.delete(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }
}
