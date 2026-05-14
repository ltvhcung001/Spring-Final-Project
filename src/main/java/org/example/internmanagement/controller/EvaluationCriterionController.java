package org.example.internmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.EvaluationCriterionRequestDTO;
import org.example.internmanagement.dto.response.EvaluationCriterionResponseDTO;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.service.EvaluationCriterionService;
import org.example.internmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluation_criteria")
@RequiredArgsConstructor
public class EvaluationCriterionController {

    private final EvaluationCriterionService evaluationCriterionService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<EvaluationCriterionResponseDTO>> getAllCriteria(
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(evaluationCriterionService.getAllCriteria(currentUser));
    }

    @GetMapping("/{criterion_id}")
    public ResponseEntity<EvaluationCriterionResponseDTO> getCriterionById(
            @PathVariable("criterion_id") Integer criterionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(evaluationCriterionService.getCriterionById(criterionId, currentUser));
    }

    @PostMapping
    public ResponseEntity<EvaluationCriterionResponseDTO> createCriterion(
            @RequestBody EvaluationCriterionRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(evaluationCriterionService.createCriterion(request, currentUser));
    }

    @PutMapping("/{criterion_id}")
    public ResponseEntity<EvaluationCriterionResponseDTO> updateCriterion(
            @PathVariable("criterion_id") Integer criterionId,
            @RequestBody EvaluationCriterionRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(evaluationCriterionService.updateCriterion(criterionId, request, currentUser));
    }

    @DeleteMapping("/{criterion_id}")
    public ResponseEntity<Void> deleteCriterion(
            @PathVariable("criterion_id") Integer criterionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        evaluationCriterionService.deleteCriterion(criterionId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
