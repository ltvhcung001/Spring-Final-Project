package org.example.internmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.InternshipPhaseRequestDTO;
import org.example.internmanagement.dto.response.InternshipPhaseResponseDTO;
import org.example.internmanagement.entity.InternshipPhase;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.exception.ResourceNotFoundException;
import org.example.internmanagement.repository.InternshipPhaseRepository;
import org.example.internmanagement.service.InternshipPhaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InternshipPhaseServiceImpl implements InternshipPhaseService {

    private final InternshipPhaseRepository internshipPhaseRepository;

    @Override
    public List<InternshipPhaseResponseDTO> getAllPhases(User user) {
        if (user.getRole() != User.Role.ADMIN && user.getRole() != User.Role.MENTOR && user.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        List<InternshipPhase> phases = internshipPhaseRepository.findAll();
        return phases.stream().map(InternshipPhaseResponseDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public InternshipPhaseResponseDTO getPhaseById(Integer phaseId, User user) {
        if (user.getRole() != User.Role.ADMIN && user.getRole() != User.Role.MENTOR && user.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        InternshipPhase phase = internshipPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship phase not found with id: " + phaseId));
        return InternshipPhaseResponseDTO.fromEntity(phase);
    }

    @Override
    @Transactional
    public InternshipPhaseResponseDTO createPhase(InternshipPhaseRequestDTO request, User user) {
        if (user.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        InternshipPhase phase = new InternshipPhase();
        phase.setPhaseName(request.getPhaseName());
        phase.setStartDate(request.getStartDate());
        phase.setEndDate(request.getEndDate());
        phase.setDescription(request.getDescription());

        return InternshipPhaseResponseDTO.fromEntity(internshipPhaseRepository.save(phase));
    }

    @Override
    @Transactional
    public InternshipPhaseResponseDTO updatePhase(Integer phaseId, InternshipPhaseRequestDTO request, User user) {
        if (user.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        InternshipPhase phase = internshipPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship phase not found with id: " + phaseId));

        phase.setPhaseName(request.getPhaseName());
        phase.setStartDate(request.getStartDate());
        phase.setEndDate(request.getEndDate());
        phase.setDescription(request.getDescription());

        return InternshipPhaseResponseDTO.fromEntity(internshipPhaseRepository.save(phase));
    }

    @Override
    @Transactional
    public void deletePhase(Integer phaseId, User user) {
        if (user.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        InternshipPhase phase = internshipPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship phase not found with id: " + phaseId));

        internshipPhaseRepository.delete(phase);
    }
}
