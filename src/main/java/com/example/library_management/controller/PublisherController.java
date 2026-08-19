package com.example.library_management.controller;

import com.example.library_management.dto.BookResponse;
import com.example.library_management.dto.CategoryResponse;
import com.example.library_management.dto.PublisherRequest;
import com.example.library_management.dto.PublisherResponse;
import com.example.library_management.exception.ErrorResponse;
import com.example.library_management.service.PublisherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
@PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
@SecurityRequirement(name = "bearerAuth")
public class PublisherController {
    private final PublisherService service;
    PublisherController(PublisherService service){
        this.service=service;
    }
    @PostMapping
    @Operation(summary = "create Publisher",description = "Create a new publisher")
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
            description = "Publisher created successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PublisherResponse.class)
            )
    )
    public ResponseEntity<PublisherResponse>create(@Valid@RequestBody PublisherRequest request){
        PublisherResponse response=service.create(request);
      return   ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    @Operation(summary = "Retrieve Publishers",description = "Retrieve all  publishers")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved all publisher",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PublisherResponse.class)
            )
    )
    public ResponseEntity<Page<PublisherResponse>>getAll(@RequestParam(defaultValue = "0")int page,@RequestParam(defaultValue = "10")int size){
        return ResponseEntity.ok( service.getAll(page,size));
    }
    @PutMapping("/{id}")
    @Operation(summary = "Update Publisher")
    @ApiResponse(
            responseCode = "200",
            description = "Publisher updated successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation =PublisherResponse.class)
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
            description = "Publisher not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    public ResponseEntity<PublisherResponse>update(@Valid@PathVariable Long id,@RequestBody PublisherRequest request){
        PublisherResponse response=service.update(id,request);
        return ResponseEntity.ok(response);
    }
}
