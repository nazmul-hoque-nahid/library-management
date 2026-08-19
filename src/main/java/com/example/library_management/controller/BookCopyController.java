package com.example.library_management.controller;

import com.example.library_management.dto.BookCopyRequest;
import com.example.library_management.dto.BookCopyResponse;
import com.example.library_management.dto.BookCopyUpdateRequest;
import com.example.library_management.entity.BookCopy;
import com.example.library_management.exception.ErrorResponse;
import com.example.library_management.service.BookCopyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/book-copies")
@Tag(name = "Book Copies", description = "Book copy management APIs")
@SecurityRequirement(name = "bearerAuth")
public class BookCopyController {
    private final BookCopyService service;
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @Operation(
            summary = "Add book copies",
            description = "Add new copies of a book"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Book copies added successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BookCopyResponse.class)
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
    public ResponseEntity<BookCopyResponse> addBookCopies(
           @Valid @RequestBody BookCopyRequest request) {
      BookCopyResponse response= service.addBookCopies(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @Operation(
            summary = "Get all book copies",
            description = "Returns paginated book copies"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Book copies retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<Page<BookCopyResponse>> getAll(@RequestParam(defaultValue = "0")int page,@RequestParam(defaultValue = "10")int size){
        return  ResponseEntity.ok(service.getAll(page,size));
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN','MEMBER')")
    @Operation(
            summary = "Get book copy by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Book copy found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BookCopyResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Book copy not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    public ResponseEntity<BookCopyResponse>getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getById(id));
    }
    @GetMapping("/{bookId}/copies")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @Operation(
            summary = "Get book copies by book ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Book copies retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<Page<BookCopyResponse>>getByBookId(@PathVariable Long id,@RequestParam(defaultValue ="0")int page,@RequestParam(defaultValue = "10")int size){
        return ResponseEntity.ok(service.getByBookId(id,page,size));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Delete book copy"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Book copy deleted successfully"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Book copy not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    public ResponseEntity<Void>delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
     @GetMapping("/search")
     @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
     @Operation(
             summary = "Search book copies by status"
     )
     @ApiResponse(
             responseCode = "200",
             description = "Book copies found",
             content = @Content(
                     mediaType = "application/json",
                     schema = @Schema(implementation = BookCopyResponse.class)
             )
     )
    public ResponseEntity<List<BookCopyResponse>>getByAvailable(@RequestParam BookCopy.Status status){
        return ResponseEntity.ok(service.getByAvailable(status));
     }
     @PutMapping("/{id}")
     @PreAuthorize("hasRole('ADMIN')")
     @Operation(
             summary = "Update book copy"
     )
     @ApiResponse(
             responseCode = "200",
             description = "Book copy updated successfully",
             content = @Content(
                     mediaType = "application/json",
                     schema = @Schema(implementation = BookCopyResponse.class)
             )
     )
     @ApiResponse(
             responseCode = "400",
             description = "Invalid update request",
             content = @Content(
                     mediaType = "application/json",
                     schema = @Schema(implementation = ErrorResponse.class)
             )
     )
     @ApiResponse(
             responseCode = "404",
             description = "Book copy not found",
             content = @Content(
                     mediaType = "application/json",
                     schema = @Schema(implementation = ErrorResponse.class)
             )
     )
    public ResponseEntity<BookCopyResponse>update(@PathVariable Long id,@RequestBody BookCopyUpdateRequest request){
        return ResponseEntity.ok(service.update(id,request));
     }
}
