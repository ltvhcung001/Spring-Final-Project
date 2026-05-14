package org.example.internmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationCriterionRequestDTO {
    @NotBlank(message = "Criterion name is required")
    private String criterionName;

    private String description;

    @NotNull(message = "Max score is required")
    @Positive(message = "Max score must be greater than zero")
    private BigDecimal maxScore;
}
