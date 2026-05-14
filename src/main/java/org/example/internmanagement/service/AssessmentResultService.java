package org.example.internmanagement.service;

import org.example.internmanagement.dto.request.AssessmentResultRequestDTO;
import org.example.internmanagement.dto.request.AssessmentResultUpdateDTO;
import org.example.internmanagement.dto.response.AssessmentResultResponseDTO;
import org.example.internmanagement.entity.User;

import java.util.List;

public interface AssessmentResultService {
    List<AssessmentResultResponseDTO> getAssessmentResults(Integer assignmentId, User currentUser);
    AssessmentResultResponseDTO createAssessmentResult(AssessmentResultRequestDTO requestDTO, User currentUser);
    AssessmentResultResponseDTO updateAssessmentResult(Integer resultId, AssessmentResultUpdateDTO requestDTO, User currentUser);
}
