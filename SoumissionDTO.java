package com.conference.api.dto;

import com.conference.api.entities.SoumissionEtat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(description = "DTO représentant une soumission d'article pour une conférence scientifique.")
public class SoumissionDTO {

    @NotNull(message = "Le titre de l'article est obligatoire.")
    @Size(min = 5, max = 200, message = "Le titre doit contenir entre 5 et 200 caractères.")
    @Schema(description = "Titre de l'article soumis.", example = "Introduction à Java 19")
    private String titreArticle;

    @NotNull(message = "Le résumé est obligatoire.")
    @Size(min = 10, max = 1000, message = "Le résumé doit contenir entre 10 et 1000 caractères.")
    @Schema(description = "Résumé détaillé de l'article soumis.", example = "Cet article présente les nouveautés de Java 19.")
    private String resume;

    @NotNull(message = "L'état de la soumission est obligatoire.")
    @Schema(description = "État actuel de la soumission (e.g., EN_REVUE, ACCEPTEE, REJETEE).", example = "EN_REVUE")
    private SoumissionEtat etat;

    @Schema(description = "Date de la soumission (automatiquement définie si non fournie).", example = "2025-04-15T10:30:00")
    private LocalDateTime dateSoumission;

    @NotNull(message = "L'ID de l'auteur principal est obligatoire.")
    @Schema(description = "Identifiant unique de l'auteur principal de la soumission.", example = "1")
    private Integer auteurId;

    @NotNull(message = "L'ID de la conférence est obligatoire.")
    @Schema(description = "Identifiant unique de la conférence associée.", example = "1")
    private Integer conferenceId;

    @Schema(description = "Liste des identifiants des co-auteurs associés à la soumission.", example = "[2, 3, 4]")
    private List<Integer> coAuteurs;

    @Schema(description = "Liste des identifiants des évaluateurs assignés à la soumission.", example = "[5, 6]")
    private List<Integer> evaluateurs;

    @NotNull(message = "Le document PDF est obligatoire.")
    @Schema(description = "Chemin ou contenu du document PDF associé à la soumission.", example = "/path/to/document.pdf")
    private String documentPdf;
}
