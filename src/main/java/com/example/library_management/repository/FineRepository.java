package com.example.library_management.repository;

import com.example.library_management.entity.Fine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<Fine,Long> {
    List<Fine> findByUserId(Long userId);
    Page<Fine> findByFineStatus(Fine.FineStatus fineStatus, Pageable pageable);
    Page<Fine> findByCategory(Fine.Category category,Pageable pageable);
    Optional<Fine> findByBorrowRecordId(Long borrowRecordId);
}
