package com.library.book.dto;

import java.time.LocalDate;
import java.util.List;


public record AuthorDTO(
        Long id,
        String name,
        LocalDate dateOfBirth,
        List<BookDTO> books
) {
}
