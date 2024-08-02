package com.library.book.service;

import com.library.book.dto.BookDTO;
import com.library.book.mapper.AuthorMapper;
import com.library.book.mapper.BookMapper;
import com.library.book.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper = BookMapper.INSTANCE;
    private final AuthorMapper authorMapper = AuthorMapper.INSTANCE;


    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Optional<BookDTO> create(BookDTO bookDTO) {
        return Optional.ofNullable(bookDTO)
                .map(bookMapper::toBook)
                .map(bookRepository::save)
                .map(bookMapper::toBookDTO);
    }

    public List<BookDTO> getAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookDTO)
                .collect(Collectors.toList());
    }

    public Optional<BookDTO> getById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toBookDTO);
    }

    public Optional<BookDTO> update(Long id, BookDTO bookDTO) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDTO.title());
                    book.setGenre(bookDTO.genre());
                    book.setPrice(bookDTO.price());
                    book.setAuthor(authorMapper.toAuthor(bookDTO.author()));
                    return Optional.ofNullable(bookMapper.toBookDTO(bookRepository.save(book)));
                }).orElseThrow();
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
