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

import org.example.internmanagement.dto.response.Response;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/round_criteria")
@RequiredArgsConstructor
public class RoundCriterionController {

    private final RoundCriterionService roundCriterionService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Response<List<RoundCriterionResponseDTO>>> getCriteriaByRound(
            @RequestParam Integer roundId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<List<RoundCriterionResponseDTO>>builder()
                .success(true)
                .message("Round criteria fetched successfully")
                .data(roundCriterionService.getCriteriaByRound(roundId, currentUser))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{round_criterion_id}")
    public ResponseEntity<Response<RoundCriterionResponseDTO>> getRoundCriterionById(
            @PathVariable("round_criterion_id") Integer roundCriterionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<RoundCriterionResponseDTO>builder()
                .success(true)
                .message("Round criterion fetched successfully")
                .data(roundCriterionService.getRoundCriterionById(roundCriterionId, currentUser))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping
    public ResponseEntity<Response<RoundCriterionResponseDTO>> addCriterionToRound(
            @RequestBody RoundCriterionRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.<RoundCriterionResponseDTO>builder()
                        .success(true)
                        .message("Criterion added to round successfully")
                        .data(roundCriterionService.addCriterionToRound(request, currentUser))
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/{round_criterion_id}")
    public ResponseEntity<Response<RoundCriterionResponseDTO>> updateRoundCriterionWeight(
            @PathVariable("round_criterion_id") Integer roundCriterionId,
            @RequestBody RoundCriterionRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<RoundCriterionResponseDTO>builder()
                .success(true)
                .message("Round criterion weight updated successfully")
                .data(roundCriterionService.updateRoundCriterionWeight(roundCriterionId, request, currentUser))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{round_criterion_id}")
    public ResponseEntity<Response<Void>> deleteRoundCriterion(
            @PathVariable("round_criterion_id") Integer roundCriterionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        roundCriterionService.deleteRoundCriterion(roundCriterionId, currentUser);
        return ResponseEntity.ok(Response.<Void>builder()
                .success(true)
                .message("Round criterion deleted successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }
}
