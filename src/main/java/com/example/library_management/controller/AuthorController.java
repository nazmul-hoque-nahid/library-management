package com.example.library_management.controller;

import com.example.library_management.dto.AuthorRequest;
import com.example.library_management.dto.AuthorResponse;
import com.example.library_management.entity.Author;
import com.example.library_management.exception.ErrorResponse;
import com.example.library_management.service.AuthorService;
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

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Author",description = "Author management APIs")
@SecurityRequirement(name = "bearerAuth")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @Operation(summary = "Create Author",description = "Create a new Author by Admin or Librarian")
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "201",
            description = "Author created Successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthorResponse.class)
            )
    )
    public ResponseEntity<AuthorResponse> create(@Valid @RequestBody AuthorRequest request){
      return   ResponseEntity.status(HttpStatus.CREATED).body(authorService.create(request));
    }

    @GetMapping
    @Operation(
            summary = "Get all authors",
            description = "Returns a paginated list of authors"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Authors retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<Page<AuthorResponse>> getAllAuthor(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "10")int size){
        return ResponseEntity.ok(authorService.getAll(page,size));
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get Author by id",description = "Return Author matching id")
    @ApiResponse(
            responseCode = "200",
            description = "Author retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation=AuthorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Author not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation= ErrorResponse.class)
            )
    )
    public ResponseEntity<AuthorResponse> getAuthor(@PathVariable Long id){
     return ResponseEntity.ok(authorService.getById(id));
    }
    @GetMapping("/search")
    @Operation(summary = "Search authors",description = "Search authors by name and country with pagination")
    @ApiResponse(
            responseCode = "200",
            description = "Authors found successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<Page<AuthorResponse>>searchAuthor(@RequestParam(required = false) String name,@RequestParam(required = false) String country,@RequestParam(defaultValue = "0")int page,@RequestParam(defaultValue = "10")int size){
        return ResponseEntity.ok( authorService.search(name,country,page,size));
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete author",description = "Delete author by ID")
    @ApiResponse(
            responseCode = "204",
            description = "Authors deleted successfully"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Author not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
