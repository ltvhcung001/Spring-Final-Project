package org.example.internmanagement.repository;

import org.example.internmanagement.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByStudentCode(String studentCode);
    Optional<Student> findByUser_UserId(Integer userId);
    boolean existsByStudentCode(String studentCode);

    @Query("SELECT s FROM Student s JOIN InternshipAssignment ia ON s.studentId = ia.student.studentId WHERE ia.mentor.mentorId = :mentorId")
    Page<Student> findStudentsByMentorId(Integer mentorId, Pageable pageable);
}

