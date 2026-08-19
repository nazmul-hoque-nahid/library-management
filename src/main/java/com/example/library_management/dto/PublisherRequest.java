package com.example.library_management.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PublisherRequest {
    @Pattern(regexp = "^[a-zA-Z ]+$",message = "Name only contain latter with space")
    private String name;
    private String phone;
    private String address;
}
