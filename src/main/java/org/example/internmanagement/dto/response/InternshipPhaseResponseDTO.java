package org.example.internmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.internmanagement.entity.InternshipPhase;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipPhaseResponseDTO {
    private Integer phaseId;
    private String phaseName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static InternshipPhaseResponseDTO fromEntity(InternshipPhase phase) {
        return new InternshipPhaseResponseDTO(
                phase.getPhaseId(),
                phase.getPhaseName(),
                phase.getStartDate(),
                phase.getEndDate(),
                phase.getDescription(),
                phase.getCreatedAt(),
                phase.getUpdatedAt()
        );
    }
}
