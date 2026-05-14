package org.example.internmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.MentorRequestDTO;
import org.example.internmanagement.dto.response.MentorResponseDTO;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.service.MentorService;
import org.example.internmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentors")
@RequiredArgsConstructor
public class MentorController {

    private final MentorService mentorService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<MentorResponseDTO>> getAllMentors(
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(mentorService.getAllMentors(currentUser));
    }

    @GetMapping("/{mentor_id}")
    public ResponseEntity<MentorResponseDTO> getMentorById(
            @PathVariable("mentor_id") Integer mentorId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(mentorService.getMentorById(mentorId, currentUser));
    }

    @PostMapping
    public ResponseEntity<MentorResponseDTO> createMentor(
            @RequestBody MentorRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mentorService.createMentor(request, currentUser));
    }

    @PutMapping("/{mentor_id}")
    public ResponseEntity<MentorResponseDTO> updateMentor(
            @PathVariable("mentor_id") Integer mentorId,
            @RequestBody MentorRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(mentorService.updateMentor(mentorId, request, currentUser));
    }
}
