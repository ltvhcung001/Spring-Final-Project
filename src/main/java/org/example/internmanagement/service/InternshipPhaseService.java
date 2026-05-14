package org.example.internmanagement.service;

import org.example.internmanagement.dto.request.InternshipPhaseRequestDTO;
import org.example.internmanagement.dto.request.InternshipPhaseUpdateDTO;
import org.example.internmanagement.dto.response.InternshipPhaseResponseDTO;
import org.example.internmanagement.entity.User;

import java.util.List;

public interface InternshipPhaseService {
    List<InternshipPhaseResponseDTO> getAllPhases(User user);
    InternshipPhaseResponseDTO getPhaseById(Integer phaseId, User user);
    InternshipPhaseResponseDTO createPhase(InternshipPhaseRequestDTO request, User user);
    InternshipPhaseResponseDTO updatePhase(Integer phaseId, InternshipPhaseUpdateDTO request, User user);
    void deletePhase(Integer phaseId, User user);
}
