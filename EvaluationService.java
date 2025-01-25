package com.conference.api.services;

import com.conference.api.entities.Evaluation;
import com.conference.api.entities.EvaluationEtat;
import com.conference.api.entities.Soumission;
import com.conference.api.entities.Utilisateur;
import com.conference.api.exceptions.ResourceNotFoundException;
import com.conference.api.repositories.EvaluationRepository;
import com.conference.api.repositories.SoumissionRepository;
import com.conference.api.repositories.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final SoumissionRepository soumissionRepository;
    private final UtilisateurRepository utilisateurRepository;

    // Injection via constructeur
    public EvaluationService(EvaluationRepository evaluationRepository,
                             SoumissionRepository soumissionRepository,
                             UtilisateurRepository utilisateurRepository) {
        this.evaluationRepository = evaluationRepository;
        this.soumissionRepository = soumissionRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    // Récupérer toutes les évaluations
    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    // Récupérer une évaluation par ID
    public Evaluation getEvaluationById(int id) {
        return evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation introuvable avec l'ID " + id));
    }

    // Créer une évaluation avec validation
    public Evaluation createEvaluation(Evaluation evaluation) {
        // Valider la soumission
        Soumission soumission = soumissionRepository.findById(evaluation.getSoumission().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Soumission introuvable avec l'ID " + evaluation.getSoumission().getId()));

        // Valider l'évaluateur
        Utilisateur evaluateur = utilisateurRepository.findById(evaluation.getEvaluateur().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Évaluateur introuvable avec l'ID " + evaluation.getEvaluateur().getId()));

        // Vérifier si l'évaluateur est co-auteur
        if (soumission.estCoAuteur(evaluateur)) {
            throw new IllegalArgumentException("Un évaluateur ne peut pas évaluer une soumission dont il est co-auteur.");
        }

        // Valider l'évaluation
        validateEvaluation(evaluation);

        // Compléter les informations
        evaluation.setDateEvaluation(new Date());
        evaluation.setSoumission(soumission);
        evaluation.setEvaluateur(evaluateur);

        return evaluationRepository.save(evaluation);
    }

    // Mettre à jour une évaluation
    public Evaluation updateEvaluation(int id, Evaluation evaluationDetails) {
        Evaluation existingEvaluation = getEvaluationById(id);

        existingEvaluation.setNote(evaluationDetails.getNote());
        existingEvaluation.setCommentaires(evaluationDetails.getCommentaires());
        existingEvaluation.setEtat(evaluationDetails.getEtat());

        validateEvaluation(existingEvaluation);

        return evaluationRepository.save(existingEvaluation);
    }

    // Supprimer une évaluation par ID
    public void deleteEvaluation(int id) {
        Evaluation evaluation = getEvaluationById(id);
        evaluationRepository.delete(evaluation);
    }

    // Changer l'état d'une évaluation
    public Evaluation changeEvaluationState(int evaluationId, EvaluationEtat newState) {
        Evaluation evaluation = getEvaluationById(evaluationId);
        evaluation.setEtat(newState);
        return evaluationRepository.save(evaluation);
    }

    // Valider une évaluation
    private void validateEvaluation(Evaluation evaluation) {
        if (evaluation.getNote() < 1 || evaluation.getNote() > 10) {
            throw new IllegalArgumentException("La note doit être entre 1 et 10.");
        }
        if (evaluation.getCommentaires() == null || evaluation.getCommentaires().isBlank()) {
            throw new IllegalArgumentException("Les commentaires sont obligatoires.");
        }
        if (evaluation.getEtat() == null) {
            throw new IllegalArgumentException("L'état de l'évaluation est obligatoire.");
        }
    }
}
