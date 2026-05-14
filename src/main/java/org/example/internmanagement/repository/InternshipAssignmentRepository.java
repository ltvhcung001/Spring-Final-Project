package org.example.internmanagement.repository;

import org.example.internmanagement.entity.InternshipAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternshipAssignmentRepository extends JpaRepository<InternshipAssignment, Integer> {
    List<InternshipAssignment> findByStudent_StudentId(Integer studentId);
    List<InternshipAssignment> findByMentor_MentorId(Integer mentorId);
    List<InternshipAssignment> findByPhase_PhaseId(Integer phaseId);
    Optional<InternshipAssignment> findByStudent_StudentIdAndPhase_PhaseId(Integer studentId, Integer phaseId);
    List<InternshipAssignment> findByStudent_User_UserId(Integer userId);
    List<InternshipAssignment> findByMentor_User_UserId(Integer userId);
}

