package org.example.internmanagement.repository;

import org.example.internmanagement.entity.AssessmentRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentRoundRepository extends JpaRepository<AssessmentRound, Integer> {
    Optional<AssessmentRound> findByRoundName(String roundName);
    boolean existsByRoundNameAndPhase_PhaseId(String roundName, Integer phaseId);
    Page<AssessmentRound> findByPhase_PhaseId(Integer phaseId, Pageable pageable);
    List<AssessmentRound> findByIsActiveTrue();
}

