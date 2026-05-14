package org.example.internmanagement.service.impl;

import lombok.RequiredArgsConstructor;
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
import org.example.internmanagement.service.RoundCriterionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoundCriterionServiceImpl implements RoundCriterionService {

    private final RoundCriterionRepository roundCriterionRepository;
    private final AssessmentRoundRepository assessmentRoundRepository;
    private final EvaluationCriterionRepository evaluationCriterionRepository;

    @Override
    public List<RoundCriterionResponseDTO> getCriteriaByRound(Integer roundId, User user) {
        if (user.getRole() != User.Role.ADMIN && user.getRole() != User.Role.MENTOR && user.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        List<RoundCriterion> criteria = roundCriterionRepository.findByRound_RoundId(roundId);
        return criteria.stream().map(RoundCriterionResponseDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public RoundCriterionResponseDTO getRoundCriterionById(Integer roundCriterionId, User user) {
        if (user.getRole() != User.Role.ADMIN && user.getRole() != User.Role.MENTOR && user.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        RoundCriterion criterion = roundCriterionRepository.findById(roundCriterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Round criterion not found with id: " + roundCriterionId));

        return RoundCriterionResponseDTO.fromEntity(criterion);
    }

    @Override
    @Transactional
    public RoundCriterionResponseDTO addCriterionToRound(RoundCriterionRequestDTO request, User user) {
        if (user.getRole() != User.Role.ADMIN) {
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

        return RoundCriterionResponseDTO.fromEntity(roundCriterionRepository.save(roundCriterion));
    }

    @Override
    @Transactional
    public RoundCriterionResponseDTO updateRoundCriterionWeight(Integer roundCriterionId, RoundCriterionRequestDTO request, User user) {
        if (user.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        RoundCriterion roundCriterion = roundCriterionRepository.findById(roundCriterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Round criterion not found with id: " + roundCriterionId));

        if (request.getWeight() != null) {
            roundCriterion.setWeight(request.getWeight());
        }

        return RoundCriterionResponseDTO.fromEntity(roundCriterionRepository.save(roundCriterion));
    }

    @Override
    @Transactional
    public void deleteRoundCriterion(Integer roundCriterionId, User user) {
        if (user.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        RoundCriterion roundCriterion = roundCriterionRepository.findById(roundCriterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Round criterion not found with id: " + roundCriterionId));

        roundCriterionRepository.delete(roundCriterion);
    }
}
