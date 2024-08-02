package com.library.book.dto;

import java.util.List;


public record MemberDTO(
        Long id,
        String username,
        String email,
        String address,
        String phoneNumber,
        List<LoanDTO> loans
) {

}
