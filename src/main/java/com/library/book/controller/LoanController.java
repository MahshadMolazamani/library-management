package com.library.book.controller;

import com.library.book.dto.LoanDTO;
import com.library.book.service.LoanService;
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

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/loans")
@Tag(name = "Loan Controller", description = "API for managing loans in the library")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }


    @PostMapping
    @Operation(summary = "Create a new loan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan created successfully", content = @Content(schema = @Schema(implementation = LoanDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<LoanDTO> create(@RequestBody LoanDTO loanDTO) {
        return loanService.create(loanDTO)
                .map(ResponseEntity::ok)
                .orElseThrow();
    }

    @GetMapping
    @Operation(summary = "Get all loans")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all loans", content = @Content(schema = @Schema(implementation = LoanDTO.class)))
    })
    public ResponseEntity<List<LoanDTO>> getAll() {
        var loans = loanService.getAll();
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a loan by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan found", content = @Content(schema = @Schema(implementation = LoanDTO.class))),
            @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    public ResponseEntity<LoanDTO> getById(@PathVariable Long id) {
        return loanService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a loan by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan updated successfully", content = @Content(schema = @Schema(implementation = LoanDTO.class))),
            @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    public ResponseEntity<LoanDTO> update(@PathVariable Long id, @RequestBody LoanDTO loanDTO) {
        Optional<LoanDTO> updatedLoan = loanService.update(id, loanDTO);
        return updatedLoan.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a loan by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Loan deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        loanService.delete(id);
        return noContent().build();
    }
}
