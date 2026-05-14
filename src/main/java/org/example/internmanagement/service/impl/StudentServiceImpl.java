package org.example.internmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.StudentRequestDTO;
import org.example.internmanagement.dto.request.StudentUpdateDTO;
import org.example.internmanagement.dto.response.StudentResponseDTO;
import org.example.internmanagement.entity.InternshipAssignment;
import org.example.internmanagement.entity.Mentor;
import org.example.internmanagement.entity.Student;
import org.example.internmanagement.entity.User;
import org.example.internmanagement.exception.DuplicateResourceException;
import org.example.internmanagement.exception.ResourceNotFoundException;
import org.example.internmanagement.repository.InternshipAssignmentRepository;
import org.example.internmanagement.repository.MentorRepository;
import org.example.internmanagement.repository.StudentRepository;
import org.example.internmanagement.repository.UserRepository;
import org.example.internmanagement.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;
    private final InternshipAssignmentRepository internshipAssignmentRepository;

    @Override
    public Page<StudentResponseDTO> getAllStudents(User currentUser, Pageable pageable) {
        Page<Student> studentPage;

        if (currentUser.getRole() == User.Role.ADMIN) {
            studentPage = studentRepository.findAll(pageable);
        } else if (currentUser.getRole() == User.Role.MENTOR) {
            Mentor mentor = mentorRepository.findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mentor not found for current user"));
            
            // For Mentor, we might need a custom query in the repository for pagination
            studentPage = studentRepository.findStudentsByMentorId(mentor.getMentorId(), pageable);
        } else {
            throw new ResourceNotFoundException("Access denied");
        }

        return studentPage.map(StudentResponseDTO::fromEntity);
    }

    @Override
    public StudentResponseDTO getStudentById(Integer studentId, User currentUser) {
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

        return StudentResponseDTO.fromEntity(student);
    }

    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO request, User currentUser) {
        if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        if (studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new DuplicateResourceException("Student code already exists");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        if (user.getRole() != User.Role.STUDENT) {
            throw new ResourceNotFoundException("User must have STUDENT role");
        }
        
        if (studentRepository.findByUser_UserId(user.getUserId()).isPresent()) {
            throw new DuplicateResourceException("User is already linked to a student");
        }

        Student student = new Student();
        student.setUser(user);
        student.setStudentCode(request.getStudentCode());
        student.setMajor(request.getMajor());
        student.setClassName(request.getClassName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setAddress(request.getAddress());

        return StudentResponseDTO.fromEntity(studentRepository.save(student));
    }

    @Override
    public StudentResponseDTO updateStudent(Integer studentId, StudentUpdateDTO request, User currentUser) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        if (currentUser.getRole() == User.Role.STUDENT) {
            if (!student.getUser().getUserId().equals(currentUser.getUserId())) {
                throw new ResourceNotFoundException("Access denied");
            }
        } else if (currentUser.getRole() != User.Role.ADMIN) {
            throw new ResourceNotFoundException("Access denied");
        }

        if (request.getStudentCode() != null && !student.getStudentCode().equals(request.getStudentCode()) &&
                studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new DuplicateResourceException("Student code already exists");
        }

        if (request.getStudentCode() != null) student.setStudentCode(request.getStudentCode());
        if (request.getMajor() != null) student.setMajor(request.getMajor());
        if (request.getClassName() != null) student.setClassName(request.getClassName());
        if (request.getDateOfBirth() != null) student.setDateOfBirth(request.getDateOfBirth());
        if (request.getAddress() != null) student.setAddress(request.getAddress());

        return StudentResponseDTO.fromEntity(studentRepository.save(student));
    }
}
