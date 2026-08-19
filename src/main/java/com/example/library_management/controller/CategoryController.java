package com.example.library_management.controller;

import com.example.library_management.dto.CategoryRequest;
import com.example.library_management.dto.CategoryResponse;
import com.example.library_management.entity.Category;
import com.example.library_management.exception.ErrorResponse;
import com.example.library_management.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Category")
public class CategoryController {
private final CategoryService service;
CategoryController(CategoryService service){
    this.service=service;
}
@PostMapping
@Operation(
        summary = "Create category",
        description = "Create a new book category"
)
@ApiResponse(
        responseCode = "201",
        description = "Category created successfully",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponse.class)
        )
)
@ApiResponse(
        responseCode = "400",
        description = "Invalid request data",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
        )
)
public ResponseEntity<CategoryResponse> create(@Valid@RequestBody CategoryRequest request){
    CategoryResponse category=service.create(request);
    return  ResponseEntity.status(HttpStatus.CREATED).body(category);
}
@GetMapping
@ApiResponse(
        responseCode = "200",
        description = "Categories retrieved successfully",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Page.class)
        )
)
public ResponseEntity<Page<CategoryResponse>>getAll(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "10")int size){
    return ResponseEntity.ok(service.getAll(page,size));
}
@PutMapping("/{id}")
@ApiResponse(
        responseCode = "200",
        description = "Category updated successfully",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CategoryResponse.class)
        )
)
@ApiResponse(
        responseCode = "400",
        description = "Invalid request data",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
        )
)
@ApiResponse(
        responseCode = "404",
        description = "Category not found",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
        )
)
    public ResponseEntity<CategoryResponse>update(@Valid@PathVariable Long id,@RequestBody CategoryRequest request){
    CategoryResponse categoryResponse=service.update(id,request);
    return ResponseEntity.ok(categoryResponse);
}
@DeleteMapping("/{id}")
@Operation(
        summary = "Delete category",
        description = "Delete category by ID"
)
@ApiResponse(
        responseCode = "204",
        description = "Category deleted successfully"
)
@ApiResponse(
        responseCode = "404",
        description = "Category not found",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
        )
)
    public ResponseEntity<Void>delete(@PathVariable Long id){
    return ResponseEntity.noContent().build();
}
}
