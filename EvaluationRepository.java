package com.conference.api.repositories;

import com.conference.api.entities.EvaluationEtat;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import com.conference.api.entities.Evaluation;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
    List<Evaluation> findByEtat(@NotNull(message = "L'état de l'évaluation est obligatoire") EvaluationEtat etat);

    List<Evaluation> findByEvaluateurId(int evaluateurId);
}

