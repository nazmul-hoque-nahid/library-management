package com.example.library_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime issuedAt;
    private LocalDateTime dueAt;
    private LocalDateTime returnedAt;
    @Enumerated(EnumType.STRING)
    private Status status=Status.BORROWED;
    @ManyToOne
    @JoinColumn(name = "issued_by",nullable = false)
    private User issuedBy;
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "copy_id",nullable = false)
    private BookCopy bookCopy;
    public enum Status{
        BORROWED,
        RETURNED,
        OVERDUE
    }
    @PrePersist
    public void prePersist(){
        issuedAt=LocalDateTime.now();
        dueAt=issuedAt.plusDays(7);
    }
}
