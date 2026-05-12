package org.example.internmanagement.controller;

import org.example.internmanagement.dto.request.UserRequestDTO;
import org.example.internmanagement.dto.response.UserResponseDTO;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.example.internmanagement.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(@RequestParam(required = false) User.Role role) {
        List<User> users;

        if (role != null) {
            users = userRepository.findByRole(role);
        } else {
            users = userRepository.findAll();
        }

        List<UserResponseDTO> response = users.stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer user_id) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user_id));
        return ResponseEntity.ok(UserResponseDTO.fromEntity(user));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResourceNotFoundException("Username already exists");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceNotFoundException("Email already exists");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhone());
        user.setRole(User.Role.valueOf(request.getRole()));
        user.setIsActive(request.getIsActive());
        return ResponseEntity.ok(UserResponseDTO.fromEntity(userRepository.save(user)));
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Integer user_id,
            @RequestBody UserRequestDTO request) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user_id));

        if (userRepository.findByUsername(request.getUsername()).isPresent()
                && !user.getUsername().equals(request.getUsername())) {
            throw new ResourceNotFoundException("Username already exists");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent() && !user.getEmail().equals(request.getEmail())) {
            throw new ResourceNotFoundException("Email already exists");
        }

        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhone());
        user.setRole(User.Role.valueOf(request.getRole()));
        user.setIsActive(request.getIsActive());
        return ResponseEntity.ok(UserResponseDTO.fromEntity(userRepository.save(user)));
    }

    @PutMapping("/{user_id}/status")
    public ResponseEntity<UserResponseDTO> updateStatus(@PathVariable Integer user_id, @RequestParam Boolean isActive) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user_id));
        user.setIsActive(isActive);
        return ResponseEntity.ok(UserResponseDTO.fromEntity(userRepository.save(user)));
    }

    @PutMapping("/{user_id}/role")
    public ResponseEntity<UserResponseDTO> updateRole(@PathVariable Integer user_id, @RequestParam String role) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user_id));

        if (user.getRole() == User.Role.ADMIN) {
            throw new ResourceNotFoundException("Cannot update role of admin");
        }

        user.setRole(User.Role.valueOf(role));
        return ResponseEntity.ok(UserResponseDTO.fromEntity(userRepository.save(user)));
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer user_id) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user_id));

        if (user.getRole() == User.Role.ADMIN) {
            throw new ResourceNotFoundException("Cannot delete admin");
        }

        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }
}
