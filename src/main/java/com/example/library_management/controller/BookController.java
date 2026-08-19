package com.example.library_management.controller;

import com.example.library_management.dto.BookRequest;
import com.example.library_management.dto.BookResponse;
import com.example.library_management.exception.ErrorResponse;
import com.example.library_management.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@Tag(name = "Book",description = "Books management APIs")
@SecurityRequirement(name = "bearerAuth")
public class BookController {
    private final BookService bookService;
    @PostMapping
    @Operation(summary = "create book",description = "Create a new book")
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
            description = "Book created successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BookResponse.class)
            )
    )
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public ResponseEntity<BookResponse> create(@Valid@RequestBody BookRequest request){
        BookResponse response= bookService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    @Operation(
            summary = "Get all books",
            description = "Returns a paginated list of books"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Books retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<Page<BookResponse>>getAllBooks(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "10")int size){
        return ResponseEntity.ok(bookService.getAllBooks(page,size));
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID")
    @ApiResponse(
            responseCode = "200",
            description = "Book found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BookResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Book not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    public ResponseEntity<BookResponse>getById(@PathVariable Long id){
        BookResponse response=bookService.getBookById(id);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete book"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Book deleted successfully"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Book not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void>deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('MEMBER', 'LIBRARIAN', 'ADMIN')")
    @Operation(
            summary = "Search  book"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Book retrieved",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BookResponse.class)
            )
    )
    public ResponseEntity<Page<BookResponse>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false)String publisherName,
            @RequestParam(required = false) Long publisherId,
            @RequestParam(required = false) Boolean available,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<BookResponse> result = bookService.search(
                title,
                isbn,
                authorName,
                authorId,
                categoryName,
                categoryId,
                publisherId,
                publisherName,
                available,
                page,
                size
        );
        return ResponseEntity.ok(result);
    }
}
