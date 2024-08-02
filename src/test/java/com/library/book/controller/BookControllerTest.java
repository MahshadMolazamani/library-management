package com.library.book.controller;

import com.library.book.dto.BookDTO;
import com.library.book.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        bookDTO = new BookDTO(null, "Book Title", "Genre", new BigDecimal("29.99"), null);
    }

    @Test
    void createBook() throws Exception {
        when(bookService.create(any(BookDTO.class))).thenReturn(Optional.of(bookDTO));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Book Title\",\"genre\":\"Genre\",\"price\":29.99,\"author\":null}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Book Title"));
    }

    @Test
    void getAllBooks() throws Exception {
        when(bookService.getAll()).thenReturn(List.of(bookDTO));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Book Title"));
    }

    @Test
    void getBookById() throws Exception {
        when(bookService.getById(anyLong())).thenReturn(Optional.of(bookDTO));

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Book Title"));
    }

    @Test
    void updateBook() throws Exception {
        when(bookService.update(anyLong(), any(BookDTO.class))).thenReturn(Optional.of(new BookDTO(1L, "Updated Title", "Genre", new BigDecimal("29.99"), null)));

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\",\"genre\":\"Genre\",\"price\":29.99,\"author\":null}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void deleteBook() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());
    }
}
