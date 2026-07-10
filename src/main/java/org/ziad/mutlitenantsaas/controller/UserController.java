package org.ziad.mutlitenantsaas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.ziad.mutlitenantsaas.dto.request.UserRequest;
import org.ziad.mutlitenantsaas.dto.response.UserResponse;
import org.ziad.mutlitenantsaas.service.UserService;

@Tag(name = "User", description = "User API")
@SecurityRequirement(name = "bearerAuth")
@Validated
@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create user", description = "Create a new user within the current tenant. Requires COMPANY_ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation failure", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient role — COMPANY_ADMIN required", content = @Content),
            @ApiResponse(responseCode = "409", description = "Username or email already exists", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> createUser(@Valid @RequestBody final UserRequest request) {
        this.userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "List users", description = "Retrieve a paginated list of all users in the current tenant. Requires COMPANY_ADMIN or ADMINISTRATOR role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of users returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient role", content = @Content)
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @ParameterObject
            @PageableDefault(size = 20, sort = "username", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(this.userService.getAllUsers(pageable));
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a single user by their ID. Requires COMPANY_ADMIN or ADMINISTRATOR role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient role", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable("userId") @NotBlank(message = "User ID must not be blank") final String userId) {
        return ResponseEntity.ok(this.userService.getUserById(userId));
    }

    @Operation(summary = "Update user", description = "Update an existing user's details. Requires COMPANY_ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "User update accepted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation failure", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient role", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> updateUser(
            @PathVariable("userId") @NotBlank(message = "User ID must not be blank") final String userId,
            @Valid @RequestBody final UserRequest request) {
        this.userService.updateUser(userId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary = "Delete user", description = "Delete a user by their ID. Requires COMPANY_ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient role", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("userId") @NotBlank(message = "User ID must not be blank") final String userId) {
        this.userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Enable user", description = "Re-enable a previously disabled user account. Requires COMPANY_ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "User enabled", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient role", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PatchMapping("/{userId}/enable")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> enableUser(
            @PathVariable("userId") @NotBlank(message = "User ID must not be blank") final String userId) {
        this.userService.enableUser(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary = "Disable user", description = "Disable a user account without deleting it. Requires COMPANY_ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "User disabled", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient role", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PatchMapping("/{userId}/disable")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> disableUser(
            @PathVariable("userId") @NotBlank(message = "User ID must not be blank") final String userId) {
        this.userService.disableUser(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
