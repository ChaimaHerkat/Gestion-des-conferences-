package com.conference.api.services;

import com.conference.api.entities.Conference;
import com.conference.api.entities.Role;
import com.conference.api.entities.UserRole;
import com.conference.api.exceptions.ResourceNotFoundException;
import com.conference.api.repositories.ConferenceRepository;
import com.conference.api.repositories.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final ConferenceRepository conferenceRepository;

    // Injection des dépendances via le constructeur
    public UserRoleService(UserRoleRepository userRoleRepository, ConferenceRepository conferenceRepository) {
        this.userRoleRepository = userRoleRepository;
        this.conferenceRepository = conferenceRepository;
    }

    // Trouver tous les rôles utilisateur
    public List<UserRole> findAll() {
        return userRoleRepository.findAll();
    }

    // Trouver un rôle utilisateur par ID
    public UserRole findById(Long id) {
        return userRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rôle avec l'ID " + id + " introuvable."));
    }

    // Créer un rôle utilisateur
    public UserRole save(UserRole userRole) {
        validateUserRole(userRole);

        // Si une conférence est liée au rôle, vérifier qu'elle existe
        if (userRole.getConference() != null) {
            Conference conference = conferenceRepository.findById(userRole.getConference().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Conférence introuvable avec l'ID fourni."));
            userRole.setConference(conference);
        }

        return userRoleRepository.save(userRole);
    }

    // Mettre à jour un rôle utilisateur
    public UserRole update(Long id, UserRole userRoleDetails) {
        UserRole existingRole = findById(id);

        // Mettre à jour les champs pertinents
        existingRole.setRole(userRoleDetails.getRole());
        existingRole.setUtilisateur(userRoleDetails.getUtilisateur());

        if (userRoleDetails.getConference() != null) {
            Conference conference = conferenceRepository.findById(userRoleDetails.getConference().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Conférence introuvable avec l'ID fourni."));
            existingRole.setConference(conference);
        } else {
            existingRole.setConference(null); // Suppression de l'association si nécessaire
        }

        validateUserRole(existingRole);
        return userRoleRepository.save(existingRole);
    }

    // Supprimer un rôle utilisateur par ID
    public void deleteById(Long id) {
        if (!userRoleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rôle avec l'ID " + id + " introuvable.");
        }
        userRoleRepository.deleteById(id);
    }

    // Valider les données du rôle utilisateur
    private void validateUserRole(UserRole userRole) {
        // Vérification du rôle et de l'utilisateur
        if (userRole.getRole() == null || userRole.getUtilisateur() == null) {
            throw new IllegalArgumentException("Le rôle et l'utilisateur sont obligatoires.");
        }

        // Si le rôle est ÉDITEUR ou ÉVALUATEUR, une conférence est obligatoire
        if (userRole.getRole() == Role.EVALUATEUR && userRole.getConference() == null) {
            throw new IllegalArgumentException("Une conférence est obligatoire pour le rôle d'évaluateur.");
        }

        // Les éditeurs ne doivent pas être liés directement à une conférence
        if (userRole.getRole() == Role.EDITEUR && userRole.getConference() != null) {
            throw new IllegalArgumentException("Un éditeur ne doit pas être lié à une conférence spécifique.");
        }
    }
}
