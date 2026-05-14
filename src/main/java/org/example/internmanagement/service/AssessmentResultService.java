package org.example.internmanagement.service;

import org.example.internmanagement.dto.request.AssessmentResultRequestDTO;
import org.example.internmanagement.dto.request.AssessmentResultUpdateDTO;
import org.example.internmanagement.dto.response.AssessmentResultResponseDTO;
import org.example.internmanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssessmentResultService {
    Page<AssessmentResultResponseDTO> getAssessmentResults(Integer assignmentId, User currentUser, Pageable pageable);
    AssessmentResultResponseDTO createAssessmentResult(AssessmentResultRequestDTO requestDTO, User currentUser);
    AssessmentResultResponseDTO updateAssessmentResult(Integer resultId, AssessmentResultUpdateDTO requestDTO, User currentUser);
}
