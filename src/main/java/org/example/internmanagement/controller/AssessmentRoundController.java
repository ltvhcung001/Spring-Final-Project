package org.example.internmanagement.controller;

import org.example.internmanagement.dto.request.AssessmentRoundRequestDTO;
import org.example.internmanagement.dto.request.RoundCriterionRequestDTO;
import org.example.internmanagement.dto.response.AssessmentRoundResponseDTO;
import org.example.internmanagement.dto.response.RoundCriterionResponseDTO;
import org.example.internmanagement.entity.*;
import org.example.internmanagement.exception.ResourceNotFoundException;
import org.example.internmanagement.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assessment_rounds")
public class AssessmentRoundController {

    private final AssessmentRoundRepository assessmentRoundRepository;
    private final RoundCriterionRepository roundCriterionRepository;
    private final InternshipPhaseRepository internshipPhaseRepository;
    private final EvaluationCriterionRepository evaluationCriterionRepository;
    private final UserRepository userRepository;

    public AssessmentRoundController(AssessmentRoundRepository assessmentRoundRepository,
                                     RoundCriterionRepository roundCriterionRepository,
                                     InternshipPhaseRepository internshipPhaseRepository,
                                     EvaluationCriterionRepository evaluationCriterionRepository,
                                     UserRepository userRepository) {
        this.assessmentRoundRepository = assessmentRoundRepository;
        this.roundCriterionRepository = roundCriterionRepository;
        this.internshipPhaseRepository = internshipPhaseRepository;
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

    private AssessmentRoundResponseDTO mapToResponseDTO(AssessmentRound round) {
        List<RoundCriterion> criteria = roundCriterionRepository.findByRound_RoundId(round.getRoundId());
        List<RoundCriterionResponseDTO> criteriaDTOs = criteria.stream()
                .map(RoundCriterionResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return AssessmentRoundResponseDTO.fromEntity(round, criteriaDTOs);
    }

    @GetMapping
    public ResponseEntity<List<AssessmentRoundResponseDTO>> getAllRounds(@RequestParam(required = false) Integer phaseId) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN && currentUser.getRole() != User.Role.MENTOR && currentUser.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        List<AssessmentRound> rounds;
        if (phaseId != null) {
            rounds = assessmentRoundRepository.findByPhase_PhaseId(phaseId);
        } else {
            rounds = assessmentRoundRepository.findAll();
        }

        return ResponseEntity.ok(rounds.stream().map(this::mapToResponseDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{round_id}")
    public ResponseEntity<AssessmentRoundResponseDTO> getRoundById(@PathVariable("round_id") Integer roundId) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN && currentUser.getRole() != User.Role.MENTOR && currentUser.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        AssessmentRound round = assessmentRoundRepository.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment round not found with id: " + roundId));

        return ResponseEntity.ok(mapToResponseDTO(round));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<AssessmentRoundResponseDTO> createRound(@RequestBody AssessmentRoundRequestDTO request) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        InternshipPhase phase = internshipPhaseRepository.findById(request.getPhaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Phase not found with id: " + request.getPhaseId()));

        AssessmentRound round = new AssessmentRound();
        round.setPhase(phase);
        round.setRoundName(request.getRoundName());
        round.setStartDate(request.getStartDate());
        round.setEndDate(request.getEndDate());
        round.setDescription(request.getDescription());
        if (request.getIsActive() != null) {
            round.setIsActive(request.getIsActive());
        }

        AssessmentRound savedRound = assessmentRoundRepository.save(round);

        if (request.getCriteria() != null && !request.getCriteria().isEmpty()) {
            for (RoundCriterionRequestDTO criterionRequest : request.getCriteria()) {
                EvaluationCriterion criterion = evaluationCriterionRepository.findById(criterionRequest.getCriterionId())
                        .orElseThrow(() -> new ResourceNotFoundException("Evaluation criterion not found with id: " + criterionRequest.getCriterionId()));
                
                RoundCriterion roundCriterion = new RoundCriterion();
                roundCriterion.setRound(savedRound);
                roundCriterion.setCriterion(criterion);
                roundCriterion.setWeight(criterionRequest.getWeight());
                roundCriterionRepository.save(roundCriterion);
            }
        }

        return ResponseEntity.ok(mapToResponseDTO(savedRound));
    }

    @PutMapping("/{round_id}")
    @Transactional
    public ResponseEntity<AssessmentRoundResponseDTO> updateRound(@PathVariable("round_id") Integer roundId,
                                                                  @RequestBody AssessmentRoundRequestDTO request) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        AssessmentRound round = assessmentRoundRepository.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment round not found with id: " + roundId));

        if (request.getPhaseId() != null) {
            InternshipPhase phase = internshipPhaseRepository.findById(request.getPhaseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Phase not found with id: " + request.getPhaseId()));
            round.setPhase(phase);
        }

        round.setRoundName(request.getRoundName());
        round.setStartDate(request.getStartDate());
        round.setEndDate(request.getEndDate());
        round.setDescription(request.getDescription());
        if (request.getIsActive() != null) {
            round.setIsActive(request.getIsActive());
        }

        AssessmentRound savedRound = assessmentRoundRepository.save(round);

        if (request.getCriteria() != null) {
            roundCriterionRepository.deleteAllByRound_RoundId(roundId);
            
            for (RoundCriterionRequestDTO criterionRequest : request.getCriteria()) {
                EvaluationCriterion criterion = evaluationCriterionRepository.findById(criterionRequest.getCriterionId())
                        .orElseThrow(() -> new ResourceNotFoundException("Evaluation criterion not found with id: " + criterionRequest.getCriterionId()));
                
                RoundCriterion roundCriterion = new RoundCriterion();
                roundCriterion.setRound(savedRound);
                roundCriterion.setCriterion(criterion);
                roundCriterion.setWeight(criterionRequest.getWeight());
                roundCriterionRepository.save(roundCriterion);
            }
        }

        return ResponseEntity.ok(mapToResponseDTO(savedRound));
    }

    @DeleteMapping("/{round_id}")
    @Transactional
    public ResponseEntity<Void> deleteRound(@PathVariable("round_id") Integer roundId) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        AssessmentRound round = assessmentRoundRepository.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment round not found with id: " + roundId));

        roundCriterionRepository.deleteAllByRound_RoundId(roundId);
        assessmentRoundRepository.delete(round);

        return ResponseEntity.noContent().build();
    }
}
