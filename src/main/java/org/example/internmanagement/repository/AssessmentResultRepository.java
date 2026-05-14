package org.example.internmanagement.repository;

import org.example.internmanagement.entity.AssessmentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Integer> {
    List<AssessmentResult> findByAssignment_AssignmentId(Integer assignmentId);
    List<AssessmentResult> findByRound_RoundId(Integer roundId);
    List<AssessmentResult> findByCriterion_CriterionId(Integer criterionId);
    Optional<AssessmentResult> findByAssignment_AssignmentIdAndRound_RoundIdAndCriterion_CriterionId(Integer assignmentId, Integer roundId, Integer criterionId);
    List<AssessmentResult> findByEvaluatedBy_UserId(Integer evaluatedBy);
    List<AssessmentResult> findByAssignment_Mentor_User_UserId(Integer userId);
    List<AssessmentResult> findByAssignment_Student_User_UserId(Integer userId);
    List<AssessmentResult> findByAssignment_AssignmentIdAndAssignment_Mentor_User_UserId(Integer assignmentId, Integer userId);
    List<AssessmentResult> findByAssignment_AssignmentIdAndAssignment_Student_User_UserId(Integer assignmentId, Integer userId);
}

