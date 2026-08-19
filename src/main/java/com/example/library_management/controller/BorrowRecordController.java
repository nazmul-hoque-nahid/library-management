package com.example.library_management.controller;

import com.example.library_management.dto.BorrowRequest;
import com.example.library_management.dto.BorrowResponse;
import com.example.library_management.entity.BorrowRecord;
import com.example.library_management.exception.ErrorResponse;
import com.example.library_management.service.BorrowRecordService;
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
@RequestMapping("/api/borrow-records")
@Tag(name = "Book Borrow")
@SecurityRequirement(name = "bearerAuth")
public class BorrowRecordController {
    private final BorrowRecordService service;
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Create borrow record",
            description = "Create a new borrow record"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Borrow record created successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BorrowResponse.class)
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
    public ResponseEntity<BorrowResponse> create(@Valid @RequestBody BorrowRequest request) {
        BorrowResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @GetMapping
    @Operation(
            summary = "Get all borrow records",
            description = "Returns a paginated list of borrow records"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Borrow records retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<Page<BorrowResponse>> getAll(@RequestParam(defaultValue = "0")int page,@RequestParam(defaultValue = "10")int size) {

        return ResponseEntity.ok(service.getAll(page,size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get borrow record by ID")
    @ApiResponse(
            responseCode = "200",
            description = "Borrow record found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BorrowResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Borrow record not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    public ResponseEntity<BorrowResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PutMapping("/{id}/return")
    @Operation(summary = "Return borrowed book")
    @ApiResponse(
            responseCode = "200",
            description = "Book returned successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BorrowResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Borrow record not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    public ResponseEntity<BorrowResponse> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(service.returnBook(id));
    }

    @GetMapping("/{userId}/borrows")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN','MEMBER')")
    @Operation(summary = "Get borrow records by user")
    @ApiResponse(
            responseCode = "200",
            description = "Borrow records retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BorrowResponse.class)
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
    public ResponseEntity<List<BorrowResponse>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getByUser(userId));
    }

    @GetMapping("/book-copy/{copyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @Operation(summary = "Get borrow records by book copy")
    @ApiResponse(
            responseCode = "200",
            description = "Borrow records retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BorrowResponse.class)
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
    public ResponseEntity<List<BorrowResponse>> getByBookCopy(@PathVariable Long copyId) {
        return ResponseEntity.ok(service.getByBookCopy(copyId));
    }


    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @Operation(summary = "Search borrow records by status")
    @ApiResponse(
            responseCode = "200",
            description = "Borrow records found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BorrowResponse.class)
            )
    )
    public ResponseEntity<List<BorrowResponse>> getByStatus(@RequestParam BorrowRecord.Status status) {
        return ResponseEntity.ok(service.getByStatus(status));
    }


    @GetMapping("/overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @ApiResponse(
            responseCode = "200",
            description = "Overdue books retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BorrowResponse.class)
            )
    )
    public ResponseEntity<List<BorrowResponse>> getOverdueBooks() {
        return ResponseEntity.ok(service.getOverdueBooks());
    }
}