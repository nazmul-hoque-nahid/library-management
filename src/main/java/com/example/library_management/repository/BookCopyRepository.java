package com.example.library_management.repository;

import com.example.library_management.entity.BookCopy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy,Long> {
List<BookCopy> findByCopyNumberContainingIgnoreCase(String copyNumber);
List<BookCopy>findByStatus(BookCopy.Status status);
boolean existsByCopyNumber(String copyNumber);
Page<BookCopy> findByBookId(Long id, Pageable pageable);
boolean existsByBookId(Long id);

}
