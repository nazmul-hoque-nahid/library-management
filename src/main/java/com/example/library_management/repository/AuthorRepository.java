package com.example.library_management.repository;

import com.example.library_management.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface AuthorRepository extends JpaRepository<Author,Long> {

    Page<Author> findByNameContainingIgnoreCaseAndCountryContainingIgnoreCase(String name, String country, Pageable pageable);
    boolean existsByNameIgnoreCase(String name);
    Page<Author>findByNameContainingIgnoreCase(String name,Pageable pageable);
    Page<Author>findByCountryContainingIgnoreCase(String country,Pageable pageable);
}

