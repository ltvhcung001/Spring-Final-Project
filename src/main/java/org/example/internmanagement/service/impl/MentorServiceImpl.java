package org.example.internmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.MentorRequestDTO;
import org.example.internmanagement.dto.request.MentorUpdateDTO;
import org.example.internmanagement.dto.response.MentorResponseDTO;
import org.example.internmanagement.entity.Mentor;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.exception.DuplicateResourceException;
import org.example.internmanagement.exception.ResourceNotFoundException;
import org.example.internmanagement.repository.MentorRepository;
import org.example.internmanagement.repository.UserRepository;
import org.example.internmanagement.service.MentorService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {

    private final MentorRepository mentorRepository;
    private final UserRepository userRepository;

    @Override
    public Page<MentorResponseDTO> getAllMentors(User user, Pageable pageable) {
        if (user.getRole() != User.Role.ADMIN && user.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        Page<Mentor> mentors = mentorRepository.findAll(pageable);
        return mentors.map(MentorResponseDTO::fromEntity);
    }

    @Override
    public MentorResponseDTO getMentorById(Integer mentorId, User user) {
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with id: " + mentorId));

        if (user.getRole() == User.Role.MENTOR) {
            if (!mentor.getUser().getUserId().equals(user.getUserId())) {
                throw new ResourceNotFoundException("Access denied: You can only view your own profile");
            }
        } else if (user.getRole() != User.Role.ADMIN && user.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("Access denied");
        }

        return MentorResponseDTO.fromEntity(mentor);
    }

    @Override
    @Transactional
    public MentorResponseDTO createMentor(MentorRequestDTO request, User currentUser) {
        if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        if (user.getRole() != User.Role.MENTOR) {
            throw new ResourceNotFoundException("User must have MENTOR role");
        }

        if (mentorRepository.findByUser_UserId(user.getUserId()).isPresent()) {
            throw new DuplicateResourceException("User is already linked to a mentor");
        }

        Mentor mentor = new Mentor();
        mentor.setUser(user);
        mentor.setDepartment(request.getDepartment());
        mentor.setAcademicRank(request.getAcademicRank());

        return MentorResponseDTO.fromEntity(mentorRepository.save(mentor));
    }

    @Override
    @Transactional
    public MentorResponseDTO updateMentor(Integer mentorId, MentorUpdateDTO request, User user) {
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with id: " + mentorId));

        if (user.getRole() == User.Role.MENTOR) {
            if (!mentor.getUser().getUserId().equals(user.getUserId())) {
                throw new ResourceNotFoundException("Access denied");
            }
        } else if (user.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        if (request.getDepartment() != null) mentor.setDepartment(request.getDepartment());
        if (request.getAcademicRank() != null) mentor.setAcademicRank(request.getAcademicRank());

        return MentorResponseDTO.fromEntity(mentorRepository.save(mentor));
    }
}
