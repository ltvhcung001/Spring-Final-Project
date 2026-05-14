package org.example.internmanagement.service;

import org.example.internmanagement.dto.request.MentorRequestDTO;
import org.example.internmanagement.dto.request.MentorUpdateDTO;
import org.example.internmanagement.dto.response.MentorResponseDTO;
import org.example.internmanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MentorService {
    Page<MentorResponseDTO> getAllMentors(User user, Pageable pageable);
    MentorResponseDTO getMentorById(Integer mentorId, User user);
    MentorResponseDTO createMentor(MentorRequestDTO request, User user);
    MentorResponseDTO updateMentor(Integer mentorId, MentorUpdateDTO request, User user);
}
