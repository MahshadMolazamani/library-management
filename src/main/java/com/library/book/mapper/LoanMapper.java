package com.library.book.mapper;

import com.library.book.dto.LoanDTO;
import com.library.book.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoanMapper {

    LoanMapper INSTANCE = Mappers.getMapper(LoanMapper.class);

    @Mappings({
            @Mapping(source = "member.username", target = "username"),
            @Mapping(source = "book.id", target = "bookId")
    })
    LoanDTO toLoanDTO(Loan loan);

    @Mappings({
            @Mapping(target = "member.username", source = "username"),
            @Mapping(target = "book.id", source = "bookId")
    })
    Loan toLoan(LoanDTO loanDTO);

}
