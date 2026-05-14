package org.example.internmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.internmanagement.entity.InternshipAssignment;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipAssignmentStatusRequestDTO {
    private InternshipAssignment.Status status;
}
