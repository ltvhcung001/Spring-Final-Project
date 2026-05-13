package org.example.internmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorRequestDTO {
    private Integer userId;
    private String department;
    private String academicRank;
}
