package org.example.internmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import jakarta.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResultUpdateDTO {
    @Min(value = 0, message = "Score must be greater than or equal to 0")
    private BigDecimal score;
    private String comments;
}
