package org.example.internmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.AssessmentResultRequestDTO;
import org.example.internmanagement.dto.request.AssessmentResultUpdateDTO;
import org.example.internmanagement.dto.response.AssessmentResultResponseDTO;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.service.AssessmentResultService;
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
@RequestMapping("/api/assessment_results")
@RequiredArgsConstructor
public class AssessmentResultController {

    private final AssessmentResultService assessmentResultService;
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
    public ResponseEntity<Response<List<AssessmentResultResponseDTO>>> getAssessmentResults(
            @RequestParam(value = "assignment_id", required = false) Integer assignmentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<List<AssessmentResultResponseDTO>>builder()
                .success(true)
                .message("Assessment results fetched successfully")
                .data(assessmentResultService.getAssessmentResults(assignmentId, user))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<Response<AssessmentResultResponseDTO>> createAssessmentResult(
            @RequestBody AssessmentResultRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getCurrentUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.<AssessmentResultResponseDTO>builder()
                        .success(true)
                        .message("Assessment result created successfully")
                        .data(assessmentResultService.createAssessmentResult(requestDTO, user))
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/{result_id}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<Response<AssessmentResultResponseDTO>> updateAssessmentResult(
            @PathVariable("result_id") Integer resultId,
            @RequestBody AssessmentResultUpdateDTO requestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<AssessmentResultResponseDTO>builder()
                .success(true)
                .message("Assessment result updated successfully")
                .data(assessmentResultService.updateAssessmentResult(resultId, requestDTO, user))
                .timestamp(LocalDateTime.now())
                .build());
    }
}
