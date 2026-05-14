package org.example.internmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.AssessmentRoundRequestDTO;
import org.example.internmanagement.dto.request.RoundCriterionRequestDTO;
import org.example.internmanagement.dto.response.AssessmentRoundResponseDTO;
import org.example.internmanagement.dto.response.RoundCriterionResponseDTO;
import org.example.internmanagement.entity.*;
import org.example.internmanagement.exception.ResourceNotFoundException;
import org.example.internmanagement.repository.*;
import org.example.internmanagement.service.AssessmentRoundService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentRoundServiceImpl implements AssessmentRoundService {

    private final AssessmentRoundRepository assessmentRoundRepository;
    private final RoundCriterionRepository roundCriterionRepository;
    private final InternshipPhaseRepository internshipPhaseRepository;
    private final EvaluationCriterionRepository evaluationCriterionRepository;

    private AssessmentRoundResponseDTO mapToResponseDTO(AssessmentRound round) {
        List<RoundCriterion> criteria = roundCriterionRepository.findByRound_RoundId(round.getRoundId());
        List<RoundCriterionResponseDTO> criteriaDTOs = criteria.stream()
                .map(RoundCriterionResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return AssessmentRoundResponseDTO.fromEntity(round, criteriaDTOs);
    }

    @Override
    public List<AssessmentRoundResponseDTO> getAllRounds(Integer phaseId, User user) {
        if (user.getRole() != User.Role.ADMIN && user.getRole() != User.Role.MENTOR && user.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        List<AssessmentRound> rounds;
        if (phaseId != null) {
            rounds = assessmentRoundRepository.findByPhase_PhaseId(phaseId);
        } else {
            rounds = assessmentRoundRepository.findAll();
        }

        return rounds.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public AssessmentRoundResponseDTO getRoundById(Integer roundId, User user) {
        if (user.getRole() != User.Role.ADMIN && user.getRole() != User.Role.MENTOR && user.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        AssessmentRound round = assessmentRoundRepository.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment round not found with id: " + roundId));

        return mapToResponseDTO(round);
    }

    @Override
    @Transactional
    public AssessmentRoundResponseDTO createRound(AssessmentRoundRequestDTO request, User user) {
        if (user.getRole() != User.Role.ADMIN) {
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

        return mapToResponseDTO(savedRound);
    }

    @Override
    @Transactional
    public AssessmentRoundResponseDTO updateRound(Integer roundId, AssessmentRoundRequestDTO request, User user) {
        if (user.getRole() != User.Role.ADMIN) {
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

        return mapToResponseDTO(savedRound);
    }

    @Override
    @Transactional
    public void deleteRound(Integer roundId, User user) {
        if (user.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        AssessmentRound round = assessmentRoundRepository.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment round not found with id: " + roundId));

        roundCriterionRepository.deleteAllByRound_RoundId(roundId);
        assessmentRoundRepository.delete(round);
    }
}
