package com.example.library_management.service;
import com.example.library_management.dto.BookRequest;
import com.example.library_management.dto.BookResponse;
import com.example.library_management.entity.*;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repository.*;
import com.example.library_management.specification.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BookService {
    private BookResponse packBook(Book book){
        Book newBook= bookRepository.save(book);
        BookResponse response=new BookResponse();
        response.setId(newBook.getId());
        response.setIsbn(newBook.getIsbn());
        response.setTitle(newBook.getTitle());
        response.setLanguage(newBook.getLanguage());
        response.setEdition(newBook.getEdition());
        response.setAvailableCopies(newBook.getAvailableCopy());
        return response;
    }
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
public BookResponse create(BookRequest request){
    Book book=new Book();
    book.setIsbn(request.getIsbn());
    book.setEdition(request.getEdition());
    book.setTitle(request.getTitle());
    book.setLanguage(request.getLanguage());
    Publisher publisher=publisherRepository.findById(request.getPublisherId()).orElseThrow(()->new ResourceNotFoundException("Publisher not found"));
    Set<Author> authors=new HashSet<>(authorRepository.findAllById(request.getAuthorIds()));
    Set<Category>categories=new HashSet<>(categoryRepository.findAllById(request.getCategoryIds()));
    book.setPublisher(publisher);
    book.setAuthors(authors);
    book.setCategories(categories);
    book.setAvailableCopy(0);
    book.setTotalCopy(0);
  return packBook(bookRepository.save(book));
}
public Page<BookResponse> getAllBooks(int page,int size){
    Pageable pageable= PageRequest.of(page, size);
    Page<Book>bookPage=bookRepository.findAll(pageable);
    return bookPage.map(this::packBook);
}
public   BookResponse getBookById(Long id){
    Book book=bookRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("book not found"));
    return packBook(book);
}
  public void deleteBook(Long id){
      Book book=bookRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("book not found"));
       if(book.getTotalCopy()!=0){
           throw new IllegalArgumentException("Can't delete book with copies");
       }
       bookRepository.deleteById(id);

  }
public Page<BookResponse>search(
       String  title,
       String   isbn,
       String   authorName,
       Long  authorId,
       String categoryName,
       Long categoryId,
       Long  publisherId,
       String publisherName,
       Boolean  available,
      int page,
      int size
){
    Pageable pageable=PageRequest.of(page,size);
    Specification<Book>specification= BookSpecification.search(
            title,
            isbn,
            authorName,
            authorId,
            categoryName,
            categoryId,
            publisherName,
            publisherId,
            available
    );
    return bookRepository.findAll(specification, pageable).map(this::packBook);
}

}
