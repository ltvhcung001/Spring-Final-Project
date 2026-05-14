package org.example.internmanagement.service;

import org.example.internmanagement.dto.request.InternshipAssignmentRequestDTO;
import org.example.internmanagement.dto.request.InternshipAssignmentStatusRequestDTO;
import org.example.internmanagement.dto.response.InternshipAssignmentResponseDTO;
import org.example.internmanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InternshipAssignmentService {
    Page<InternshipAssignmentResponseDTO> getAllAssignments(User currentUser, Pageable pageable);
    InternshipAssignmentResponseDTO getAssignmentById(Integer assignmentId, User currentUser);
    InternshipAssignmentResponseDTO createAssignment(InternshipAssignmentRequestDTO requestDTO);
    InternshipAssignmentResponseDTO updateAssignmentStatus(Integer assignmentId, InternshipAssignmentStatusRequestDTO requestDTO);
}
