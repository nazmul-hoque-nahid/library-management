package com.example.library_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "user",
indexes = {
        @Index(name = "idx_user_email",columnList = "email"),
        @Index(name = "idx_user_name",columnList = "name")
}
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 40)
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role=Role.MEMBER;
    @Column(unique = true,name = "membership_number")
    private  String membershipNumber;
    @Enumerated(EnumType.STRING)
    private Status status= Status.PENDING;
    private String address;
    public enum Role{
        MEMBER,LIBRARIAN,ADMIN
    }
    public enum Status{
        PENDING,
        ACTIVE,
        SUSPENDED
    }
    private LocalDateTime createdAt;
    @PrePersist
    public void prePersist(){
        createdAt=LocalDateTime.now();
    }
}
