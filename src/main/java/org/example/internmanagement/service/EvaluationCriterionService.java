package org.example.internmanagement.service;

import org.example.internmanagement.dto.request.EvaluationCriterionRequestDTO;
import org.example.internmanagement.dto.request.EvaluationCriterionUpdateDTO;
import org.example.internmanagement.dto.response.EvaluationCriterionResponseDTO;
import org.example.internmanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EvaluationCriterionService {
    Page<EvaluationCriterionResponseDTO> getAllCriteria(User user, Pageable pageable);
    EvaluationCriterionResponseDTO getCriterionById(Integer criterionId, User user);
    EvaluationCriterionResponseDTO createCriterion(EvaluationCriterionRequestDTO request, User user);
    EvaluationCriterionResponseDTO updateCriterion(Integer criterionId, EvaluationCriterionUpdateDTO request, User user);
    void deleteCriterion(Integer criterionId, User user);
}
