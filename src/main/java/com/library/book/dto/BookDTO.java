package com.library.book.dto;

import java.math.BigDecimal;

public record BookDTO(
        Long id,
        String title,
        String genre,
        BigDecimal price,
        AuthorDTO author
) {
}