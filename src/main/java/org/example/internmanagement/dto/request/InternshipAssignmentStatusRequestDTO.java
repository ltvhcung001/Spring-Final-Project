package org.example.internmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.internmanagement.entity.InternshipAssignment;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipAssignmentStatusRequestDTO {
    @NotNull(message = "Status is required")
    private InternshipAssignment.Status status;
}
