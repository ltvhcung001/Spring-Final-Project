package org.example.internmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String role;
    private Boolean isActive;
}
