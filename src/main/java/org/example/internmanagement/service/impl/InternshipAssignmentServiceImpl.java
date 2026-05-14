package org.example.internmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.internmanagement.dto.request.InternshipAssignmentRequestDTO;
import org.example.internmanagement.dto.request.InternshipAssignmentStatusRequestDTO;
import org.example.internmanagement.dto.response.InternshipAssignmentResponseDTO;
import org.example.internmanagement.entity.*;
import org.example.internmanagement.exception.DuplicateResourceException;
import org.example.internmanagement.exception.ResourceNotFoundException;
import org.example.internmanagement.repository.InternshipAssignmentRepository;
import org.example.internmanagement.repository.InternshipPhaseRepository;
import org.example.internmanagement.repository.MentorRepository;
import org.example.internmanagement.repository.StudentRepository;
import org.example.internmanagement.service.InternshipAssignmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InternshipAssignmentServiceImpl implements InternshipAssignmentService {

    private final InternshipAssignmentRepository internshipAssignmentRepository;
    private final StudentRepository studentRepository;
    private final MentorRepository mentorRepository;
    private final InternshipPhaseRepository phaseRepository;

    @Override
    public Page<InternshipAssignmentResponseDTO> getAllAssignments(User currentUser, Pageable pageable) {
        Page<InternshipAssignment> assignments;
        if (currentUser.getRole() == User.Role.ADMIN) {
            assignments = internshipAssignmentRepository.findAll(pageable);
        } else if (currentUser.getRole() == User.Role.MENTOR) {
            assignments = internshipAssignmentRepository.findByMentor_User_UserId(currentUser.getUserId(), pageable);
        } else {
            assignments = internshipAssignmentRepository.findByStudent_User_UserId(currentUser.getUserId(), pageable);
        }
        return assignments.map(InternshipAssignmentResponseDTO::fromEntity);
    }

    @Override
    public InternshipAssignmentResponseDTO getAssignmentById(Integer assignmentId, User currentUser) {
        InternshipAssignment assignment = internshipAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship assignment not found with id " + assignmentId));

        if (currentUser.getRole() == User.Role.MENTOR && !assignment.getMentor().getUser().getUserId().equals(currentUser.getUserId())) {
            throw new ResourceNotFoundException("Internship assignment not found with id " + assignmentId);
        }
        if (currentUser.getRole() == User.Role.STUDENT && !assignment.getStudent().getUser().getUserId().equals(currentUser.getUserId())) {
            throw new ResourceNotFoundException("Internship assignment not found with id " + assignmentId);
        }

        return InternshipAssignmentResponseDTO.fromEntity(assignment);
    }

    @Override
    public InternshipAssignmentResponseDTO createAssignment(InternshipAssignmentRequestDTO requestDTO) {
        Student student = studentRepository.findById(requestDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + requestDTO.getStudentId()));
        Mentor mentor = mentorRepository.findById(requestDTO.getMentorId())
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with id " + requestDTO.getMentorId()));
        InternshipPhase phase = phaseRepository.findById(requestDTO.getPhaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Phase not found with id " + requestDTO.getPhaseId()));

        if (internshipAssignmentRepository.existsByStudent_StudentIdAndPhase_PhaseId(student.getStudentId(), phase.getPhaseId())) {
            throw new DuplicateResourceException("Student is already assigned to this phase");
        }

        InternshipAssignment assignment = new InternshipAssignment();
        assignment.setStudent(student);
        assignment.setMentor(mentor);
        assignment.setPhase(phase);
        if (requestDTO.getStatus() != null) {
            assignment.setStatus(requestDTO.getStatus());
        } else {
            assignment.setStatus(InternshipAssignment.Status.PENDING);
        }

        InternshipAssignment savedAssignment = internshipAssignmentRepository.save(assignment);
        return InternshipAssignmentResponseDTO.fromEntity(savedAssignment);
    }

    @Override
    public InternshipAssignmentResponseDTO updateAssignmentStatus(Integer assignmentId, InternshipAssignmentStatusRequestDTO requestDTO) {
        InternshipAssignment assignment = internshipAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship assignment not found with id " + assignmentId));

        assignment.setStatus(requestDTO.getStatus());
        InternshipAssignment updatedAssignment = internshipAssignmentRepository.save(assignment);
        return InternshipAssignmentResponseDTO.fromEntity(updatedAssignment);
    }
}
