package com.conference.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "DTO pour représenter un utilisateur")
public class UtilisateurDTO {

    @NotNull(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    @Schema(description = "Nom de l'utilisateur", example = "Dupont")
    private String nom;

    @NotNull(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    @Schema(description = "Prénom de l'utilisateur", example = "Jean")
    private String prenom;

    @NotNull(message = "L'email est obligatoire")
    @Email(message = "Le format de l'email est invalide")
    @Schema(description = "Adresse email de l'utilisateur", example = "jean.dupont@example.com")
    private String email;

    @Schema(description = "Liste des rôles de l'utilisateur", example = "[\"AUTEUR\", \"EVALUATEUR\"]")
    private List<String> roles; // Liste de chaînes représentant les rôles
}
