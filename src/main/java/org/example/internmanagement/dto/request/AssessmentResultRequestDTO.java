package org.example.internmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResultRequestDTO {
    @NotNull(message = "Assignment ID is required")
    private Integer assignmentId;

    @NotNull(message = "Round ID is required")
    private Integer roundId;

    @NotNull(message = "Criterion ID is required")
    private Integer criterionId;

    @NotNull(message = "Score is required")
    @Min(value = 0, message = "Score must be greater than or equal to 0")
    private BigDecimal score;

    private String comments;
}
