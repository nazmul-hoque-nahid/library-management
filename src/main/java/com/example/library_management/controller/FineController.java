package com.example.library_management.controller;
import com.example.library_management.dto.FineRequest;
import com.example.library_management.dto.FineResponse;
import com.example.library_management.entity.Fine;
import com.example.library_management.exception.ErrorResponse;
import com.example.library_management.service.FineService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/fines")
@SecurityRequirement(name = "bearerAuth")
public class FineController {
    private final FineService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @Operation(
            summary = "Create fine",
            description = "Create a new fine for a user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Fine created successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FineResponse.class)
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
    public ResponseEntity<FineResponse> create(@Valid @RequestBody FineRequest request) {
        return ResponseEntity.ok(service.create(request));
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @Operation(
            summary = "Get all fines",
            description = "Returns paginated list of fines"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Fines retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<Page<FineResponse>> getAll(@RequestParam(defaultValue = "0")int page,@RequestParam(defaultValue = "10")int size) {
        return ResponseEntity.ok(service.getAll(page,size));
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN','MEMBER')")
    @Operation(
            summary = "Get fine by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Fine found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FineResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Fine not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    public ResponseEntity<FineResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN','MEMBER')")
    @Operation(
            summary = "Get fines by user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User fines retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FineResponse.class)
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
    public ResponseEntity<List<FineResponse>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getByUser(userId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @GetMapping("/search")
    @Operation(
            summary = "Search fines",
            description = "Search fines by status or category"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Fines retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    public ResponseEntity<Page<FineResponse>> search(
            @RequestParam(defaultValue = "0")int page,@RequestParam(defaultValue = "10")int size,
            @RequestParam(required = false) Fine.FineStatus status,
            @RequestParam(required = false) Fine.Category category) {
        if (status != null) return ResponseEntity.ok(service.getByStatus(page,size,status));
        if (category != null) return ResponseEntity.ok(service.getByCategory(page,size,category));
        return ResponseEntity.ok(service.getAll(page,size));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @PutMapping("/{id}/pay")
    @Operation(
            summary = "Pay fine",
            description = "Mark a fine as paid"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Fine paid successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FineResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Fine not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    public ResponseEntity<FineResponse> pay(@PathVariable Long id) {
        return ResponseEntity.ok(service.pay(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/waive")
    @Operation(
            summary = "Waive fine",
            description = "Waive an existing fine"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Fine waived successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FineResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Fine not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    public ResponseEntity<FineResponse> waive(@PathVariable Long id) {
        return ResponseEntity.ok(service.waive(id));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Delete fine",
            description = "Delete a fine by ID"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Fine deleted successfully"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Fine not found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}