package org.example.internmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.internmanagement.entity.AssessmentRound;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentRoundResponseDTO {
    private Integer roundId;
    private InternshipPhaseResponseDTO phase;
    private String roundName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RoundCriterionResponseDTO> criteria;

    public static AssessmentRoundResponseDTO fromEntity(AssessmentRound round, List<RoundCriterionResponseDTO> criteria) {
        return new AssessmentRoundResponseDTO(
                round.getRoundId(),
                InternshipPhaseResponseDTO.fromEntity(round.getPhase()),
                round.getRoundName(),
                round.getStartDate(),
                round.getEndDate(),
                round.getDescription(),
                round.getIsActive(),
                round.getCreatedAt(),
                round.getUpdatedAt(),
                criteria
        );
    }
}
