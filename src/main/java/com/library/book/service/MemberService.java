package com.library.book.service;

import com.library.book.dto.MemberDTO;
import com.library.book.mapper.MemberMapper;
import com.library.book.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper mapper = MemberMapper.INSTANCE;


    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Optional<MemberDTO> create(MemberDTO memberDTO) {
        return Optional.ofNullable(memberDTO)
                .map(mapper::toMember)
                .map(memberRepository::save)
                .map(mapper::toMemberDTO);
    }

    public List<MemberDTO> getAll() {
        return memberRepository.findAll().stream()
                .map(mapper::toMemberDTO)
                .collect(Collectors.toList());
    }

    public Optional<MemberDTO> getByUsername(String username) {
        return memberRepository.findByUsername(username)
                .map(mapper::toMemberDTO);
    }

    public Optional<MemberDTO> update(String username, MemberDTO memberDTO) {
        return memberRepository.findByUsername(username)
                .map(member -> {
                    member.setEmail(memberDTO.email());
                    member.setAddress(memberDTO.address());
                    member.setPhoneNumber(memberDTO.phoneNumber());
                    return Optional.ofNullable(mapper.toMemberDTO(memberRepository.save(member)));
                }).orElseThrow();
    }

    public void delete(String username) {
        memberRepository.deleteMemberByUsername(username);
    }
}
