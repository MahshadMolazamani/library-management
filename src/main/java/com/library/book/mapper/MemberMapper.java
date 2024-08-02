package com.library.book.mapper;

import com.library.book.dto.MemberDTO;
import com.library.book.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = LoanMapper.class)
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Mapping(target = "loans", source = "loans")
    MemberDTO toMemberDTO(Member member);

    @Mapping(target = "loans", source = "loans")
    Member toMember(MemberDTO memberDTO);
}
