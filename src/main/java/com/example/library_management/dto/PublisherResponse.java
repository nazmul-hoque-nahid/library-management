package com.example.library_management.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PublisherResponse {
    private Long id;
    private String name;
    private String address;
    private String phone;
}
