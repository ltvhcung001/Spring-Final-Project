package org.example.internmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.AssessmentRoundRequestDTO;
import org.example.internmanagement.dto.response.AssessmentRoundResponseDTO;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.service.AssessmentRoundService;
import org.example.internmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessment_rounds")
@RequiredArgsConstructor
public class AssessmentRoundController {

    private final AssessmentRoundService assessmentRoundService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<AssessmentRoundResponseDTO>> getAllRounds(
            @RequestParam(required = false) Integer phaseId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(assessmentRoundService.getAllRounds(phaseId, currentUser));
    }

    @GetMapping("/{round_id}")
    public ResponseEntity<AssessmentRoundResponseDTO> getRoundById(
            @PathVariable("round_id") Integer roundId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(assessmentRoundService.getRoundById(roundId, currentUser));
    }

    @PostMapping
    public ResponseEntity<AssessmentRoundResponseDTO> createRound(
            @RequestBody AssessmentRoundRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assessmentRoundService.createRound(request, currentUser));
    }

    @PutMapping("/{round_id}")
    public ResponseEntity<AssessmentRoundResponseDTO> updateRound(
            @PathVariable("round_id") Integer roundId,
            @RequestBody AssessmentRoundRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(assessmentRoundService.updateRound(roundId, request, currentUser));
    }

    @DeleteMapping("/{round_id}")
    public ResponseEntity<Void> deleteRound(
            @PathVariable("round_id") Integer roundId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        assessmentRoundService.deleteRound(roundId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
