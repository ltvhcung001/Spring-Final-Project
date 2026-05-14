package org.example.internmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.AssessmentResultRequestDTO;
import org.example.internmanagement.dto.response.AssessmentResultResponseDTO;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.exception.ResourceNotFoundException;
import org.example.internmanagement.repository.UserRepository;
import org.example.internmanagement.service.AssessmentResultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessment_results")
@RequiredArgsConstructor
public class AssessmentResultController {

    private final AssessmentResultService assessmentResultService;
    private final UserRepository userRepository;

    private User getCurrentUser(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
    public ResponseEntity<List<AssessmentResultResponseDTO>> getAssessmentResults(
            @RequestParam(value = "assignment_id", required = false) Integer assignmentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        return ResponseEntity.ok(assessmentResultService.getAssessmentResults(assignmentId, user));
    }

    @PostMapping
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<AssessmentResultResponseDTO> createAssessmentResult(
            @RequestBody AssessmentResultRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assessmentResultService.createAssessmentResult(requestDTO, user));
    }

    @PutMapping("/{result_id}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<AssessmentResultResponseDTO> updateAssessmentResult(
            @PathVariable("result_id") Integer resultId,
            @RequestBody AssessmentResultRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        return ResponseEntity.ok(assessmentResultService.updateAssessmentResult(resultId, requestDTO, user));
    }
}
