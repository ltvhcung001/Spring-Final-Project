package org.example.internmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.RoundCriterionRequestDTO;
import org.example.internmanagement.dto.response.RoundCriterionResponseDTO;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.service.RoundCriterionService;
import org.example.internmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/round_criteria")
@RequiredArgsConstructor
public class RoundCriterionController {

    private final RoundCriterionService roundCriterionService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<RoundCriterionResponseDTO>> getCriteriaByRound(
            @RequestParam Integer roundId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(roundCriterionService.getCriteriaByRound(roundId, currentUser));
    }

    @GetMapping("/{round_criterion_id}")
    public ResponseEntity<RoundCriterionResponseDTO> getRoundCriterionById(
            @PathVariable("round_criterion_id") Integer roundCriterionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(roundCriterionService.getRoundCriterionById(roundCriterionId, currentUser));
    }

    @PostMapping
    public ResponseEntity<RoundCriterionResponseDTO> addCriterionToRound(
            @RequestBody RoundCriterionRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roundCriterionService.addCriterionToRound(request, currentUser));
    }

    @PutMapping("/{round_criterion_id}")
    public ResponseEntity<RoundCriterionResponseDTO> updateRoundCriterionWeight(
            @PathVariable("round_criterion_id") Integer roundCriterionId,
            @RequestBody RoundCriterionRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(roundCriterionService.updateRoundCriterionWeight(roundCriterionId, request, currentUser));
    }

    @DeleteMapping("/{round_criterion_id}")
    public ResponseEntity<Void> deleteRoundCriterion(
            @PathVariable("round_criterion_id") Integer roundCriterionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        roundCriterionService.deleteRoundCriterion(roundCriterionId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
