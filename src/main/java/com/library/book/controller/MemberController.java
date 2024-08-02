package com.library.book.controller;

import com.library.book.dto.MemberDTO;
import com.library.book.service.MemberService;
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
@RequestMapping("/members")
@Tag(name = "Member Controller", description = "API for managing members in the library")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    @Operation(summary = "Create a new member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member created successfully", content = @Content(schema = @Schema(implementation = MemberDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<MemberDTO> create(@RequestBody MemberDTO memberDTO) {
        return memberService.create(memberDTO)
                .map(ResponseEntity::ok)
                .orElseThrow();
    }


    @GetMapping
    @Operation(summary = "Get all members")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all members", content = @Content(schema = @Schema(implementation = MemberDTO.class)))
    })
    public ResponseEntity<List<MemberDTO>> getAll() {
        var members = memberService.getAll();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get a member by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member found", content = @Content(schema = @Schema(implementation = MemberDTO.class))),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    public ResponseEntity<MemberDTO> getByUsername(@PathVariable String username) {
        return memberService.getByUsername(username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> notFound().build());
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update a member by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member updated successfully", content = @Content(schema = @Schema(implementation = MemberDTO.class))),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    public ResponseEntity<MemberDTO> update(@PathVariable String username, @RequestBody MemberDTO memberDTO) {
        Optional<MemberDTO> updatedMember = memberService.update(username, memberDTO);
        return updatedMember.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete a member by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Member deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    public ResponseEntity<Void> delete(@PathVariable String username) {
        memberService.delete(username);
        return noContent().build();
    }
}
