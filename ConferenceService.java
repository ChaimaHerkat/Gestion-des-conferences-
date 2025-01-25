package com.conference.api.services;

import com.conference.api.dto.ConferenceDTO;
import com.conference.api.entities.Conference;
import com.conference.api.entities.ConferenceEtat;
import com.conference.api.entities.Role;
import com.conference.api.entities.Soumission;
import com.conference.api.entities.UserRole;
import com.conference.api.entities.Utilisateur;
import com.conference.api.exceptions.ResourceNotFoundException;
import com.conference.api.repositories.ConferenceRepository;
import com.conference.api.repositories.UserRoleRepository;
import com.conference.api.repositories.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConferenceService {

    private final ConferenceRepository conferenceRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final UserRoleRepository userRoleRepository;

    public ConferenceService(ConferenceRepository conferenceRepository, UtilisateurRepository utilisateurRepository, UserRoleRepository userRoleRepository) {
        this.conferenceRepository = conferenceRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.userRoleRepository = userRoleRepository;
    }

    // Récupérer toutes les conférences
    public List<Conference> getAllConferences() {
        return conferenceRepository.findAll();
    }

    // Récupérer une conférence par ID
    public Conference getConferenceById(int id) {
        return conferenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conférence introuvable avec l'ID : " + id));
    }

    // Créer une nouvelle conférence
    public Conference saveConference(Conference conference) {
        // Valider si le créateur est un éditeur
        UserRole userRole = userRoleRepository.findByUtilisateurIdAndRole(conference.getCreateur().getId(), Role.EDITEUR)
                .orElseThrow(() -> new IllegalArgumentException("Le créateur doit avoir un rôle d'éditeur."));

        // Sauvegarder la conférence
        Conference savedConference = conferenceRepository.save(conference);

        // Mettre à jour le rôle de l'utilisateur avec l'ID de la conférence
        userRole.setConference(savedConference);
        userRoleRepository.save(userRole);

        return savedConference;
    }

    // Créer une conférence à partir d'un DTO
    public Conference createConference(ConferenceDTO conferenceDTO) {
        // Vérification que l'utilisateur existe
        Utilisateur createur = utilisateurRepository.findById(conferenceDTO.getCreateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Créateur introuvable avec l'ID : " + conferenceDTO.getCreateurId()));

        // Vérification que l'utilisateur a le rôle d'Éditeur
        boolean estEditeur = createur.getUserRoles().stream()
                .anyMatch(userRole -> userRole.getRole() == Role.EDITEUR);
        if (!estEditeur) {
            throw new IllegalArgumentException("Seuls les utilisateurs ayant le rôle d'Éditeur peuvent créer une conférence.");
        }

        // Création de la conférence
        Conference conference = new Conference();
        conference.setTitre(conferenceDTO.getTitre());
        conference.setThematique(conferenceDTO.getThematique());
        conference.setDateDebut(conferenceDTO.getDateDebut());
        conference.setDateFin(conferenceDTO.getDateFin());
        conference.setEtat(conferenceDTO.getEtat());
        conference.setCreateur(createur);

        validateDates(conference.getDateDebut(), conference.getDateFin());
        return saveConference(conference);
    }

    // Mettre à jour une conférence
    public Conference updateConference(int id, Conference conferenceDetails) {
        Conference conference = getConferenceById(id);

        validateDates(conferenceDetails.getDateDebut(), conferenceDetails.getDateFin());

        conference.setTitre(conferenceDetails.getTitre());
        conference.setThematique(conferenceDetails.getThematique());
        conference.setDateDebut(conferenceDetails.getDateDebut());
        conference.setDateFin(conferenceDetails.getDateFin());
        conference.setEtat(conferenceDetails.getEtat());

        return conferenceRepository.save(conference);
    }

    // Supprimer une conférence
    public void deleteConference(int id) {
        Conference conference = getConferenceById(id);
        conferenceRepository.delete(conference);
    }

    // Changer l'état d'une conférence
    public Conference changeConferenceState(int conferenceId, ConferenceEtat newState) {
        Conference conference = getConferenceById(conferenceId);
        conference.setEtat(newState);
        return conferenceRepository.save(conference);
    }

    // Récupérer les soumissions associées à une conférence
    public List<Soumission> getSoumissionsByConferenceId(int conferenceId) {
        Conference conference = getConferenceById(conferenceId);
        return conference.getSoumissions();
    }

    // Validation des dates
    private void validateDates(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début doit être antérieure ou égale à la date de fin.");
        }
    }
}
