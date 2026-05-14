package org.example.internmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.AssessmentResultRequestDTO;
import org.example.internmanagement.dto.request.AssessmentResultUpdateDTO;
import org.example.internmanagement.dto.response.AssessmentResultResponseDTO;
import org.example.internmanagement.entity.*;
import org.example.internmanagement.exception.DuplicateResourceException;
import org.example.internmanagement.exception.ResourceNotFoundException;
import org.example.internmanagement.repository.*;
import org.example.internmanagement.service.AssessmentResultService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentResultServiceImpl implements AssessmentResultService {

    private final AssessmentResultRepository assessmentResultRepository;
    private final InternshipAssignmentRepository internshipAssignmentRepository;
    private final AssessmentRoundRepository assessmentRoundRepository;
    private final EvaluationCriterionRepository evaluationCriterionRepository;

    @Override
    public Page<AssessmentResultResponseDTO> getAssessmentResults(Integer assignmentId, User currentUser, Pageable pageable) {
        Page<AssessmentResult> results;
        if (currentUser.getRole() == User.Role.ADMIN) {
            if (assignmentId != null) {
                results = assessmentResultRepository.findByAssignment_AssignmentId(assignmentId, pageable);
            } else {
                results = assessmentResultRepository.findAll(pageable);
            }
        } else if (currentUser.getRole() == User.Role.MENTOR) {
            if (assignmentId != null) {
                results = assessmentResultRepository.findByAssignment_AssignmentIdAndAssignment_Mentor_User_UserId(assignmentId, currentUser.getUserId(), pageable);
            } else {
                results = assessmentResultRepository.findByAssignment_Mentor_User_UserId(currentUser.getUserId(), pageable);
            }
        } else { // STUDENT
            if (assignmentId != null) {
                results = assessmentResultRepository.findByAssignment_AssignmentIdAndAssignment_Student_User_UserId(assignmentId, currentUser.getUserId(), pageable);
            } else {
                results = assessmentResultRepository.findByAssignment_Student_User_UserId(currentUser.getUserId(), pageable);
            }
        }
        return results.map(AssessmentResultResponseDTO::fromEntity);
    }

    @Override
    public AssessmentResultResponseDTO createAssessmentResult(AssessmentResultRequestDTO requestDTO, User currentUser) {
        InternshipAssignment assignment = internshipAssignmentRepository.findById(requestDTO.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Internship assignment not found with id " + requestDTO.getAssignmentId()));

        if (!assignment.getMentor().getUser().getUserId().equals(currentUser.getUserId())) {
            throw new ResourceNotFoundException("You are not the mentor for this assignment");
        }

        AssessmentRound round = assessmentRoundRepository.findById(requestDTO.getRoundId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessment round not found with id " + requestDTO.getRoundId()));

        EvaluationCriterion criterion = evaluationCriterionRepository.findById(requestDTO.getCriterionId())
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation criterion not found with id " + requestDTO.getCriterionId()));

        if (assessmentResultRepository.existsByAssignment_AssignmentIdAndRound_RoundIdAndCriterion_CriterionId(
                assignment.getAssignmentId(), round.getRoundId(), criterion.getCriterionId())) {
            throw new DuplicateResourceException("Assessment result already exists for this assignment, round and criterion");
        }

        AssessmentResult result = new AssessmentResult();
        result.setAssignment(assignment);
        result.setRound(round);
        result.setCriterion(criterion);
        result.setScore(requestDTO.getScore());
        result.setComments(requestDTO.getComments());
        result.setEvaluatedBy(currentUser);

        AssessmentResult savedResult = assessmentResultRepository.save(result);
        return AssessmentResultResponseDTO.fromEntity(savedResult);
    }

    @Override
    public AssessmentResultResponseDTO updateAssessmentResult(Integer resultId, AssessmentResultUpdateDTO requestDTO, User currentUser) {
        AssessmentResult result = assessmentResultRepository.findById(resultId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment result not found with id " + resultId));

        if (!result.getEvaluatedBy().getUserId().equals(currentUser.getUserId())) {
            throw new ResourceNotFoundException("You are not the creator of this assessment result");
        }

        if (requestDTO.getScore() != null) {
            result.setScore(requestDTO.getScore());
        }
        if (requestDTO.getComments() != null) {
            result.setComments(requestDTO.getComments());
        }

        AssessmentResult updatedResult = assessmentResultRepository.save(result);
        return AssessmentResultResponseDTO.fromEntity(updatedResult);
    }
}
