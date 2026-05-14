package org.example.internmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.internmanagement.entity.InternshipAssignment;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipAssignmentRequestDTO {
    @NotNull(message = "Student ID is required")
    private Integer studentId;

    @NotNull(message = "Mentor ID is required")
    private Integer mentorId;

    @NotNull(message = "Phase ID is required")
    private Integer phaseId;

    @NotNull(message = "Status is required")
    private InternshipAssignment.Status status;
}
