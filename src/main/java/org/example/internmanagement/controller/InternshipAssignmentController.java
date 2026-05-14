package org.example.internmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.InternshipAssignmentRequestDTO;
import org.example.internmanagement.dto.request.InternshipAssignmentStatusRequestDTO;
import org.example.internmanagement.dto.response.InternshipAssignmentResponseDTO;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.service.InternshipAssignmentService;
import org.example.internmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import org.example.internmanagement.dto.response.PagedData;
import org.example.internmanagement.dto.response.Response;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/internship_assignments")
@RequiredArgsConstructor
public class InternshipAssignmentController {

    private final InternshipAssignmentService internshipAssignmentService;
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
    public ResponseEntity<Response<PagedData<InternshipAssignmentResponseDTO>>> getAllAssignments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<PagedData<InternshipAssignmentResponseDTO>>builder()
                .success(true)
                .message("Assignments fetched successfully")
                .data(PagedData.from(internshipAssignmentService.getAllAssignments(user, PageRequest.of(page, size))))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{assignment_id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
    public ResponseEntity<Response<InternshipAssignmentResponseDTO>> getAssignmentById(
            @PathVariable("assignment_id") Integer assignmentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<InternshipAssignmentResponseDTO>builder()
                .success(true)
                .message("Assignment fetched successfully")
                .data(internshipAssignmentService.getAssignmentById(assignmentId, user))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<InternshipAssignmentResponseDTO>> createAssignment(
            @RequestBody InternshipAssignmentRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.<InternshipAssignmentResponseDTO>builder()
                        .success(true)
                        .message("Assignment created successfully")
                        .data(internshipAssignmentService.createAssignment(requestDTO))
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/{assignment_id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<InternshipAssignmentResponseDTO>> updateAssignmentStatus(
            @PathVariable("assignment_id") Integer assignmentId,
            @RequestBody InternshipAssignmentStatusRequestDTO requestDTO) {
        return ResponseEntity.ok(Response.<InternshipAssignmentResponseDTO>builder()
                .success(true)
                .message("Assignment status updated successfully")
                .data(internshipAssignmentService.updateAssignmentStatus(assignmentId, requestDTO))
                .timestamp(LocalDateTime.now())
                .build());
    }
}
