package com.library.book.controller;

import com.library.book.dto.LoanDTO;
import com.library.book.service.LoanService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    private LoanDTO loanDTO;

    @BeforeEach
    void setUp() {
        loanDTO = new LoanDTO(null, LocalDate.now(), LocalDate.now().plusDays(14), "username", 1L);
    }

    @Test
    void createLoan() throws Exception {
        when(loanService.create(any(LoanDTO.class))).thenReturn(Optional.of(loanDTO));

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lendDate\":\"2024-01-01\",\"returnDate\":\"2024-01-15\",\"username\":\"username\",\"bookId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    void getAllLoans() throws Exception {
        when(loanService.getAll()).thenReturn(List.of(loanDTO));

        mockMvc.perform(get("/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("username"));
    }

    @Test
    void getLoanById() throws Exception {
        when(loanService.getById(anyLong())).thenReturn(Optional.of(loanDTO));

        mockMvc.perform(get("/loans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    void updateLoan() throws Exception {
        LoanDTO updatedLoanDTO = new LoanDTO(1L, LocalDate.now(), LocalDate.now().plusDays(14), "updatedUsername", 1L);
        when(loanService.update(anyLong(), any(LoanDTO.class))).thenReturn(Optional.of(updatedLoanDTO));

        mockMvc.perform(put("/loans/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lendDate\":\"2024-01-01\",\"returnDate\":\"2024-01-15\",\"username\":\"updatedUsername\",\"bookId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedUsername"));
    }

    @Test
    void deleteLoan() throws Exception {
        mockMvc.perform(delete("/loans/1"))
                .andExpect(status().isNoContent());
    }
}
