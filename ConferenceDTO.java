package com.conference.api.dto;

import com.conference.api.entities.ConferenceEtat;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ConferenceDTO {

    @NotNull(message = "Le titre est obligatoire")
    @Schema(description = "Titre de la conférence", example = "Conférence Java 2025")
    private String titre;

    @NotNull(message = "La date de début est obligatoire")
    @Schema(description = "Date de début de la conférence", example = "2025-05-01")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    @Schema(description = "Date de fin de la conférence", example = "2025-05-05")
    private LocalDate dateFin;

    @NotNull(message = "La thématique est obligatoire")
    @Schema(description = "Thématique de la conférence", example = "Intelligence Artificielle")
    private String thematique;

    @NotNull(message = "L'état est obligatoire")
    @Schema(description = "État actuel de la conférence", example = "OUVERTE")
    private ConferenceEtat etat;

    @Schema(description = "Liste des IDs des soumissions associées à la conférence")
    private List<Integer> soumissions;

    @Schema(description = "ID du créateur de la conférence", example = "1")
    private Integer createurId;
}
