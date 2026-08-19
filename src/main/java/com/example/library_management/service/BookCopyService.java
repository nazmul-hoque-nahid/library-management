package com.example.library_management.service;

import com.example.library_management.dto.BookCopyRequest;
import com.example.library_management.dto.BookCopyResponse;
import com.example.library_management.dto.BookCopyUpdateRequest;
import com.example.library_management.entity.Book;
import com.example.library_management.entity.BookCopy;
import com.example.library_management.exception.DuplicateResourceException;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repository.BookCopyRepository;
import com.example.library_management.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCopyService {
    private BookCopyResponse packCopy(BookCopy copy){
        BookCopyResponse response=new BookCopyResponse();
        response.setCopyNumber(copy.getCopyNumber());
        response.setBookId(copy.getBook().getId());
        response.setCopyNumber(copy.getCopyNumber());
        response.setAcquiredDate(copy.getAcquiredDate());
        response.setStatus(copy.getStatus());
        response.setId(copy.getId());
        return response;
    }
    private final BookCopyRepository bookCopyRepository;
    private final BookRepository bookRepository;
    @Transactional
    public BookCopyResponse addBookCopies( BookCopyRequest request){
        Book book=bookRepository.findById(request.getBookId()).orElseThrow(()->new ResourceNotFoundException("book not found"));
        if (bookCopyRepository.existsByCopyNumber(request.getCopyNumber())) {
            throw new DuplicateResourceException("Copy number already exists");
        }
           BookCopy copy=new BookCopy();
        copy.setCopyNumber(request.getCopyNumber());
        copy.setAcquiredDate(LocalDateTime.now());
        book.setAvailableCopy(book.getAvailableCopy()+1);
        book.setTotalCopy(book.getTotalCopy()+1);
        //bookRepository.save(book); not necessary
        //book.getBookCopies().add(bookCopy) not necessary
        copy.setBook(book);
      return packCopy(bookCopyRepository.save(copy));
    }
    public Page<BookCopyResponse> getAll(int page, int size){
        Pageable pageable= PageRequest.of(page,size);
        Page<BookCopy>copies=bookCopyRepository.findAll(pageable);
     return copies.map(this::packCopy);
    }
    public BookCopyResponse getById(Long id){
        BookCopy copy=bookCopyRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Copy is not found"));
        return packCopy(copy);
    }
    public void delete(Long id){
        if (!bookCopyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book copy not found");
        }
        bookCopyRepository.deleteById(id);
    }
    public Page<BookCopyResponse>getByBookId(Long id,int page,int size){
        Pageable pageable=PageRequest.of(page,size);
        Page<BookCopy>copies=bookCopyRepository.findByBookId(id,pageable);
        return copies.map(this::packCopy);
    }
    public List<BookCopyResponse>getByAvailable(BookCopy.Status status){
        return bookCopyRepository.findByStatus(status).stream().map(this::packCopy).toList();
    }
    public BookCopyResponse update(Long id,BookCopyUpdateRequest request){
        BookCopy copy=bookCopyRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Book Copy not found"));
        if(request.getCopyNumber()!=null){
            copy.setCopyNumber(request.getCopyNumber());
        }
        if(request.getStatus()!=null){
            copy.setStatus(request.getStatus());
        }
        if(request.getBookId()!=null && bookRepository.existsById(request.getBookId())){
            Book book=bookRepository.findById(request.getBookId()).orElseThrow(()->new ResourceNotFoundException("book not found"));
            copy.setBook(book);
        }
        return packCopy(bookCopyRepository.save(copy));
    }
}
