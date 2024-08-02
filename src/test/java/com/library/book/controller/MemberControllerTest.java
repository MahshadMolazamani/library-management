package com.library.book.controller;

import com.library.book.dto.MemberDTO;
import com.library.book.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    private MemberDTO memberDTO;

    @BeforeEach
    void setUp() {
        memberDTO = new MemberDTO(null, "username", "email@example.com", "address", "1234567890", List.of());
    }

    @Test
    void createMember() throws Exception {
        when(memberService.create(any(MemberDTO.class))).thenReturn(Optional.of(memberDTO));

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"username\",\"email\":\"email@example.com\",\"address\":\"address\",\"phoneNumber\":\"1234567890\",\"loans\":[]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    void getAllMembers() throws Exception {
        when(memberService.getAll()).thenReturn(List.of(memberDTO));

        mockMvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("username"));
    }

    @Test
    void getMemberByUsername() throws Exception {
        when(memberService.getByUsername(anyString())).thenReturn(Optional.of(memberDTO));

        mockMvc.perform(get("/members/username"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    void updateMember() throws Exception {
        MemberDTO updatedMemberDTO = new MemberDTO(null, "username", "newemail@example.com", "address", "1234567890", List.of());
        when(memberService.update(anyString(), any(MemberDTO.class))).thenReturn(Optional.of(updatedMemberDTO));

        mockMvc.perform(put("/members/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"username\",\"email\":\"newemail@example.com\",\"address\":\"address\",\"phoneNumber\":\"1234567890\",\"loans\":[]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("newemail@example.com"));
    }

    @Test
    void deleteMember() throws Exception {
        mockMvc.perform(delete("/members/username"))
                .andExpect(status().isNoContent());
    }
}
