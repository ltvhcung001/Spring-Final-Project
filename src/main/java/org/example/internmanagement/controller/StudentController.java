package org.example.internmanagement.controller;

import org.example.internmanagement.dto.request.StudentRequestDTO;
import org.example.internmanagement.dto.response.StudentResponseDTO;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.exception.ResourceNotFoundException;
import org.example.internmanagement.repository.UserRepository;
import org.example.internmanagement.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final UserRepository userRepository;

    public StudentController(StudentService studentService, UserRepository userRepository) {
        this.studentService = studentService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new ResourceNotFoundException("User not authenticated");
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents(getCurrentUser()));
    }

    @GetMapping("/{student_id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable("student_id") Integer studentId) {
        return ResponseEntity.ok(studentService.getStudentById(studentId, getCurrentUser()));
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody StudentRequestDTO request) {
        return ResponseEntity.ok(studentService.createStudent(request, getCurrentUser()));
    }

    @PutMapping("/{student_id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable("student_id") Integer studentId,
                                                            @RequestBody StudentRequestDTO request) {
        return ResponseEntity.ok(studentService.updateStudent(studentId, request, getCurrentUser()));
    }
}
