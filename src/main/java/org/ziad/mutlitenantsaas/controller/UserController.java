package org.ziad.mutlitenantsaas.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.ziad.mutlitenantsaas.dto.request.UserRequest;
import org.ziad.mutlitenantsaas.dto.response.UserResponse;
import org.ziad.mutlitenantsaas.service.UserService;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> createUser(
            @Valid
            @RequestBody final UserRequest request) {
        this.userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            Pageable pageable) {
        final Page<UserResponse> response = this.userService.getAllUsers(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{user-id}")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable("user-id") final String id) {
        final UserResponse response = this.userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{user-id}")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> updateUser(
            @PathVariable("user-id") final String id,
            @Valid
            @RequestBody final UserRequest request) {
        this.userService.updateUser(id, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @DeleteMapping("/{user-id}")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("user-id") final String id) {
        this.userService.deleteUser(id);
        return ResponseEntity.noContent()
                .build();
    }

    @PutMapping("/{user-id}/enable")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> enableUser(
            @PathVariable("user-id") final String id) {
        this.userService.enableUser(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }

    @PutMapping("/{user-id}/disable")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> disableUser(
            @PathVariable("user-id") final String id) {
        this.userService.disableUser(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .build();
    }
}
