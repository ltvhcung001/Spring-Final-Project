package org.example.internmanagement.service;

import org.example.internmanagement.dto.request.RoundCriterionRequestDTO;
import org.example.internmanagement.dto.request.RoundCriterionUpdateDTO;
import org.example.internmanagement.dto.response.RoundCriterionResponseDTO;
import org.example.internmanagement.entity.User;

import java.util.List;

public interface RoundCriterionService {
    List<RoundCriterionResponseDTO> getCriteriaByRound(Integer roundId, User user);
    RoundCriterionResponseDTO getRoundCriterionById(Integer roundCriterionId, User user);
    RoundCriterionResponseDTO addCriterionToRound(RoundCriterionRequestDTO request, User user);
    RoundCriterionResponseDTO updateRoundCriterionWeight(Integer roundCriterionId, RoundCriterionUpdateDTO request, User user);
    void deleteRoundCriterion(Integer roundCriterionId, User user);
}
