package org.example.internmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.internmanagement.entity.RoundCriterion;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoundCriterionResponseDTO {
    private Integer roundCriterionId;
    private EvaluationCriterionResponseDTO criterion;
    private BigDecimal weight;

    public static RoundCriterionResponseDTO fromEntity(RoundCriterion rc) {
        return new RoundCriterionResponseDTO(
                rc.getRoundCriterionId(),
                EvaluationCriterionResponseDTO.fromEntity(rc.getCriterion()),
                rc.getWeight()
        );
    }
}
