package org.example.internmanagement.controller;

import org.example.internmanagement.dto.request.StudentRequestDTO;
import org.example.internmanagement.dto.response.StudentResponseDTO;
import org.example.internmanagement.entity.InternshipAssignment;
import org.example.internmanagement.entity.Mentor;
import org.example.internmanagement.entity.Student;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.exception.ResourceNotFoundException;
import org.example.internmanagement.repository.InternshipAssignmentRepository;
import org.example.internmanagement.repository.MentorRepository;
import org.example.internmanagement.repository.StudentRepository;
import org.example.internmanagement.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;
    private final InternshipAssignmentRepository internshipAssignmentRepository;

    public StudentController(StudentRepository studentRepository, UserRepository userRepository,
                             MentorRepository mentorRepository, InternshipAssignmentRepository internshipAssignmentRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.mentorRepository = mentorRepository;
        this.internshipAssignmentRepository = internshipAssignmentRepository;
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
        User currentUser = getCurrentUser();
        List<Student> students;

        if (currentUser.getRole() == User.Role.ADMIN) {
            students = studentRepository.findAll();
        } else if (currentUser.getRole() == User.Role.MENTOR) {
            Mentor mentor = mentorRepository.findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mentor not found for current user"));
            List<InternshipAssignment> assignments = internshipAssignmentRepository.findByMentor_MentorId(mentor.getMentorId());
            students = assignments.stream().map(InternshipAssignment::getStudent).distinct().collect(Collectors.toList());
        } else {
            throw new ResourceNotFoundException("Access denied"); 
        }

        return ResponseEntity.ok(students.stream().map(StudentResponseDTO::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/{student_id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable("student_id") Integer studentId) {
        User currentUser = getCurrentUser();
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        if (currentUser.getRole() == User.Role.STUDENT) {
            if (!student.getUser().getUserId().equals(currentUser.getUserId())) {
                throw new ResourceNotFoundException("Access denied");
            }
        } else if (currentUser.getRole() == User.Role.MENTOR) {
            Mentor mentor = mentorRepository.findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mentor not found for current user"));
            List<InternshipAssignment> assignments = internshipAssignmentRepository.findByMentor_MentorId(mentor.getMentorId());
            boolean isAssigned = assignments.stream().anyMatch(a -> a.getStudent().getStudentId().equals(studentId));
            if (!isAssigned) {
                throw new ResourceNotFoundException("Access denied: You are not assigned to this student");
            }
        }

        return ResponseEntity.ok(StudentResponseDTO.fromEntity(student));
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody StudentRequestDTO request) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        if (studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new ResourceNotFoundException("Student code already exists");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        if (user.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("User must have STUDENT role");
        }
        
        if (studentRepository.findByUser_UserId(user.getUserId()).isPresent()) {
            throw new ResourceNotFoundException("User is already linked to a student");
        }

        Student student = new Student();
        student.setUser(user);
        student.setStudentCode(request.getStudentCode());
        student.setMajor(request.getMajor());
        student.setClassName(request.getClassName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setAddress(request.getAddress());

        return ResponseEntity.ok(StudentResponseDTO.fromEntity(studentRepository.save(student)));
    }

    @PutMapping("/{student_id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable("student_id") Integer studentId,
                                                            @RequestBody StudentRequestDTO request) {
        User currentUser = getCurrentUser();
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        if (currentUser.getRole() == User.Role.STUDENT) {
            if (!student.getUser().getUserId().equals(currentUser.getUserId())) {
                throw new ResourceNotFoundException("Access denied");
            }
        } else if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        if (!student.getStudentCode().equals(request.getStudentCode()) &&
                studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new ResourceNotFoundException("Student code already exists");
        }

        student.setStudentCode(request.getStudentCode());
        student.setMajor(request.getMajor());
        student.setClassName(request.getClassName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setAddress(request.getAddress());

        return ResponseEntity.ok(StudentResponseDTO.fromEntity(studentRepository.save(student)));
    }
}
