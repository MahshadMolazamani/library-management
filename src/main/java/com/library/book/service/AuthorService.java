package com.library.book.service;

import com.library.book.dto.AuthorDTO;
import com.library.book.mapper.AuthorMapper;
import com.library.book.mapper.BookMapper;
import com.library.book.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper = AuthorMapper.INSTANCE;
    private final BookMapper bookMapper = BookMapper.INSTANCE;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Optional<AuthorDTO> create(AuthorDTO authorDTO) {
        return Optional.ofNullable(authorDTO)
                .map(authorMapper::toAuthor)
                .map(authorRepository::save)
                .map(authorMapper::toAuthorDTO);
    }


    public List<AuthorDTO> getAll() {
        return authorMapper.toAuthorDTOs(authorRepository.findAll());
    }

    public Optional<AuthorDTO> getById(Long id) {
        return authorRepository.findById(id)
                .map(authorMapper::toAuthorDTO);
    }


    public Optional<AuthorDTO> update(Long id, AuthorDTO authorDTO) {
        return authorRepository.findById(id)
                .map(author -> {
                    author.setName(authorDTO.name());
                    author.setDateOfBirth(authorDTO.dateOfBirth());
                    author.setBooks(bookMapper.toBooks(authorDTO.books()));
                    return Optional.ofNullable(authorMapper.toAuthorDTO(authorRepository.save(author)));
                }).orElseThrow();
    }

    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}
