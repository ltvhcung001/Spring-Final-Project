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

import org.example.internmanagement.dto.response.Response;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/internship_phases")
@RequiredArgsConstructor
public class InternshipPhaseController {

    private final InternshipPhaseService internshipPhaseService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Response<List<InternshipPhaseResponseDTO>>> getAllPhases(
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<List<InternshipPhaseResponseDTO>>builder()
                .success(true)
                .message("Phases fetched successfully")
                .data(internshipPhaseService.getAllPhases(currentUser))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{phase_id}")
    public ResponseEntity<Response<InternshipPhaseResponseDTO>> getPhaseById(
            @PathVariable("phase_id") Integer phaseId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<InternshipPhaseResponseDTO>builder()
                .success(true)
                .message("Phase fetched successfully")
                .data(internshipPhaseService.getPhaseById(phaseId, currentUser))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping
    public ResponseEntity<Response<InternshipPhaseResponseDTO>> createPhase(
            @RequestBody InternshipPhaseRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.<InternshipPhaseResponseDTO>builder()
                        .success(true)
                        .message("Phase created successfully")
                        .data(internshipPhaseService.createPhase(request, currentUser))
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/{phase_id}")
    public ResponseEntity<Response<InternshipPhaseResponseDTO>> updatePhase(
            @PathVariable("phase_id") Integer phaseId,
            @RequestBody InternshipPhaseRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<InternshipPhaseResponseDTO>builder()
                .success(true)
                .message("Phase updated successfully")
                .data(internshipPhaseService.updatePhase(phaseId, request, currentUser))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{phase_id}")
    public ResponseEntity<Response<Void>> deletePhase(
            @PathVariable("phase_id") Integer phaseId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        internshipPhaseService.deletePhase(phaseId, currentUser);
        return ResponseEntity.ok(Response.<Void>builder()
                .success(true)
                .message("Phase deleted successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }
}
