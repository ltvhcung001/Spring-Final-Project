package org.example.internmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentRoundRequestDTO {
    @NotNull(message = "Phase ID is required")
    private Integer phaseId;

    @NotBlank(message = "Round name is required")
    private String roundName;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private String description;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    @Valid
    @NotEmpty(message = "Criteria list cannot be empty")
    private List<RoundCriterionRequestDTO> criteria;
}
