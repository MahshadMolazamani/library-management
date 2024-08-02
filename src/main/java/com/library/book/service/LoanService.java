package com.library.book.service;

import com.library.book.dto.LoanDTO;
import com.library.book.entity.Book;
import com.library.book.entity.Loan;
import com.library.book.entity.Member;
import com.library.book.exceptions.LoanLimitExceededException;
import com.library.book.mapper.LoanMapper;
import com.library.book.repository.BookRepository;
import com.library.book.repository.LoanRepository;
import com.library.book.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanService {

    @Value("${max.loan.amount}")
    private int maxLoanAmount;

    private final LoanRepository loanRepository;

    private final MemberRepository memberRepository;

    private final BookRepository bookRepository;

    private final LoanMapper mapper = LoanMapper.INSTANCE;


    public LoanService(LoanRepository loanRepository, MemberRepository memberRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
    }

    public Optional<LoanDTO> create(LoanDTO loanDTO) {

        Member member = memberRepository.findByUsername(loanDTO.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username"));

        Book book = bookRepository.findById(loanDTO.bookId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid book ID"));

        List<Loan> existingLoans = loanRepository.findByMember(member);

        List<Loan> activeLoanList = findActiveLoans(existingLoans);

        if (activeLoanList.toArray().length > maxLoanAmount) {
            throw new LoanLimitExceededException("Member cannot have more than 5 active loans.");
        }

        Loan loan = mapper.toLoan(loanDTO);
        loan.setMember(member);
        loan.setBook(book);

        return Optional.ofNullable(mapper.toLoanDTO(loanRepository.save(loan)));
    }

    public List<LoanDTO> getAll() {
        return loanRepository.findAll().stream()
                .map(mapper::toLoanDTO)
                .collect(Collectors.toList());
    }

    public Optional<LoanDTO> getById(Long id) {
        return loanRepository.findById(id)
                .map(mapper::toLoanDTO);
    }

    public Optional<LoanDTO> update(Long id, LoanDTO loanDTO) {
        return loanRepository.findById(id)
                .map(existingLoan -> {
                    existingLoan.setBook(bookRepository.findById(loanDTO.bookId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid book ID")));
                    existingLoan.setMember(memberRepository.findByUsername(loanDTO.username())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid username")));
                    existingLoan.setLendDate(loanDTO.lendDate());
                    existingLoan.setReturnDate(loanDTO.returnDate());
                    return Optional.ofNullable(mapper.toLoanDTO(loanRepository.save(existingLoan)));
                }).orElseThrow();
    }

    public void delete(Long id) {
        loanRepository.deleteById(id);
    }

    private List<Loan> findActiveLoans(List<Loan> existingLoans) {
        return existingLoans.stream()
                .filter(loan -> loan.getReturnDate() == null || loan.getReturnDate().isAfter(LocalDate.now()))
                .collect(Collectors.toList());
    }
}
