package org.example.internmanagement.controller;

import org.example.internmanagement.dto.request.MentorRequestDTO;
import org.example.internmanagement.dto.response.MentorResponseDTO;
import org.example.internmanagement.entity.Mentor;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.exception.ResourceNotFoundException;
import org.example.internmanagement.repository.MentorRepository;
import org.example.internmanagement.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mentors")
public class MentorController {

    private final MentorRepository mentorRepository;
    private final UserRepository userRepository;

    public MentorController(MentorRepository mentorRepository, UserRepository userRepository) {
        this.mentorRepository = mentorRepository;
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
    public ResponseEntity<List<MentorResponseDTO>> getAllMentors() {
        User currentUser = getCurrentUser();

        if (currentUser.getRole() != User.Role.ADMIN && currentUser.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        List<Mentor> mentors = mentorRepository.findAll();
        return ResponseEntity.ok(mentors.stream().map(MentorResponseDTO::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/{mentor_id}")
    public ResponseEntity<MentorResponseDTO> getMentorById(@PathVariable("mentor_id") Integer mentorId) {
        User currentUser = getCurrentUser();
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with id: " + mentorId));

        if (currentUser.getRole() == User.Role.MENTOR) {
            if (!mentor.getUser().getUserId().equals(currentUser.getUserId())) {
                throw new ResourceNotFoundException("Access denied: You can only view your own profile");
            }
        } else if (currentUser.getRole() != User.Role.ADMIN && currentUser.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        return ResponseEntity.ok(MentorResponseDTO.fromEntity(mentor));
    }

    @PostMapping
    public ResponseEntity<MentorResponseDTO> createMentor(@RequestBody MentorRequestDTO request) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        if (user.getRole() != User.Role.MENTOR) {
            throw new ResourceNotFoundException("User must have MENTOR role");
        }

        if (mentorRepository.findByUser_UserId(user.getUserId()).isPresent()) {
            throw new ResourceNotFoundException("User is already linked to a mentor");
        }

        Mentor mentor = new Mentor();
        mentor.setUser(user);
        mentor.setDepartment(request.getDepartment());
        mentor.setAcademicRank(request.getAcademicRank());

        return ResponseEntity.ok(MentorResponseDTO.fromEntity(mentorRepository.save(mentor)));
    }

    @PutMapping("/{mentor_id}")
    public ResponseEntity<MentorResponseDTO> updateMentor(@PathVariable("mentor_id") Integer mentorId,
                                                          @RequestBody MentorRequestDTO request) {
        User currentUser = getCurrentUser();
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with id: " + mentorId));

        if (currentUser.getRole() == User.Role.MENTOR) {
            if (!mentor.getUser().getUserId().equals(currentUser.getUserId())) {
                throw new ResourceNotFoundException("Access denied");
            }
        } else if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        mentor.setDepartment(request.getDepartment());
        mentor.setAcademicRank(request.getAcademicRank());

        return ResponseEntity.ok(MentorResponseDTO.fromEntity(mentorRepository.save(mentor)));
    }
}
