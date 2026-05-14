package org.example.internmanagement.service;

import org.example.internmanagement.dto.request.AssessmentRoundRequestDTO;
import org.example.internmanagement.dto.request.AssessmentRoundUpdateDTO;
import org.example.internmanagement.dto.response.AssessmentRoundResponseDTO;
import org.example.internmanagement.entity.User;

import java.util.List;

public interface AssessmentRoundService {
    List<AssessmentRoundResponseDTO> getAllRounds(Integer phaseId, User user);
    AssessmentRoundResponseDTO getRoundById(Integer roundId, User user);
    AssessmentRoundResponseDTO createRound(AssessmentRoundRequestDTO request, User user);
    AssessmentRoundResponseDTO updateRound(Integer roundId, AssessmentRoundUpdateDTO request, User user);
    void deleteRound(Integer roundId, User user);
}
