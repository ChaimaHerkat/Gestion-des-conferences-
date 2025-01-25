package com.conference.api.dto;

import com.conference.api.entities.EvaluationEtat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO représentant une évaluation d'une soumission")
public class EvaluationDTO {

    @NotNull(message = "La note est obligatoire.")
    @Min(value = 1, message = "La note doit être au moins de 1.")
    @Max(value = 10, message = "La note doit être au maximum de 10.")
    @Schema(description = "Note attribuée par l'évaluateur (de 1 à 10).", example = "8")
    private Integer note;

    @NotNull(message = "Les commentaires sont obligatoires.")
    @Schema(description = "Commentaires détaillés sur la soumission.", example = "Bon travail mais quelques améliorations sont nécessaires.")
    private String commentaires;

    @NotNull(message = "L'état est obligatoire.")
    @Schema(description = "État actuel de l'évaluation (e.g., EN_REVISION, ACCEPTEE, REJETEE).", example = "EN_REVISION")
    private EvaluationEtat etat;

    @NotNull(message = "L'ID de la soumission est obligatoire.")
    @Schema(description = "Identifiant unique de la soumission évaluée.", example = "1")
    private Integer soumissionId;

    @NotNull(message = "L'ID de l'évaluateur est obligatoire.")
    @Schema(description = "Identifiant unique de l'évaluateur ayant réalisé cette évaluation.", example = "2")
    private Integer evaluateurId;

    @Schema(description = "Date de l'évaluation (automatiquement générée).", example = "2025-05-02", accessMode = Schema.AccessMode.READ_ONLY)
    private String dateEvaluation; // Date générée automatiquement dans le service
}
