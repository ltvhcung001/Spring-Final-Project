package org.example.internmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.InternshipAssignmentRequestDTO;
import org.example.internmanagement.dto.request.InternshipAssignmentStatusRequestDTO;
import org.example.internmanagement.dto.response.InternshipAssignmentResponseDTO;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.exception.ResourceNotFoundException;
import org.example.internmanagement.repository.UserRepository;
import org.example.internmanagement.service.InternshipAssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internship_assignments")
@RequiredArgsConstructor
public class InternshipAssignmentController {

    private final InternshipAssignmentService internshipAssignmentService;
    private final UserRepository userRepository;

    private User getCurrentUser(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
    public ResponseEntity<List<InternshipAssignmentResponseDTO>> getAllAssignments(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        return ResponseEntity.ok(internshipAssignmentService.getAllAssignments(user));
    }

    @GetMapping("/{assignment_id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
    public ResponseEntity<InternshipAssignmentResponseDTO> getAssignmentById(
            @PathVariable("assignment_id") Integer assignmentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        return ResponseEntity.ok(internshipAssignmentService.getAssignmentById(assignmentId, user));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InternshipAssignmentResponseDTO> createAssignment(
            @RequestBody InternshipAssignmentRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(internshipAssignmentService.createAssignment(requestDTO));
    }

    @PutMapping("/{assignment_id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InternshipAssignmentResponseDTO> updateAssignmentStatus(
            @PathVariable("assignment_id") Integer assignmentId,
            @RequestBody InternshipAssignmentStatusRequestDTO requestDTO) {
        return ResponseEntity.ok(internshipAssignmentService.updateAssignmentStatus(assignmentId, requestDTO));
    }
}
