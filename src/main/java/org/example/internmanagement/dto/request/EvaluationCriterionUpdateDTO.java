package org.example.internmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationCriterionUpdateDTO {
    private String criterionName;
    private String description;
    private BigDecimal maxScore;
}
