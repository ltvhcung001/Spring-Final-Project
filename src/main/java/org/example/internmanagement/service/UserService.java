package org.example.internmanagement.service;

import org.example.internmanagement.dto.request.UserRequestDTO;
import org.example.internmanagement.dto.request.UserUpdateDTO;
import org.example.internmanagement.dto.response.UserResponseDTO;
import org.example.internmanagement.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User getCurrentUser(UserDetails userDetails);
    Optional<User> findByUsername(String username);
    List<UserResponseDTO> getAllUsers(User.Role role);
    UserResponseDTO getUserById(Integer userId);
    UserResponseDTO createUser(UserRequestDTO request);
    UserResponseDTO updateUser(Integer userId, UserUpdateDTO request);
    UserResponseDTO updateStatus(Integer userId, Boolean isActive);
    UserResponseDTO updateRole(Integer userId, User.Role role, User currentUser);
    void deleteUser(Integer userId);
}
