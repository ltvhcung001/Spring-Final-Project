package org.example.internmanagement.service.impl;

import lombok.RequiredArgsConstructor;
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
import org.example.internmanagement.service.StudentService;
import org.springframework.stereotype.Service;

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
    public List<StudentResponseDTO> getAllStudents(User currentUser) {
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

        return students.stream().map(StudentResponseDTO::fromEntity).collect(Collectors.toList());
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

        return StudentResponseDTO.fromEntity(studentRepository.save(student));
    }

    @Override
    public StudentResponseDTO updateStudent(Integer studentId, StudentRequestDTO request, User currentUser) {
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

        return StudentResponseDTO.fromEntity(studentRepository.save(student));
    }
}
