package com.conference.api.services;

import com.conference.api.dto.UtilisateurDTO;
import com.conference.api.entities.Role;
import com.conference.api.entities.UserRole;
import com.conference.api.entities.Utilisateur;
import com.conference.api.exceptions.ResourceNotFoundException;
import com.conference.api.repositories.UtilisateurRepository;
import com.conference.api.repositories.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final UserRoleRepository userRoleRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, UserRoleRepository userRoleRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.userRoleRepository = userRoleRepository;
    }

    // Retrieve all users
    public List<UtilisateurDTO> getAllUtilisateurs() {
        return utilisateurRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Retrieve a user by ID
    public Utilisateur getUtilisateurById(int id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + id));
    }

    // Create a user with role handling
    public UtilisateurDTO createUtilisateur(UtilisateurDTO utilisateurDTO) {
        // Validate email
        validateEmail(utilisateurDTO.getEmail());

        // Create the Utilisateur entity
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(utilisateurDTO.getNom());
        utilisateur.setPrenom(utilisateurDTO.getPrenom());
        utilisateur.setEmail(utilisateurDTO.getEmail());

        // Save the user
        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);

        // Handle roles
        if (utilisateurDTO.getRoles() != null && !utilisateurDTO.getRoles().isEmpty()) {
            for (String roleStr : utilisateurDTO.getRoles()) {
                Role role;
                try {
                    role = Role.valueOf(roleStr); // Convert string to enum
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Rôle invalide : " + roleStr);
                }

                // Create and save the UserRole
                UserRole userRole = new UserRole();
                userRole.setRole(role);
                userRole.setUtilisateur(savedUtilisateur);

                // Save the role in the database
                userRoleRepository.save(userRole);
            }
        }

        // Return the DTO
        return convertToDTO(savedUtilisateur);
    }

    // Update a user
    public UtilisateurDTO updateUtilisateur(int id, Utilisateur utilisateurDetails) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur not found with id " + id));
        validateEmail(utilisateurDetails.getEmail());
        utilisateur.setNom(utilisateurDetails.getNom());
        utilisateur.setPrenom(utilisateurDetails.getPrenom());
        utilisateur.setEmail(utilisateurDetails.getEmail());
        Utilisateur updatedUtilisateur = utilisateurRepository.save(utilisateur);
        return convertToDTO(updatedUtilisateur);
    }

    // Delete a user
    public void deleteUtilisateur(int id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur not found with id " + id));
        utilisateurRepository.delete(utilisateur);
    }

    // Retrieve a user by email
    public UtilisateurDTO getUtilisateurByEmail(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur not found with email " + email));
        return convertToDTO(utilisateur);
    }

    // Retrieve users by role
    public List<UtilisateurDTO> getUtilisateursByRole(Role role) {
        List<UserRole> userRoles = userRoleRepository.findByRole(role);
        return userRoles.stream()
                .map(userRole -> convertToDTO(userRole.getUtilisateur()))
                .collect(Collectors.toList());
    }

    // Validate the email format
    private void validateEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email invalide : " + email);
        }
    }

    // Convert a Utilisateur entity to a DTO
    private UtilisateurDTO convertToDTO(Utilisateur utilisateur) {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setNom(utilisateur.getNom());
        dto.setPrenom(utilisateur.getPrenom());
        dto.setEmail(utilisateur.getEmail());

        // Get roles of the user as strings
        List<String> roles = utilisateur.getUserRoles().stream()
                .map(userRole -> userRole.getRole().name())
                .collect(Collectors.toList());
        dto.setRoles(roles);

        return dto;
    }
}
