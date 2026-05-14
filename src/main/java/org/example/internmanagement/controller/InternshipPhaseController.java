package org.example.internmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.InternshipPhaseRequestDTO;
import org.example.internmanagement.dto.response.InternshipPhaseResponseDTO;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.service.InternshipPhaseService;
import org.example.internmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internship_phases")
@RequiredArgsConstructor
public class InternshipPhaseController {

    private final InternshipPhaseService internshipPhaseService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<InternshipPhaseResponseDTO>> getAllPhases(
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(internshipPhaseService.getAllPhases(currentUser));
    }

    @GetMapping("/{phase_id}")
    public ResponseEntity<InternshipPhaseResponseDTO> getPhaseById(
            @PathVariable("phase_id") Integer phaseId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(internshipPhaseService.getPhaseById(phaseId, currentUser));
    }

    @PostMapping
    public ResponseEntity<InternshipPhaseResponseDTO> createPhase(
            @RequestBody InternshipPhaseRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(internshipPhaseService.createPhase(request, currentUser));
    }

    @PutMapping("/{phase_id}")
    public ResponseEntity<InternshipPhaseResponseDTO> updatePhase(
            @PathVariable("phase_id") Integer phaseId,
            @RequestBody InternshipPhaseRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(internshipPhaseService.updatePhase(phaseId, request, currentUser));
    }

    @DeleteMapping("/{phase_id}")
    public ResponseEntity<Void> deletePhase(
            @PathVariable("phase_id") Integer phaseId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        internshipPhaseService.deletePhase(phaseId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
