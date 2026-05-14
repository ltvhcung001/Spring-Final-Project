package org.example.internmanagement.service;

import org.example.internmanagement.dto.request.MentorRequestDTO;
import org.example.internmanagement.dto.request.MentorUpdateDTO;
import org.example.internmanagement.dto.response.MentorResponseDTO;
import org.example.internmanagement.entity.User;

import java.util.List;

public interface MentorService {
    List<MentorResponseDTO> getAllMentors(User user);
    MentorResponseDTO getMentorById(Integer mentorId, User user);
    MentorResponseDTO createMentor(MentorRequestDTO request, User user);
    MentorResponseDTO updateMentor(Integer mentorId, MentorUpdateDTO request, User user);
}
