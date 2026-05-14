package org.example.internmanagement.controller;

import org.example.internmanagement.dto.request.UserRequestDTO;
import org.example.internmanagement.dto.response.UserResponseDTO;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(@RequestParam(required = false) User.Role role) {
        return ResponseEntity.ok(userService.getAllUsers(role));
    }

    @GetMapping("/{user_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer user_id) {
        return ResponseEntity.ok(userService.getUserById(user_id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @PutMapping("/{user_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Integer user_id, @RequestBody UserRequestDTO request) {
        return ResponseEntity.ok(userService.updateUser(user_id, request));
    }

    @PutMapping("/{user_id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateStatus(@PathVariable Integer user_id, @RequestParam Boolean isActive) {
        return ResponseEntity.ok(userService.updateStatus(user_id, isActive));
    }

    @DeleteMapping("/{user_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer user_id) {
        userService.deleteUser(user_id);
        return ResponseEntity.noContent().build();
    }
}
