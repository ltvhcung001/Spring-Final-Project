package org.example.internmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.internmanagement.entity.EvaluationCriterion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationCriterionResponseDTO {
    private Integer criterionId;
    private String criterionName;
    private String description;
    private BigDecimal maxScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static EvaluationCriterionResponseDTO fromEntity(EvaluationCriterion criterion) {
        return new EvaluationCriterionResponseDTO(
                criterion.getCriterionId(),
                criterion.getCriterionName(),
                criterion.getDescription(),
                criterion.getMaxScore(),
                criterion.getCreatedAt(),
                criterion.getUpdatedAt()
        );
    }
}
