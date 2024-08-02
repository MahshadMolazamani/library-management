package com.library.book.dto;


import java.time.LocalDate;

public record LoanDTO(
        Long id,
        LocalDate lendDate,
        LocalDate returnDate,
        String username,
        Long bookId
) {
}
