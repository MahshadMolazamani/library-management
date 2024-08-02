package com.library.book.repository;

import com.library.book.entity.Loan;
import com.library.book.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByMember(Member member);
}
