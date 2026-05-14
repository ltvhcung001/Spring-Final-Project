package org.example.internmanagement.service;

import org.example.internmanagement.dto.request.AssessmentRoundRequestDTO;
import org.example.internmanagement.dto.request.AssessmentRoundUpdateDTO;
import org.example.internmanagement.dto.response.AssessmentRoundResponseDTO;
import org.example.internmanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssessmentRoundService {
    Page<AssessmentRoundResponseDTO> getAllRounds(Integer phaseId, User user, Pageable pageable);
    AssessmentRoundResponseDTO getRoundById(Integer roundId, User user);
    AssessmentRoundResponseDTO createRound(AssessmentRoundRequestDTO request, User user);
    AssessmentRoundResponseDTO updateRound(Integer roundId, AssessmentRoundUpdateDTO request, User user);
    void deleteRound(Integer roundId, User user);
}
