package org.example.internmanagement.service;

import org.example.internmanagement.dto.request.InternshipPhaseRequestDTO;
import org.example.internmanagement.dto.request.InternshipPhaseUpdateDTO;
import org.example.internmanagement.dto.response.InternshipPhaseResponseDTO;
import org.example.internmanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InternshipPhaseService {
    Page<InternshipPhaseResponseDTO> getAllPhases(User user, Pageable pageable);
    InternshipPhaseResponseDTO getPhaseById(Integer phaseId, User user);
    InternshipPhaseResponseDTO createPhase(InternshipPhaseRequestDTO request, User user);
    InternshipPhaseResponseDTO updatePhase(Integer phaseId, InternshipPhaseUpdateDTO request, User user);
    void deletePhase(Integer phaseId, User user);
}
