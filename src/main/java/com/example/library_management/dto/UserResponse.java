package com.example.library_management.dto;

import com.example.library_management.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private  String membershipNumber;
    private String address;
    private User.Status status;
    private User.Role role;
}
