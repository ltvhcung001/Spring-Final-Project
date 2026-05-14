package org.example.internmanagement.repository;

import org.example.internmanagement.entity.AssessmentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Integer> {
    Page<AssessmentResult> findByAssignment_AssignmentId(Integer assignmentId, Pageable pageable);
    List<AssessmentResult> findByRound_RoundId(Integer roundId);
    List<AssessmentResult> findByCriterion_CriterionId(Integer criterionId);
    Optional<AssessmentResult> findByAssignment_AssignmentIdAndRound_RoundIdAndCriterion_CriterionId(Integer assignmentId, Integer roundId, Integer criterionId);
    boolean existsByAssignment_AssignmentIdAndRound_RoundIdAndCriterion_CriterionId(Integer assignmentId, Integer roundId, Integer criterionId);
    List<AssessmentResult> findByEvaluatedBy_UserId(Integer evaluatedBy);
    Page<AssessmentResult> findByAssignment_Mentor_User_UserId(Integer userId, Pageable pageable);
    Page<AssessmentResult> findByAssignment_Student_User_UserId(Integer userId, Pageable pageable);
    Page<AssessmentResult> findByAssignment_AssignmentIdAndAssignment_Mentor_User_UserId(Integer assignmentId, Integer userId, Pageable pageable);
    Page<AssessmentResult> findByAssignment_AssignmentIdAndAssignment_Student_User_UserId(Integer assignmentId, Integer userId, Pageable pageable);
}

