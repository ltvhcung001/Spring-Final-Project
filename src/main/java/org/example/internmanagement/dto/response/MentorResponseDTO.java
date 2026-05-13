package org.example.internmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.internmanagement.entity.Mentor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorResponseDTO {
    private Integer mentorId;
    private UserResponseDTO user;
    private String department;
    private String academicRank;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MentorResponseDTO fromEntity(Mentor mentor) {
        return new MentorResponseDTO(
            mentor.getMentorId(),
            UserResponseDTO.fromEntity(mentor.getUser()),
            mentor.getDepartment(),
            mentor.getAcademicRank(),
            mentor.getCreatedAt(),
            mentor.getUpdatedAt()
        );
    }
}
