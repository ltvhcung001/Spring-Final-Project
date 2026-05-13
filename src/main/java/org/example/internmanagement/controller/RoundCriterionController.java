package org.example.internmanagement.controller;

import org.example.internmanagement.dto.request.RoundCriterionRequestDTO;
import org.example.internmanagement.dto.response.RoundCriterionResponseDTO;
import org.example.internmanagement.entity.AssessmentRound;
import org.example.internmanagement.entity.EvaluationCriterion;
import org.example.internmanagement.entity.RoundCriterion;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.exception.ResourceNotFoundException;
import org.example.internmanagement.repository.AssessmentRoundRepository;
import org.example.internmanagement.repository.EvaluationCriterionRepository;
import org.example.internmanagement.repository.RoundCriterionRepository;
import org.example.internmanagement.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/round_criteria")
public class RoundCriterionController {

    private final RoundCriterionRepository roundCriterionRepository;
    private final AssessmentRoundRepository assessmentRoundRepository;
    private final EvaluationCriterionRepository evaluationCriterionRepository;
    private final UserRepository userRepository;

    public RoundCriterionController(RoundCriterionRepository roundCriterionRepository,
                                    AssessmentRoundRepository assessmentRoundRepository,
                                    EvaluationCriterionRepository evaluationCriterionRepository,
                                    UserRepository userRepository) {
        this.roundCriterionRepository = roundCriterionRepository;
        this.assessmentRoundRepository = assessmentRoundRepository;
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
    public ResponseEntity<List<RoundCriterionResponseDTO>> getCriteriaByRound(@RequestParam Integer roundId) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN && currentUser.getRole() != User.Role.MENTOR && currentUser.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        List<RoundCriterion> criteria = roundCriterionRepository.findByRound_RoundId(roundId);
        return ResponseEntity.ok(criteria.stream().map(RoundCriterionResponseDTO::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/{round_criterion_id}")
    public ResponseEntity<RoundCriterionResponseDTO> getRoundCriterionById(@PathVariable("round_criterion_id") Integer roundCriterionId) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN && currentUser.getRole() != User.Role.MENTOR && currentUser.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        RoundCriterion criterion = roundCriterionRepository.findById(roundCriterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Round criterion not found with id: " + roundCriterionId));

        return ResponseEntity.ok(RoundCriterionResponseDTO.fromEntity(criterion));
    }

    @PostMapping
    public ResponseEntity<RoundCriterionResponseDTO> addCriterionToRound(@RequestBody RoundCriterionRequestDTO request) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        if (request.getRoundId() == null) {
            throw new ResourceNotFoundException("Round ID is required");
        }

        AssessmentRound round = assessmentRoundRepository.findById(request.getRoundId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessment round not found with id: " + request.getRoundId()));

        EvaluationCriterion criterion = evaluationCriterionRepository.findById(request.getCriterionId())
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation criterion not found with id: " + request.getCriterionId()));

        RoundCriterion roundCriterion = new RoundCriterion();
        roundCriterion.setRound(round);
        roundCriterion.setCriterion(criterion);
        roundCriterion.setWeight(request.getWeight());

        return ResponseEntity.ok(RoundCriterionResponseDTO.fromEntity(roundCriterionRepository.save(roundCriterion)));
    }

    @PutMapping("/{round_criterion_id}")
    public ResponseEntity<RoundCriterionResponseDTO> updateRoundCriterionWeight(@PathVariable("round_criterion_id") Integer roundCriterionId,
                                                                                @RequestBody RoundCriterionRequestDTO request) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        RoundCriterion roundCriterion = roundCriterionRepository.findById(roundCriterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Round criterion not found with id: " + roundCriterionId));

        if (request.getWeight() != null) {
            roundCriterion.setWeight(request.getWeight());
        }

        return ResponseEntity.ok(RoundCriterionResponseDTO.fromEntity(roundCriterionRepository.save(roundCriterion)));
    }

    @DeleteMapping("/{round_criterion_id}")
    public ResponseEntity<Void> deleteRoundCriterion(@PathVariable("round_criterion_id") Integer roundCriterionId) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        RoundCriterion roundCriterion = roundCriterionRepository.findById(roundCriterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Round criterion not found with id: " + roundCriterionId));

        roundCriterionRepository.delete(roundCriterion);

        return ResponseEntity.noContent().build();
    }
}
