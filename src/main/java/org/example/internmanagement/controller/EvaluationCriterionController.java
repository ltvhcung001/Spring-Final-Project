package org.example.internmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.EvaluationCriterionRequestDTO;
import org.example.internmanagement.dto.request.EvaluationCriterionUpdateDTO;
import org.example.internmanagement.dto.response.EvaluationCriterionResponseDTO;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.service.EvaluationCriterionService;
import org.example.internmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.example.internmanagement.dto.response.Response;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/evaluation_criteria")
@RequiredArgsConstructor
public class EvaluationCriterionController {

    private final EvaluationCriterionService evaluationCriterionService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Response<List<EvaluationCriterionResponseDTO>>> getAllCriteria(
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<List<EvaluationCriterionResponseDTO>>builder()
                .success(true)
                .message("Evaluation criteria fetched successfully")
                .data(evaluationCriterionService.getAllCriteria(currentUser))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{criterion_id}")
    public ResponseEntity<Response<EvaluationCriterionResponseDTO>> getCriterionById(
            @PathVariable("criterion_id") Integer criterionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<EvaluationCriterionResponseDTO>builder()
                .success(true)
                .message("Evaluation criterion fetched successfully")
                .data(evaluationCriterionService.getCriterionById(criterionId, currentUser))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping
    public ResponseEntity<Response<EvaluationCriterionResponseDTO>> createCriterion(
            @RequestBody EvaluationCriterionRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.<EvaluationCriterionResponseDTO>builder()
                        .success(true)
                        .message("Evaluation criterion created successfully")
                        .data(evaluationCriterionService.createCriterion(request, currentUser))
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/{criterion_id}")
    public ResponseEntity<Response<EvaluationCriterionResponseDTO>> updateCriterion(
            @PathVariable("criterion_id") Integer criterionId,
            @RequestBody EvaluationCriterionUpdateDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<EvaluationCriterionResponseDTO>builder()
                .success(true)
                .message("Evaluation criterion updated successfully")
                .data(evaluationCriterionService.updateCriterion(criterionId, request, currentUser))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{criterion_id}")
    public ResponseEntity<Response<Void>> deleteCriterion(
            @PathVariable("criterion_id") Integer criterionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        evaluationCriterionService.deleteCriterion(criterionId, currentUser);
        return ResponseEntity.ok(Response.<Void>builder()
                .success(true)
                .message("Evaluation criterion deleted successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }
}
