package com.library.book.service;

import com.library.book.dto.LoanDTO;
import com.library.book.entity.Book;
import com.library.book.entity.Loan;
import com.library.book.entity.Member;
import com.library.book.exceptions.LoanLimitExceededException;
import com.library.book.repository.BookRepository;
import com.library.book.repository.LoanRepository;
import com.library.book.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    private LoanRepository loanRepository;
    private MemberRepository memberRepository;
    private BookRepository bookRepository;
    private LoanService loanService;

    @BeforeEach
    void setUp() throws Exception {
        loanRepository = mock(LoanRepository.class);
        memberRepository = mock(MemberRepository.class);
        bookRepository = mock(BookRepository.class);
        loanService = new LoanService(loanRepository, memberRepository, bookRepository);

        Field maxLoanAmountField = LoanService.class.getDeclaredField("maxLoanAmount");
        maxLoanAmountField.setAccessible(true);
        maxLoanAmountField.set(loanService, 5);
    }


    @Test
    void createLoan() {

        Optional<Member> username = memberRepository.findByUsername("username");

        if (username.isPresent()) {
            List<Loan> byMember = loanRepository.findByMember(username.get());
            loanRepository.deleteAll(byMember);
        }


        LoanDTO loanDTO = new LoanDTO(null, LocalDate.now(), LocalDate.now().plusDays(14), "username", 1L);
        Member member = new Member();
        Book book = new Book();

        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(member));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(loanRepository.findByMember(member)).thenReturn(List.of());
        when(loanRepository.save(any(Loan.class))).thenReturn(new Loan());

        Optional<LoanDTO> createdLoan = loanService.create(loanDTO);
        assertTrue(createdLoan.isPresent());

    }

    @Test
    void getAllLoans() {
        Loan loan = new Loan();
        when(loanRepository.findAll()).thenReturn(List.of(loan));

        List<LoanDTO> loans = loanService.getAll();
        assertEquals(1, loans.size());
    }

    @Test
    void getLoanById() {
        Loan loan = new Loan();
        when(loanRepository.findById(anyLong())).thenReturn(Optional.of(loan));

        Optional<LoanDTO> foundLoan = loanService.getById(1L);
        assertTrue(foundLoan.isPresent());
    }

    @Test
    void updateLoan() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setLendDate(LocalDate.now());

        LoanDTO loanDTO = new LoanDTO(1L, LocalDate.now(), LocalDate.now().plusDays(14), "username", 1L);
        Member member = new Member();
        Book book = new Book();

        when(loanRepository.findById(anyLong())).thenReturn(Optional.of(loan));
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(member));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        Optional<LoanDTO> updatedLoan = loanService.update(1L, loanDTO);
        assertTrue(updatedLoan.isPresent());
    }

    @Test
    void deleteLoan() {
        loanService.delete(1L);
        verify(loanRepository, times(1)).deleteById(1L);
    }

    @Test
    void createLoanExceedsLimit() {
        LoanDTO loanDTO = new LoanDTO(null, LocalDate.now(), LocalDate.now().plusDays(14), "username", 1L);
        Member member = new Member();
        Book book = new Book();
        List<Loan> loans = List.of(new Loan(), new Loan(), new Loan(), new Loan(), new Loan(), new Loan());

        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(member));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(loanRepository.findByMember(member)).thenReturn(loans);

        assertThrows(LoanLimitExceededException.class, () -> loanService.create(loanDTO));
    }
}
