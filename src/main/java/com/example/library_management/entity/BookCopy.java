package com.example.library_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Setter
@Getter
@NoArgsConstructor
public class BookCopy{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String copyNumber;
    private LocalDateTime acquiredDate;
    @Enumerated(EnumType.STRING)
    private Status status=Status.AVAILABLE;
    @ManyToOne
    @JoinColumn(name = "book_id",nullable = false)
    private Book book;
    public enum Status{
        AVAILABLE,
        BORROWED,
        RESERVED,
        LOST,
        DAMAGED
    }
}
