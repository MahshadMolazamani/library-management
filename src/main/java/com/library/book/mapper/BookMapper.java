package com.library.book.mapper;

import com.library.book.dto.BookDTO;
import com.library.book.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(source = "author", target = "author")
    BookDTO toBookDTO(Book book);

    @Mapping(source = "author", target = "author")
    @Mapping(source = "id", target = "id")
    Book toBook(BookDTO bookDTO);

    List<BookDTO> toBookDTOs(List<Book> books);

    List<Book> toBooks(List<BookDTO> bookDTOs);
}
