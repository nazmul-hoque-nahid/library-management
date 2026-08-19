package com.example.library_management.service;

import com.example.library_management.dto.FineRequest;
import com.example.library_management.dto.FineResponse;
import com.example.library_management.entity.BorrowRecord;
import com.example.library_management.entity.Fine;
import com.example.library_management.entity.User;
import com.example.library_management.exception.DuplicateResourceException;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repository.BorrowRecordRepository;
import com.example.library_management.repository.FineRepository;
import com.example.library_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FineService {

    private final FineRepository fineRepository;
    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    private FineResponse packData(Fine fine) {
        FineResponse response = new FineResponse();
        response.setId(fine.getId());
        response.setUserId(fine.getUser().getId());
        response.setBorrowRecordId(fine.getBorrowRecord().getId());
        response.setAmount(fine.getAmount());
        response.setReason(fine.getReason());
        response.setCategory(fine.getCategory());
        response.setFineStatus(fine.getFineStatus());
        response.setCreatedAt(fine.getCreatedAt());
        return response;
    }

    public FineResponse create(FineRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        BorrowRecord borrowRecord = borrowRecordRepository.findById(request.getBorrowRecordId())
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found"));
        if (fineRepository.findByBorrowRecordId(borrowRecord.getId()).isPresent())
            throw new DuplicateResourceException("A fine already exists for this borrow record");

        Fine fine = new Fine();
        fine.setUser(user);
        fine.setBorrowRecord(borrowRecord);
        fine.setAmount(request.getAmount());
        fine.setReason(request.getReason());
        fine.setCategory(request.getCategory());
        return packData(fineRepository.save(fine));
    }

    public Page<FineResponse> getAll(int page, int size) {
        Pageable pageable= PageRequest.of(page, size);
        return  fineRepository.findAll(pageable).map(this::packData);
    }

    public FineResponse getById(Long id) {
        Fine fine = fineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fine not found"));
        return packData(fine);
    }

    public List<FineResponse> getByUser(Long userId) {
        if (!userRepository.existsById(userId))
            throw new ResourceNotFoundException("User not found");
        return fineRepository.findByUserId(userId).stream().map(this::packData).toList();
    }

    public Page<FineResponse> getByStatus(int page,int size,Fine.FineStatus status) {
        Pageable pageable=PageRequest.of(page,size);
        Page<Fine>fines=fineRepository.findByFineStatus( status,pageable);
        return fines.map(this::packData);
    }

    public Page<FineResponse> getByCategory(int page,int size,Fine.Category category) {
        Pageable pageable=PageRequest.of(page,size);
        Page<Fine>finePage=fineRepository.findByCategory(category,pageable);
        return finePage.map(this::packData);
    }

    public FineResponse pay(Long id) {
        Fine fine = fineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fine not found"));
        if (fine.getFineStatus() != Fine.FineStatus.UNPAID)
            throw new IllegalStateException("Only unpaid fines can be marked as paid");
        fine.setFineStatus(Fine.FineStatus.PAID);
        return packData(fineRepository.save(fine));
    }

    public FineResponse waive(Long id) {
        Fine fine = fineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fine not found"));
        if (fine.getFineStatus() != Fine.FineStatus.UNPAID)
            throw new IllegalStateException("Only unpaid fines can be waived");
        fine.setFineStatus(Fine.FineStatus.WAIVED);
        return packData(fineRepository.save(fine));
    }

    public void delete(Long id) {
        Fine fine = fineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fine not found"));
        fineRepository.delete(fine);
    }
}