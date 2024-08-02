package com.library.book.service;

import com.library.book.dto.AuthorDTO;
import com.library.book.entity.Author;
import com.library.book.mapper.AuthorMapper;
import com.library.book.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

    private AuthorRepository authorRepository;
    private AuthorMapper authorMapper;
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorRepository = mock(AuthorRepository.class);
        authorMapper = AuthorMapper.INSTANCE;
        authorService = new AuthorService(authorRepository);
    }

    @Test
    void createAuthor() {
        AuthorDTO authorDTO = new AuthorDTO(null, "Author Name", LocalDate.of(1990, 1, 1), List.of());
        Author author = authorMapper.toAuthor(authorDTO);

        when(authorRepository.save(any(Author.class))).thenReturn(author);

        Optional<AuthorDTO> savedAuthor = authorService.create(authorDTO);
        assertTrue(savedAuthor.isPresent());
        assertEquals(savedAuthor.get().name(), authorDTO.name());
    }

    @Test
    void getAllAuthors() {
        Author author1 = new Author();
        author1.setName("Author Name");

        Author author2 = new Author();
        author2.setName("Author Name 2");

        when(authorRepository.findAll()).thenReturn(List.of(author1, author2));

        List<AuthorDTO> authors = authorService.getAll();
        assertEquals(2, authors.size());
    }

    @Test
    void getAuthorById() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Author Name");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        Optional<AuthorDTO> foundAuthor = authorService.getById(1L);
        assertTrue(foundAuthor.isPresent());
        assertEquals("Author Name", foundAuthor.get().name());
    }

    @Test
    void updateAuthor() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Old Name");

        AuthorDTO authorDTO = new AuthorDTO(1L, "New Name", LocalDate.of(1990, 1, 1), List.of());

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        Optional<AuthorDTO> updatedAuthor = authorService.update(1L, authorDTO);
        assertTrue(updatedAuthor.isPresent());
        assertEquals("New Name", updatedAuthor.get().name());
    }

    @Test
    void deleteAuthor() {
        authorService.delete(1L);
        verify(authorRepository, times(1)).deleteById(1L);
    }
}