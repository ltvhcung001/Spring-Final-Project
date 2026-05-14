package org.example.internmanagement.service;

import org.example.internmanagement.dto.request.StudentRequestDTO;
import org.example.internmanagement.dto.request.StudentUpdateDTO;
import org.example.internmanagement.dto.response.StudentResponseDTO;
import org.example.internmanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {
    Page<StudentResponseDTO> getAllStudents(User currentUser, Pageable pageable);
    StudentResponseDTO getStudentById(Integer studentId, User currentUser);
    StudentResponseDTO createStudent(StudentRequestDTO request, User currentUser);
    StudentResponseDTO updateStudent(Integer studentId, StudentUpdateDTO request, User currentUser);
}
