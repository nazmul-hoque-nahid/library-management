package com.example.library_management.dto;

import com.example.library_management.entity.User;
import lombok.Getter;

@Getter
public class ChangeRoleRequest {
  private   User.Role role;
}
