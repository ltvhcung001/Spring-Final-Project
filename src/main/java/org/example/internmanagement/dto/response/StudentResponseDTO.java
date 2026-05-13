package org.example.internmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.internmanagement.entity.Student;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDTO {
    private Integer studentId;
    private UserResponseDTO user;
    private String studentCode;
    private String major;
    private String className;
    private LocalDate dateOfBirth;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static StudentResponseDTO fromEntity(Student student) {
        return new StudentResponseDTO(
            student.getStudentId(),
            UserResponseDTO.fromEntity(student.getUser()),
            student.getStudentCode(),
            student.getMajor(),
            student.getClassName(),
            student.getDateOfBirth(),
            student.getAddress(),
            student.getCreatedAt(),
            student.getUpdatedAt()
        );
    }
}
