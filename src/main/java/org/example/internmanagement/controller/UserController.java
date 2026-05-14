package org.example.internmanagement.controller;

import org.example.internmanagement.dto.request.UserRequestDTO;
import org.example.internmanagement.dto.request.UserUpdateDTO;
import org.example.internmanagement.dto.response.UserResponseDTO;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.example.internmanagement.dto.response.Response;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<List<UserResponseDTO>>> getAllUsers(@RequestParam(required = false) User.Role role) {
        return ResponseEntity.ok(Response.<List<UserResponseDTO>>builder()
                .success(true)
                .message("Users fetched successfully")
                .data(userService.getAllUsers(role))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{user_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<UserResponseDTO>> getUserById(@PathVariable Integer user_id) {
        return ResponseEntity.ok(Response.<UserResponseDTO>builder()
                .success(true)
                .message("User fetched successfully")
                .data(userService.getUserById(user_id))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<UserResponseDTO>> createUser(@RequestBody UserRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.<UserResponseDTO>builder()
                        .success(true)
                        .message("User created successfully")
                        .data(userService.createUser(request))
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/{user_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<UserResponseDTO>> updateUser(@PathVariable Integer user_id, @RequestBody UserUpdateDTO request) {
        return ResponseEntity.ok(Response.<UserResponseDTO>builder()
                .success(true)
                .message("User updated successfully")
                .data(userService.updateUser(user_id, request))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/{user_id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<UserResponseDTO>> updateStatus(@PathVariable Integer user_id, @RequestParam Boolean isActive) {
        return ResponseEntity.ok(Response.<UserResponseDTO>builder()
                .success(true)
                .message("User status updated successfully")
                .data(userService.updateStatus(user_id, isActive))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/{user_id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<UserResponseDTO>> updateRole(
            @PathVariable Integer user_id, 
            @RequestParam User.Role role,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<UserResponseDTO>builder()
                .success(true)
                .message("User role updated successfully")
                .data(userService.updateRole(user_id, role, currentUser))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{user_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<Void>> deleteUser(@PathVariable Integer user_id) {
        userService.deleteUser(user_id);
        return ResponseEntity.ok(Response.<Void>builder()
                .success(true)
                .message("User deleted successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }
}
