package org.example.internmanagement.controller;

import org.example.internmanagement.dto.request.InternshipPhaseRequestDTO;
import org.example.internmanagement.dto.response.InternshipPhaseResponseDTO;
import org.example.internmanagement.entity.InternshipPhase;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.exception.ResourceNotFoundException;
import org.example.internmanagement.repository.InternshipPhaseRepository;
import org.example.internmanagement.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/internship_phases")
public class InternshipPhaseController {

    private final InternshipPhaseRepository internshipPhaseRepository;
    private final UserRepository userRepository;

    public InternshipPhaseController(InternshipPhaseRepository internshipPhaseRepository, UserRepository userRepository) {
        this.internshipPhaseRepository = internshipPhaseRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new ResourceNotFoundException("User not authenticated");
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @GetMapping
    public ResponseEntity<List<InternshipPhaseResponseDTO>> getAllPhases() {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN && currentUser.getRole() != User.Role.MENTOR && currentUser.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        List<InternshipPhase> phases = internshipPhaseRepository.findAll();
        return ResponseEntity.ok(phases.stream().map(InternshipPhaseResponseDTO::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/{phase_id}")
    public ResponseEntity<InternshipPhaseResponseDTO> getPhaseById(@PathVariable("phase_id") Integer phaseId) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN && currentUser.getRole() != User.Role.MENTOR && currentUser.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        InternshipPhase phase = internshipPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship phase not found with id: " + phaseId));
        return ResponseEntity.ok(InternshipPhaseResponseDTO.fromEntity(phase));
    }

    @PostMapping
    public ResponseEntity<InternshipPhaseResponseDTO> createPhase(@RequestBody InternshipPhaseRequestDTO request) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        InternshipPhase phase = new InternshipPhase();
        phase.setPhaseName(request.getPhaseName());
        phase.setStartDate(request.getStartDate());
        phase.setEndDate(request.getEndDate());
        phase.setDescription(request.getDescription());

        return ResponseEntity.ok(InternshipPhaseResponseDTO.fromEntity(internshipPhaseRepository.save(phase)));
    }

    @PutMapping("/{phase_id}")
    public ResponseEntity<InternshipPhaseResponseDTO> updatePhase(@PathVariable("phase_id") Integer phaseId,
                                                                  @RequestBody InternshipPhaseRequestDTO request) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        InternshipPhase phase = internshipPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship phase not found with id: " + phaseId));

        phase.setPhaseName(request.getPhaseName());
        phase.setStartDate(request.getStartDate());
        phase.setEndDate(request.getEndDate());
        phase.setDescription(request.getDescription());

        return ResponseEntity.ok(InternshipPhaseResponseDTO.fromEntity(internshipPhaseRepository.save(phase)));
    }

    @DeleteMapping("/{phase_id}")
    public ResponseEntity<Void> deletePhase(@PathVariable("phase_id") Integer phaseId) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        InternshipPhase phase = internshipPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship phase not found with id: " + phaseId));

        internshipPhaseRepository.delete(phase);
        return ResponseEntity.noContent().build();
    }
}
