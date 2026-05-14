package org.example.internmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResultRequestDTO {
    private Integer assignmentId;
    private Integer roundId;
    private Integer criterionId;
    private BigDecimal score;
    private String comments;
}
