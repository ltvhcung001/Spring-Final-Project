package org.example.internmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoundCriterionRequestDTO {
    private Integer roundId;
    private Integer criterionId;
    private BigDecimal weight;
}
