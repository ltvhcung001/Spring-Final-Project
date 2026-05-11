package org.example.internmanagement.repository;

import org.example.internmanagement.entity.EvaluationCriterion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EvaluationCriterionRepository extends JpaRepository<EvaluationCriterion, Integer> {
    Optional<EvaluationCriterion> findByCriterionName(String criterionName);
    boolean existsByCriterionName(String criterionName);
}

