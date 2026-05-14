package org.example.internmanagement.controller;

import org.example.internmanagement.dto.request.LoginRequest;
import org.example.internmanagement.dto.response.LoginResponse;
import org.example.internmanagement.dto.response.Response;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.service.UserService;
import org.example.internmanagement.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            User user = userService.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("User not found"));

            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().toString());

            LoginResponse response = new LoginResponse(
                    token,
                    user.getUserId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getRole().toString());

            return ResponseEntity.ok(Response.<LoginResponse>builder()
                    .success(true)
                    .message("Login successful")
                    .data(response)
                    .timestamp(LocalDateTime.now())
                    .build());

        } catch (BadCredentialsException e) {
            Response<?> errorResponse = Response.builder()
                    .success(false)
                    .message("Invalid username or password")
                    .timestamp(LocalDateTime.now())
                    .errors(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            Response<?> errorResponse = Response.builder()
                    .success(false)
                    .message(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .errors(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Response.builder()
                                .success(false)
                                .message("Missing or invalid Authorization header")
                                .timestamp(LocalDateTime.now())
                                .build());
            }

            String token = authHeader.substring(7);

            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Response.builder()
                                .success(false)
                                .message("Token expired or invalid")
                                .timestamp(LocalDateTime.now())
                                .build());
            }

            String username = jwtUtil.extractUsername(token);
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new Exception("User not found"));

            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("userId", user.getUserId());
            userProfile.put("username", user.getUsername());
            userProfile.put("fullName", user.getFullName());
            userProfile.put("email", user.getEmail());
            userProfile.put("phoneNumber", user.getPhoneNumber());
            userProfile.put("role", user.getRole().toString());
            userProfile.put("isActive", user.getIsActive());
            userProfile.put("createdAt", user.getCreatedAt());
            userProfile.put("updatedAt", user.getUpdatedAt());

            return ResponseEntity.ok(Response.<Map<String, Object>>builder()
                    .success(true)
                    .message("User profile fetched successfully")
                    .data(userProfile)
                    .timestamp(LocalDateTime.now())
                    .build());

        } catch (Exception e) {
            Response<?> errorResponse = Response.builder()
                    .success(false)
                    .message("Invalid token or user not found")
                    .timestamp(LocalDateTime.now())
                    .errors(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}
