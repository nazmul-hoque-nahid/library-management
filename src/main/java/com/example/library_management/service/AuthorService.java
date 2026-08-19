package com.example.library_management.service;

import com.example.library_management.dto.AuthorRequest;
import com.example.library_management.dto.AuthorResponse;
import com.example.library_management.entity.Author;
import com.example.library_management.exception.DuplicateResourceException;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repository.AuthorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    private AuthorResponse packAuthor(Author author){
        AuthorResponse response=new AuthorResponse();
        response.setId(author.getId());
        response.setName(author.getName());
        response.setBiography(author.getBiography());
        response.setCountry(author.getCountry());
        return response;
    }
    public AuthorResponse create(AuthorRequest request){
        if(authorRepository.existsByNameIgnoreCase(request.getName()))
            throw new DuplicateResourceException("This author already exist");

        Author author=new Author();
        author.setName(request.getName());
        author.setBiography(request.getBiography());
        author.setDob(request.getDob());
        author.setCountry(request.getCountry());
        return packAuthor(authorRepository.save(author));
    }
    public Page<AuthorResponse> getAll(int page, int size)
    {
        Pageable pageable= PageRequest.of(page,size);
        Page<Author>authors=authorRepository.findAll(pageable);
        return authors.map(this::packAuthor);
    }
    public AuthorResponse getById( Long id){
       Author author=authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author not found"));
       return packAuthor(author);
    }
    public Page<AuthorResponse>search(String name,String country,int page,int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Author> authors;
        if(name != null && !name.isBlank() &&country != null && !country.isBlank()){
            authors = authorRepository.findByNameContainingIgnoreCaseAndCountryContainingIgnoreCase(name, country, pageable);
        }else if(name!=null && !name.isBlank()) {
            authors = authorRepository.findByNameContainingIgnoreCase(name, pageable);
        }else if(country!=null && !country.isBlank()) {
            authors=authorRepository.findByCountryContainingIgnoreCase(country, pageable);
        } else {
            authors=authorRepository.findAll(pageable);
        }
        return authors.map(this::packAuthor);
    }
    public void delete(Long id){
        Author author=authorRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Author not found"));
        if(!author.getBooks().isEmpty()) throw new IllegalArgumentException("Author can't be delete");
        authorRepository.deleteById(id);
    }
}
