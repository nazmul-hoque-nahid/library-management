package com.example.library_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    @NotBlank
    @Size(max = 300)
    private String reason;
    @Enumerated(EnumType.STRING)
    private Category category=Category.OVERDUE;
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    @OneToOne
    @JoinColumn(name = "borrow_record_id", nullable = false)
    private BorrowRecord borrowRecord;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FineStatus fineStatus=FineStatus.UNPAID;
    private LocalDateTime createdAt;
    public enum FineStatus{
       PAID,UNPAID,WAIVED
    }
    public enum Category{
        OVERDUE,DAMAGE,LOST
    }
    @PrePersist
    public void prePersist(){createdAt=LocalDateTime.now();}
}
