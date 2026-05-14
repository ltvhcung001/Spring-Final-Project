package org.example.internmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.AssessmentRoundRequestDTO;
import org.example.internmanagement.dto.request.AssessmentRoundUpdateDTO;
import org.example.internmanagement.dto.response.AssessmentRoundResponseDTO;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.service.AssessmentRoundService;
import org.example.internmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import org.example.internmanagement.dto.response.PagedData;
import org.example.internmanagement.dto.response.Response;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/assessment_rounds")
@RequiredArgsConstructor
public class AssessmentRoundController {

    private final AssessmentRoundService assessmentRoundService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Response<PagedData<AssessmentRoundResponseDTO>>> getAllRounds(
            @RequestParam(required = false) Integer phaseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<PagedData<AssessmentRoundResponseDTO>>builder()
                .success(true)
                .message("Assessment rounds fetched successfully")
                .data(PagedData.from(assessmentRoundService.getAllRounds(phaseId, currentUser, PageRequest.of(page, size))))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{round_id}")
    public ResponseEntity<Response<AssessmentRoundResponseDTO>> getRoundById(
            @PathVariable("round_id") Integer roundId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<AssessmentRoundResponseDTO>builder()
                .success(true)
                .message("Assessment round fetched successfully")
                .data(assessmentRoundService.getRoundById(roundId, currentUser))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping
    public ResponseEntity<Response<AssessmentRoundResponseDTO>> createRound(
            @RequestBody AssessmentRoundRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.<AssessmentRoundResponseDTO>builder()
                        .success(true)
                        .message("Assessment round created successfully")
                        .data(assessmentRoundService.createRound(request, currentUser))
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/{round_id}")
    public ResponseEntity<Response<AssessmentRoundResponseDTO>> updateRound(
            @PathVariable("round_id") Integer roundId,
            @RequestBody AssessmentRoundUpdateDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<AssessmentRoundResponseDTO>builder()
                .success(true)
                .message("Assessment round updated successfully")
                .data(assessmentRoundService.updateRound(roundId, request, currentUser))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{round_id}")
    public ResponseEntity<Response<Void>> deleteRound(
            @PathVariable("round_id") Integer roundId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        assessmentRoundService.deleteRound(roundId, currentUser);
        return ResponseEntity.ok(Response.<Void>builder()
                .success(true)
                .message("Assessment round deleted successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }
}
