package com.example.library_management.service;

import com.example.library_management.dto.BorrowRequest;
import com.example.library_management.dto.BorrowResponse;
import com.example.library_management.entity.BookCopy;
import com.example.library_management.entity.BorrowRecord;
import com.example.library_management.entity.Fine;
import com.example.library_management.entity.User;
import com.example.library_management.exception.DuplicateResourceException;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repository.BookCopyRepository;
import com.example.library_management.repository.BorrowRecordRepository;
import com.example.library_management.repository.FineRepository;
import com.example.library_management.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowRecordService {
    private BorrowResponse packData(BorrowRecord record){
        BorrowResponse response=new BorrowResponse();
        response.setBorrowId(record.getId());
        response.setStatus(record.getStatus());
        response.setIssuedAt(record.getIssuedAt());
        response.setDueAt(record.getDueAt());
        response.setUserId(record.getUser().getId());
        response.setCopyId(record.getBookCopy().getId());
        response.setLibrarianId(record.getIssuedBy().getId());
        response.setReturnedAt(record.getReturnedAt());
        return response;
    }
    private final BorrowRecordRepository borrowRecordRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;
    private final FineRepository fineRepository;
     @Transactional
    public BorrowResponse create(BorrowRequest request) {
        User librarian=userRepository.findById(request.getLibrarianId()).orElseThrow(()->new ResourceNotFoundException("Librarian not found"));
        User user=userRepository.findById(request.getUserId()).orElseThrow(()->new ResourceNotFoundException("User not found"));
        if(user.getStatus()!= User.Status.ACTIVE)throw  new IllegalArgumentException("User is not active");
        BookCopy copy=bookCopyRepository.findById(request.getBookCopyId()).orElseThrow(()->new IllegalStateException("Book copy not found"));
        if(copy.getStatus()!= BookCopy.Status.AVAILABLE) throw  new ResourceNotFoundException("This copy not available");
        if (librarian.getRole() != User.Role.LIBRARIAN && librarian.getStatus() != User.Status.ACTIVE) throw new IllegalArgumentException("Only Active librarians can issue books");

        BorrowRecord borrowRecord=new BorrowRecord();
        borrowRecord.setBookCopy(copy);
        borrowRecord.setUser(user);
        borrowRecord.setIssuedBy(librarian);
        copy.setStatus(BookCopy.Status.BORROWED);
        copy.getBook().setAvailableCopy(copy.getBook().getAvailableCopy()-1);//or book.setAvailableCopy(book.getAvailable-1)
        return packData(borrowRecordRepository.save(borrowRecord));
    }
    public Page<BorrowResponse> getAll(int page,int size){
        Pageable pageable= PageRequest.of(page, size);
        Page<BorrowRecord> records=borrowRecordRepository.findAll(pageable);
        return records.map(this::packData);
    }
    public BorrowResponse getById(Long id){
        BorrowRecord record=borrowRecordRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Borrow Record not found"));
        return packData(record);
    }
    @Transactional
    public BorrowResponse returnBook(Long id) {
        BorrowRecord record = borrowRecordRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Borrow record not found"));
        if (record.getStatus() == BorrowRecord.Status.RETURNED)throw new DuplicateResourceException("Book has already been returned.");
        record.setReturnedAt(LocalDateTime.now());
        record.setStatus(BorrowRecord.Status.RETURNED);
        BookCopy copy = record.getBookCopy();
        copy.setStatus(BookCopy.Status.AVAILABLE);
        copy.getBook().setAvailableCopy(copy.getBook().getAvailableCopy()+1);
        if (record.getReturnedAt().isAfter(record.getDueAt())) {
            long overdueDays = ChronoUnit.DAYS.between(
                    record.getDueAt().toLocalDate(),
                    record.getReturnedAt().toLocalDate()
            );
            BigDecimal amount = BigDecimal.valueOf(1).multiply(BigDecimal.valueOf(overdueDays));
            Fine fine = new Fine();
            fine.setBorrowRecord(record);
            fine.setUser(record.getUser());
            fine.setAmount(amount);
            fine.setReason("Returned " + overdueDays + " day(s) late");
            fineRepository.save(fine);
        }
        return packData(borrowRecordRepository.save(record));
    }
    public List<BorrowResponse> getByUser(Long userId){
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found"));
        return borrowRecordRepository.findByUserId(userId).stream().map(this::packData).toList();
    }
    public List<BorrowResponse> getByBookCopy(Long copyId){
        if (!bookCopyRepository.existsById(copyId)) {
            throw new ResourceNotFoundException("Book copy not found");
        }
        return borrowRecordRepository.findByBookCopyId(copyId).stream().map(this::packData).toList();
    }
    public List<BorrowResponse> getByStatus(BorrowRecord.Status status) {
        return borrowRecordRepository.findByStatus(status).stream().map(this::packData).toList();
    }
    public List<BorrowResponse> getOverdueBooks() {
        return borrowRecordRepository.findByDueAtBeforeAndStatus(LocalDateTime.now(),BorrowRecord.Status.BORROWED).stream().map(this::packData).toList();
    }
}
