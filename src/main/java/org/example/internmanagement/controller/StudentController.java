package org.example.internmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.StudentRequestDTO;
import org.example.internmanagement.dto.request.StudentUpdateDTO;
import org.example.internmanagement.dto.response.StudentResponseDTO;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.service.StudentService;
import org.example.internmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import org.example.internmanagement.dto.response.PagedData;
import org.example.internmanagement.dto.response.Response;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Response<PagedData<StudentResponseDTO>>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<PagedData<StudentResponseDTO>>builder()
                .success(true)
                .message("Students fetched successfully")
                .data(PagedData.from(studentService.getAllStudents(currentUser, PageRequest.of(page, size))))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{student_id}")
    public ResponseEntity<Response<StudentResponseDTO>> getStudentById(
            @PathVariable("student_id") Integer studentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<StudentResponseDTO>builder()
                .success(true)
                .message("Student fetched successfully")
                .data(studentService.getStudentById(studentId, currentUser))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping
    public ResponseEntity<Response<StudentResponseDTO>> createStudent(
            @RequestBody StudentRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.<StudentResponseDTO>builder()
                        .success(true)
                        .message("Student created successfully")
                        .data(studentService.createStudent(request, currentUser))
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/{student_id}")
    public ResponseEntity<Response<StudentResponseDTO>> updateStudent(
            @PathVariable("student_id") Integer studentId,
            @RequestBody StudentUpdateDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(Response.<StudentResponseDTO>builder()
                .success(true)
                .message("Student updated successfully")
                .data(studentService.updateStudent(studentId, request, currentUser))
                .timestamp(LocalDateTime.now())
                .build());
    }
}
