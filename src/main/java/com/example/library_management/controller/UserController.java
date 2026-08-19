package com.example.library_management.controller;

import com.example.library_management.dto.ChangeRoleRequest;
import com.example.library_management.dto.UserResponse;
import com.example.library_management.dto.UserUpdateRequest;
import com.example.library_management.entity.User;
import com.example.library_management.exception.ErrorResponse;
import com.example.library_management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UserController {
 private final UserService service;
 @GetMapping
 @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
 @Operation(
         summary = "Get all users",
         description = "Returns paginated list of users"
 )
 @ApiResponse(
         responseCode = "200",
         description = "Users retrieved successfully",
         content = @Content(
                 mediaType = "application/json",
                 schema = @Schema(implementation = Page.class)
         )
 )
    public ResponseEntity<Page<UserResponse>>getAll(@RequestParam(defaultValue = "0")int page,@RequestParam(defaultValue = "10")int size){
     return ResponseEntity.ok(service.getAll(page,size));
 }
 @GetMapping("/{id}")
 @PreAuthorize("hasAnyRole('ADMIN','MEMBER','LIBRARIAN')")
 @Operation(
         summary = "Get user by ID"
 )
 @ApiResponse(
         responseCode = "200",
         description = "User found",
         content = @Content(
                 mediaType = "application/json",
                 schema = @Schema(implementation = UserResponse.class)
         )
 )
 @ApiResponse(
         responseCode = "404",
         description = "User not found",
         content = @Content(
                 mediaType = "application/json",
                 schema = @Schema(implementation = ErrorResponse.class)
         )
 )
    public ResponseEntity<UserResponse>getById(@PathVariable Long id){
     UserResponse response=service.getById(id);
     return ResponseEntity.ok(response);
 }
 @PutMapping("/{id}/activate")
 @PreAuthorize("hasRole('ADMIN')")
 @Operation(
         summary = "Activate user",
         description = "Activate pending user account"
 )
 @ApiResponse(
         responseCode = "204",
         description = "User activated successfully"
 )
 @ApiResponse(
         responseCode = "404",
         description = "User not found",
         content = @Content(
                 mediaType = "application/json",
                 schema = @Schema(implementation = ErrorResponse.class)
         )
 )
    public ResponseEntity<Void> approveUser(@PathVariable Long id){
     service.approveUser(id);
   return ResponseEntity.noContent().build();
 }
 @PutMapping("/{id}/suspend")
 @PreAuthorize("hasRole('ADMIN')")
 @Operation(
         summary = "Suspend user",
         description = "Suspend an active user account"
 )
 @ApiResponse(
         responseCode = "204",
         description = "User suspended successfully"
 )
 @ApiResponse(
         responseCode = "404",
         description = "User not found",
         content = @Content(
                 mediaType = "application/json",
                 schema = @Schema(implementation = ErrorResponse.class)
         )
 )
 public ResponseEntity<Void> suspendUser(@PathVariable Long id){
     service.suspendUser(id);
     return ResponseEntity.noContent().build();
 }
 @PutMapping("/{id}/change-role")
 @PreAuthorize("hasRole('ADMIN')")
 @Operation(
         summary = "Change user role",
         description = "Change role of a user"
 )
 @ApiResponse(
         responseCode = "204",
         description = "User role changed successfully"
 )
 @ApiResponse(
         responseCode = "400",
         description = "Invalid role request",
         content = @Content(
                 mediaType = "application/json",
                 schema = @Schema(implementation = ErrorResponse.class)
         )
 )
 @ApiResponse(
         responseCode = "404",
         description = "User not found",
         content = @Content(
                 mediaType = "application/json",
                 schema = @Schema(implementation = ErrorResponse.class)
         )
 )
 public ResponseEntity<Void> changeRole(@PathVariable Long id, @RequestBody ChangeRoleRequest request){
           service.changeRole(id,request.getRole());
           return ResponseEntity.noContent().build();
 }
 @PutMapping("/{id}/update")
 @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
 @Operation(
         summary = "Update user profile"
 )
 @ApiResponse(
         responseCode = "200",
         description = "User updated successfully",
         content = @Content(
                 mediaType = "application/json",
                 schema = @Schema(implementation = UserResponse.class)
         )
 )
 @ApiResponse(
         responseCode = "400",
         description = "Invalid update data",
         content = @Content(
                 mediaType = "application/json",
                 schema = @Schema(implementation = ErrorResponse.class)
         )
 )
   public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,@Valid @RequestBody UserUpdateRequest request){
     UserResponse response=service.updateUser(id,request);
     return ResponseEntity.ok(response);
 }
  @GetMapping("/search")
  @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public ResponseEntity<Page<UserResponse>>search(
          String name,
          String email,
          String membershipNumber,
          User.Role role,
          User.Status status,
          int page,
          int size
  ){
    Page<UserResponse>users=service.search(name,email,membershipNumber,role,status,page,size);
    return ResponseEntity.of(Optional.ofNullable(users));
  }

}
