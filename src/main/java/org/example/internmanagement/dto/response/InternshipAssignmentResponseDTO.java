package org.example.internmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.internmanagement.entity.InternshipAssignment;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipAssignmentResponseDTO {
    private Integer assignmentId;
    private StudentResponseDTO student;
    private MentorResponseDTO mentor;
    private InternshipPhaseResponseDTO phase;
    private LocalDateTime assignedDate;
    private InternshipAssignment.Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static InternshipAssignmentResponseDTO fromEntity(InternshipAssignment assignment) {
        return new InternshipAssignmentResponseDTO(
                assignment.getAssignmentId(),
                StudentResponseDTO.fromEntity(assignment.getStudent()),
                MentorResponseDTO.fromEntity(assignment.getMentor()),
                InternshipPhaseResponseDTO.fromEntity(assignment.getPhase()),
                assignment.getAssignedDate(),
                assignment.getStatus(),
                assignment.getCreatedAt(),
                assignment.getUpdatedAt()
        );
    }
}
