package com.library.book.mapper;

import com.library.book.dto.AuthorDTO;
import com.library.book.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AuthorMapper {
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    @Mapping(source = "books", target = "books")
    AuthorDTO toAuthorDTO(Author author);
    Author toAuthor(AuthorDTO authorDTO);
    List<AuthorDTO> toAuthorDTOs(List<Author> authors);
    List<Author> toAuthors(List<AuthorDTO> authorDTOs);
}
