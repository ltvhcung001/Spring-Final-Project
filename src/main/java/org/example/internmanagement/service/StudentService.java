package org.example.internmanagement.service;

import org.example.internmanagement.dto.request.StudentRequestDTO;
import org.example.internmanagement.dto.request.StudentUpdateDTO;
import org.example.internmanagement.dto.response.StudentResponseDTO;
import org.example.internmanagement.entity.User;

import java.util.List;

public interface StudentService {
    List<StudentResponseDTO> getAllStudents(User currentUser);
    StudentResponseDTO getStudentById(Integer studentId, User currentUser);
    StudentResponseDTO createStudent(StudentRequestDTO request, User currentUser);
    StudentResponseDTO updateStudent(Integer studentId, StudentUpdateDTO request, User currentUser);
}
