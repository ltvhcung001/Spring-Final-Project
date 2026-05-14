package org.example.internmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.EvaluationCriterionRequestDTO;
import org.example.internmanagement.dto.request.EvaluationCriterionUpdateDTO;
import org.example.internmanagement.dto.response.EvaluationCriterionResponseDTO;
import org.example.internmanagement.entity.EvaluationCriterion;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.exception.DuplicateResourceException;
import org.example.internmanagement.exception.ResourceNotFoundException;
import org.example.internmanagement.repository.EvaluationCriterionRepository;
import org.example.internmanagement.service.EvaluationCriterionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluationCriterionServiceImpl implements EvaluationCriterionService {

    private final EvaluationCriterionRepository evaluationCriterionRepository;

    @Override
    public Page<EvaluationCriterionResponseDTO> getAllCriteria(User user, Pageable pageable) {
        if (user.getRole() != User.Role.ADMIN && user.getRole() != User.Role.MENTOR && user.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        Page<EvaluationCriterion> criteria = evaluationCriterionRepository.findAll(pageable);
        return criteria.map(EvaluationCriterionResponseDTO::fromEntity);
    }

    @Override
    public EvaluationCriterionResponseDTO getCriterionById(Integer criterionId, User user) {
        if (user.getRole() != User.Role.ADMIN && user.getRole() != User.Role.MENTOR && user.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        EvaluationCriterion criterion = evaluationCriterionRepository.findById(criterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation criterion not found with id: " + criterionId));
        return EvaluationCriterionResponseDTO.fromEntity(criterion);
    }

    @Override
    @Transactional
    public EvaluationCriterionResponseDTO createCriterion(EvaluationCriterionRequestDTO request, User user) {
        if (user.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        if (evaluationCriterionRepository.existsByCriterionName(request.getCriterionName())) {
            throw new DuplicateResourceException("Criterion name already exists");
        }

        EvaluationCriterion criterion = new EvaluationCriterion();
        criterion.setCriterionName(request.getCriterionName());
        criterion.setDescription(request.getDescription());
        criterion.setMaxScore(request.getMaxScore());

        return EvaluationCriterionResponseDTO.fromEntity(evaluationCriterionRepository.save(criterion));
    }

    @Override
    @Transactional
    public EvaluationCriterionResponseDTO updateCriterion(Integer criterionId, EvaluationCriterionUpdateDTO request, User user) {
        if (user.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        EvaluationCriterion criterion = evaluationCriterionRepository.findById(criterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation criterion not found with id: " + criterionId));

        if (request.getCriterionName() != null && !criterion.getCriterionName().equals(request.getCriterionName()) &&
                evaluationCriterionRepository.existsByCriterionName(request.getCriterionName())) {
            throw new DuplicateResourceException("Criterion name already exists");
        }

        if (request.getCriterionName() != null) criterion.setCriterionName(request.getCriterionName());
        if (request.getDescription() != null) criterion.setDescription(request.getDescription());
        if (request.getMaxScore() != null) criterion.setMaxScore(request.getMaxScore());

        return EvaluationCriterionResponseDTO.fromEntity(evaluationCriterionRepository.save(criterion));
    }

    @Override
    @Transactional
    public void deleteCriterion(Integer criterionId, User user) {
        if (user.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        EvaluationCriterion criterion = evaluationCriterionRepository.findById(criterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation criterion not found with id: " + criterionId));

        evaluationCriterionRepository.delete(criterion);
    }
}
