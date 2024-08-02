package com.library.book.controller;

import com.library.book.dto.AuthorDTO;
import com.library.book.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("/authors")
@Tag(name = "Author Controller", description = "API for managing authors in the library")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @Operation(summary = "Create a new author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author created successfully", content = @Content(schema = @Schema(implementation = AuthorDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<AuthorDTO> create(@RequestBody AuthorDTO authorDTO) {
        return authorService.create(authorDTO)
                .map(ResponseEntity::ok)
                .orElseThrow();
    }

    @GetMapping
    @Operation(summary = "Get all authors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all authors", content = @Content(schema = @Schema(implementation = AuthorDTO.class)))
    })
    public ResponseEntity<List<AuthorDTO>> getAll() {
        var authors = authorService.getAll();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an author by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author found", content = @Content(schema = @Schema(implementation = AuthorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    public ResponseEntity<AuthorDTO> getById(@PathVariable Long id) {
        return authorService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an author by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated successfully", content = @Content(schema = @Schema(implementation = AuthorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    public ResponseEntity<AuthorDTO> update(@PathVariable Long id, @RequestBody AuthorDTO authorDTO) {
        Optional<AuthorDTO> savedAuthor = authorService.update(id, authorDTO);
        return savedAuthor.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an author by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorService.delete(id);
        return noContent().build();
    }
}
