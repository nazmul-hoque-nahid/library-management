package com.example.library_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(
        name = "authors",
        indexes = {
           @Index(name = "idx_author_name",columnList = "name")
        }
)
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Lob//text data type in db
    @Column(columnDefinition = "TEXT")
    private String biography;
    private LocalDate dob;
    private String country;
    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();
}
