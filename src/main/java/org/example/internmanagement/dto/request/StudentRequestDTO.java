package org.example.internmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequestDTO {
    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotBlank(message = "Student code is required")
    private String studentCode;

    @NotBlank(message = "Major is required")
    private String major;

    @NotBlank(message = "Class name is required")
    private String className;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Address is required")
    private String address;
}
