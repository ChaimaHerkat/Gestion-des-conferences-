package com.conference.api.services;

import com.conference.api.dto.SoumissionDTO;
import com.conference.api.entities.Conference;
import com.conference.api.entities.Soumission;
import com.conference.api.entities.Utilisateur;
import com.conference.api.entities.Role;
import com.conference.api.exceptions.ResourceNotFoundException;
import com.conference.api.repositories.ConferenceRepository;
import com.conference.api.repositories.SoumissionRepository;
import com.conference.api.repositories.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SoumissionService {

    private final SoumissionRepository soumissionRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ConferenceRepository conferenceRepository;

    public SoumissionService(SoumissionRepository soumissionRepository,
                             UtilisateurRepository utilisateurRepository,
                             ConferenceRepository conferenceRepository) {
        this.soumissionRepository = soumissionRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.conferenceRepository = conferenceRepository;
    }

    // Create Soumission from DTO
    public Soumission createFromDTO(SoumissionDTO soumissionDTO) {
        // Retrieve Auteur
        Utilisateur auteur = utilisateurRepository.findById(soumissionDTO.getAuteurId())
                .orElseThrow(() -> new ResourceNotFoundException("Auteur introuvable avec l'ID : " + soumissionDTO.getAuteurId()));

        // Retrieve Conference
        Conference conference = conferenceRepository.findById(soumissionDTO.getConferenceId())
                .orElseThrow(() -> new ResourceNotFoundException("Conférence introuvable avec l'ID : " + soumissionDTO.getConferenceId()));

        // Validate author's role
        if (!auteur.aLeRole(Role.AUTEUR, conference)) {
            throw new IllegalArgumentException("L'utilisateur n'est pas un auteur pour cette conférence.");
        }

        // Create new Soumission
        Soumission soumission = new Soumission();
        soumission.setTitreArticle(soumissionDTO.getTitreArticle());
        soumission.setResume(soumissionDTO.getResume());
        soumission.setEtat(soumissionDTO.getEtat());
        soumission.setDocumentPdf(soumissionDTO.getDocumentPdf()); // Ajout du document PDF

        // Convert LocalDateTime to java.util.Date if necessary
        if (soumissionDTO.getDateSoumission() != null) {
            Date dateSoumission = Date.from(soumissionDTO.getDateSoumission().atZone(ZoneId.systemDefault()).toInstant());
            soumission.setDateSoumission(dateSoumission);
        } else {
            soumission.setDateSoumission(new Date()); // Set current date if null
        }

        soumission.setAuteur(auteur);
        soumission.setConference(conference);

        // Assign Co-Auteurs
        List<Utilisateur> coAuteurs = new ArrayList<>();
        if (soumissionDTO.getCoAuteurs() != null) {
            for (Integer coAuteurId : soumissionDTO.getCoAuteurs()) {
                Utilisateur coAuteur = utilisateurRepository.findById(coAuteurId)
                        .orElseThrow(() -> new ResourceNotFoundException("Co-auteur introuvable avec l'ID : " + coAuteurId));
                coAuteurs.add(coAuteur);
            }
        }
        soumission.setCoAuteurs(coAuteurs);

        // Assign Evaluators
        List<Utilisateur> evaluateurs = new ArrayList<>();
        if (soumissionDTO.getEvaluateurs() != null) {
            for (Integer evaluateurId : soumissionDTO.getEvaluateurs()) {
                Utilisateur evaluateur = utilisateurRepository.findById(evaluateurId)
                        .orElseThrow(() -> new ResourceNotFoundException("Évaluateur introuvable avec l'ID : " + evaluateurId));
                evaluateurs.add(evaluateur);
            }
        }
        soumission.setEvaluateurs(evaluateurs);

        // Save and return
        return soumissionRepository.save(soumission);
    }

    // Find all submissions
    public List<Soumission> findAll() {
        return soumissionRepository.findAll();
    }

    // Find submission by ID
    public Soumission findById(int id) {
        return soumissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Soumission introuvable avec l'ID : " + id));
    }

    // Update an existing submission
    public Soumission update(int id, Soumission soumissionDetails) {
        Soumission existingSoumission = findById(id);

        existingSoumission.setTitreArticle(soumissionDetails.getTitreArticle());
        existingSoumission.setResume(soumissionDetails.getResume());
        existingSoumission.setEtat(soumissionDetails.getEtat());

        if (soumissionDetails.getDateSoumission() != null) {
            existingSoumission.setDateSoumission(soumissionDetails.getDateSoumission());
        }

        existingSoumission.setConference(soumissionDetails.getConference());
        existingSoumission.setAuteur(soumissionDetails.getAuteur());
        existingSoumission.setCoAuteurs(soumissionDetails.getCoAuteurs());
        existingSoumission.setEvaluateurs(soumissionDetails.getEvaluateurs());
        existingSoumission.setDocumentPdf(soumissionDetails.getDocumentPdf());

        return soumissionRepository.save(existingSoumission);
    }

    // Delete a submission
    public void delete(int id) {
        Soumission soumission = findById(id);
        soumissionRepository.delete(soumission);
    }

    // Assign an evaluator to a submission
    public Soumission assignEvaluateur(int soumissionId, int evaluateurId) {
        Soumission soumission = findById(soumissionId);
        Utilisateur evaluateur = utilisateurRepository.findById(evaluateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'ID : " + evaluateurId));

        // Ensure the user has the "EVALUATEUR" role for the associated conference
        if (!evaluateur.aLeRole(Role.EVALUATEUR, soumission.getConference())) {
            throw new IllegalArgumentException("L'utilisateur n'est pas un évaluateur pour cette conférence.");
        }

        // Add the evaluator to the submission if not already assigned
        if (!soumission.getEvaluateurs().contains(evaluateur)) {
            soumission.getEvaluateurs().add(evaluateur);
            return soumissionRepository.save(soumission);
        }

        throw new IllegalArgumentException("Évaluateur déjà assigné.");
    }

    // Get submissions by conference
    public List<Soumission> getSoumissionsByConference(int conferenceId) {
        return soumissionRepository.findByConferenceId(conferenceId);
    }

    // Get submissions by author
    public List<Soumission> getSoumissionsByAuteur(int auteurId) {
        return soumissionRepository.findByAuteurId(auteurId);
    }

    // Get submissions by status
    public List<Soumission> getSoumissionsByEtat(String etat) {
        return soumissionRepository.findByEtat(etat);
    }
}
