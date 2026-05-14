package org.example.internmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.internmanagement.entity.AssessmentResult;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResultResponseDTO {
    private Integer resultId;
    private Integer assignmentId;
    private AssessmentRoundResponseDTO round;
    private EvaluationCriterionResponseDTO criterion;
    private BigDecimal score;
    private String comments;
    private UserResponseDTO evaluatedBy;
    private LocalDateTime evaluationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AssessmentResultResponseDTO fromEntity(AssessmentResult result) {
        return new AssessmentResultResponseDTO(
            result.getResultId(),
            result.getAssignment().getAssignmentId(),
            AssessmentRoundResponseDTO.fromEntity(result.getRound(), new java.util.ArrayList<>()),
            EvaluationCriterionResponseDTO.fromEntity(result.getCriterion()),
            result.getScore(),
            result.getComments(),
            UserResponseDTO.fromEntity(result.getEvaluatedBy()),
            result.getEvaluationDate(),
            result.getCreatedAt(),
            result.getUpdatedAt()
        );
    }
}
