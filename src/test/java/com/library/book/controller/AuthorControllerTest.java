package com.library.book.controller;

import com.library.book.dto.AuthorDTO;
import com.library.book.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        authorDTO = new AuthorDTO(null, "Author Name", LocalDate.of(1990, 1, 1), List.of());
    }

    @Test
    void createAuthor() throws Exception {
        when(authorService.create(any(AuthorDTO.class))).thenReturn(Optional.of(authorDTO));

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Author Name\",\"dateOfBirth\":\"1990-01-01\",\"books\":[]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Author Name"));
    }

    @Test
    void getAllAuthors() throws Exception {
        when(authorService.getAll()).thenReturn(List.of(authorDTO));

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Author Name"));
    }

    @Test
    void getAuthorById() throws Exception {
        when(authorService.getById(anyLong())).thenReturn(Optional.of(authorDTO));

        mockMvc.perform(get("/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Author Name"));
    }

    @Test
    void updateAuthor() throws Exception {

        AuthorDTO updatedAuthor = new AuthorDTO(1L, "Updated Name", LocalDate.of(1990, 1, 1), List.of());
        when(authorService.update(eq(1L), any(AuthorDTO.class))).thenReturn(Optional.of(updatedAuthor));
        mockMvc.perform(put("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Name\",\"dateOfBirth\":\"1990-01-01\",\"books\":[]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.dateOfBirth").value("1990-01-01"))
                .andExpect(jsonPath("$.books").isEmpty());

    }

    @Test
    void deleteAuthor() throws Exception {
        mockMvc.perform(delete("/authors/1"))
                .andExpect(status().isNoContent());
    }
}

