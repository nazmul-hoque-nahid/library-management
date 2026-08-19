package com.example.library_management.repository;

import com.example.library_management.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord,Long> {
    List<BorrowRecord> findByUserId(Long userId);
    List<BorrowRecord>findByBookCopyId(Long copyId);
    List<BorrowRecord> findByStatus(BorrowRecord.Status status);
    List<BorrowRecord> findByDueAtBeforeAndStatus(LocalDateTime date,BorrowRecord.Status status);
}
