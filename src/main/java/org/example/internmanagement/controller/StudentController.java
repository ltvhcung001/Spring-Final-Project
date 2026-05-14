package org.example.internmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.StudentRequestDTO;
import org.example.internmanagement.dto.response.StudentResponseDTO;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.service.StudentService;
import org.example.internmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents(
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(studentService.getAllStudents(currentUser));
    }

    @GetMapping("/{student_id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(
            @PathVariable("student_id") Integer studentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(studentService.getStudentById(studentId, currentUser));
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(
            @RequestBody StudentRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.createStudent(request, currentUser));
    }

    @PutMapping("/{student_id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable("student_id") Integer studentId,
            @RequestBody StudentRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(studentService.updateStudent(studentId, request, currentUser));
    }
}
