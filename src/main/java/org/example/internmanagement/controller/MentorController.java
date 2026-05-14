package org.example.internmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.MentorRequestDTO;
import org.example.internmanagement.dto.request.MentorUpdateDTO;
import org.example.internmanagement.dto.response.MentorResponseDTO;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.service.MentorService;
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
@RequestMapping("/api/mentors")
@RequiredArgsConstructor
public class MentorController {

    private final MentorService mentorService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Response<PagedData<MentorResponseDTO>>> getAllMentors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<PagedData<MentorResponseDTO>>builder()
                .success(true)
                .message("Mentors fetched successfully")
                .data(PagedData.from(mentorService.getAllMentors(currentUser, PageRequest.of(page, size))))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{mentor_id}")
    public ResponseEntity<Response<MentorResponseDTO>> getMentorById(
            @PathVariable("mentor_id") Integer mentorId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<MentorResponseDTO>builder()
                .success(true)
                .message("Mentor fetched successfully")
                .data(mentorService.getMentorById(mentorId, currentUser))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping
    public ResponseEntity<Response<MentorResponseDTO>> createMentor(
            @RequestBody MentorRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.<MentorResponseDTO>builder()
                        .success(true)
                        .message("Mentor created successfully")
                        .data(mentorService.createMentor(request, currentUser))
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/{mentor_id}")
    public ResponseEntity<Response<MentorResponseDTO>> updateMentor(
            @PathVariable("mentor_id") Integer mentorId,
            @RequestBody MentorUpdateDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<MentorResponseDTO>builder()
                .success(true)
                .message("Mentor updated successfully")
                .data(mentorService.updateMentor(mentorId, request, currentUser))
                .timestamp(LocalDateTime.now())
                .build());
    }
}
