package org.example.internmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorRequestDTO {
    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Academic rank is required")
    private String academicRank;
}
