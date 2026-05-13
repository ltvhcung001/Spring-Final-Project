package org.example.internmanagement.controller;

import org.example.internmanagement.dto.request.EvaluationCriterionRequestDTO;
import org.example.internmanagement.dto.response.EvaluationCriterionResponseDTO;
import org.example.internmanagement.entity.EvaluationCriterion;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.exception.ResourceNotFoundException;
import org.example.internmanagement.repository.EvaluationCriterionRepository;
import org.example.internmanagement.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/evaluation_criteria")
public class EvaluationCriterionController {

    private final EvaluationCriterionRepository evaluationCriterionRepository;
    private final UserRepository userRepository;

    public EvaluationCriterionController(EvaluationCriterionRepository evaluationCriterionRepository, UserRepository userRepository) {
        this.evaluationCriterionRepository = evaluationCriterionRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new ResourceNotFoundException("User not authenticated");
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @GetMapping
    public ResponseEntity<List<EvaluationCriterionResponseDTO>> getAllCriteria() {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN && currentUser.getRole() != User.Role.MENTOR && currentUser.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        List<EvaluationCriterion> criteria = evaluationCriterionRepository.findAll();
        return ResponseEntity.ok(criteria.stream().map(EvaluationCriterionResponseDTO::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/{criterion_id}")
    public ResponseEntity<EvaluationCriterionResponseDTO> getCriterionById(@PathVariable("criterion_id") Integer criterionId) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN && currentUser.getRole() != User.Role.MENTOR && currentUser.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        EvaluationCriterion criterion = evaluationCriterionRepository.findById(criterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation criterion not found with id: " + criterionId));
        return ResponseEntity.ok(EvaluationCriterionResponseDTO.fromEntity(criterion));
    }

    @PostMapping
    public ResponseEntity<EvaluationCriterionResponseDTO> createCriterion(@RequestBody EvaluationCriterionRequestDTO request) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        if (evaluationCriterionRepository.existsByCriterionName(request.getCriterionName())) {
            throw new ResourceNotFoundException("Criterion name already exists");
        }

        EvaluationCriterion criterion = new EvaluationCriterion();
        criterion.setCriterionName(request.getCriterionName());
        criterion.setDescription(request.getDescription());
        criterion.setMaxScore(request.getMaxScore());

        return ResponseEntity.ok(EvaluationCriterionResponseDTO.fromEntity(evaluationCriterionRepository.save(criterion)));
    }

    @PutMapping("/{criterion_id}")
    public ResponseEntity<EvaluationCriterionResponseDTO> updateCriterion(@PathVariable("criterion_id") Integer criterionId,
                                                                          @RequestBody EvaluationCriterionRequestDTO request) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        EvaluationCriterion criterion = evaluationCriterionRepository.findById(criterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation criterion not found with id: " + criterionId));

        if (!criterion.getCriterionName().equals(request.getCriterionName()) &&
                evaluationCriterionRepository.existsByCriterionName(request.getCriterionName())) {
            throw new ResourceNotFoundException("Criterion name already exists");
        }

        criterion.setCriterionName(request.getCriterionName());
        criterion.setDescription(request.getDescription());
        criterion.setMaxScore(request.getMaxScore());

        return ResponseEntity.ok(EvaluationCriterionResponseDTO.fromEntity(evaluationCriterionRepository.save(criterion)));
    }

    @DeleteMapping("/{criterion_id}")
    public ResponseEntity<Void> deleteCriterion(@PathVariable("criterion_id") Integer criterionId) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        EvaluationCriterion criterion = evaluationCriterionRepository.findById(criterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation criterion not found with id: " + criterionId));

        evaluationCriterionRepository.delete(criterion);
        return ResponseEntity.noContent().build();
    }
}
