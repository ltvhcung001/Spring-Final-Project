package org.example.internmanagement.repository;

import org.example.internmanagement.entity.InternshipPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InternshipPhaseRepository extends JpaRepository<InternshipPhase, Integer> {
    Optional<InternshipPhase> findByPhaseName(String phaseName);
}

