package com.library.book.service;

import com.library.book.dto.MemberDTO;
import com.library.book.entity.Member;
import com.library.book.mapper.MemberMapper;
import com.library.book.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    private MemberRepository memberRepository;
    private MemberMapper memberMapper;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        memberMapper = MemberMapper.INSTANCE;
        memberService = new MemberService(memberRepository);
    }

    @Test
    void createMember() {
        MemberDTO memberDTO = new MemberDTO(null, "username", "email@example.com", "address", "1234567890", List.of());
        Member member = memberMapper.toMember(memberDTO);

        when(memberRepository.save(any(Member.class))).thenReturn(member);

        Optional<MemberDTO> savedMember = memberService.create(memberDTO);
        assertTrue(savedMember.isPresent());
        assertEquals(savedMember.get().username(), memberDTO.username());
    }

    @Test
    void getAllMembers() {
        Member member1 = new Member();
        member1.setUsername("username1");

        Member member2 = new Member();
        member2.setUsername("username2");

        when(memberRepository.findAll()).thenReturn(List.of(member1, member2));

        List<MemberDTO> members = memberService.getAll();
        assertEquals(2, members.size());
    }

    @Test
    void getMemberByUsername() {
        Member member = new Member();
        member.setUsername("username");

        when(memberRepository.findByUsername("username")).thenReturn(Optional.of(member));

        Optional<MemberDTO> foundMember = memberService.getByUsername("username");
        assertTrue(foundMember.isPresent());
        assertEquals("username", foundMember.get().username());
    }

    @Test
    void updateMember() {
        Member member = new Member();
        member.setUsername("username");
        member.setEmail("oldemail@example.com");

        MemberDTO memberDTO = new MemberDTO(null, "username", "newemail@example.com", "address", "1234567890", List.of());

        when(memberRepository.findByUsername("username")).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        Optional<MemberDTO> updatedMember = memberService.update("username", memberDTO);
        assertTrue(updatedMember.isPresent());
        assertEquals("newemail@example.com", updatedMember.get().email());
    }

    @Test
    void deleteMember() {
        memberService.delete("username");
        verify(memberRepository, times(1)).deleteMemberByUsername("username");
    }
}

