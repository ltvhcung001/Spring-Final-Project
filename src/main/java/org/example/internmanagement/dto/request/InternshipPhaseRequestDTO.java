package org.example.internmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipPhaseRequestDTO {
    private String phaseName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}
