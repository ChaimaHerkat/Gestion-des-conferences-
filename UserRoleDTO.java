package com.conference.api.dto;

import com.conference.api.entities.Role;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleDTO {

    @Schema(description = "Identifiant unique du rôle utilisateur", example = "1")
    private Long id;

    @NotNull(message = "Le rôle est obligatoire")
    @Schema(description = "Rôle assigné", example = "EVALUATEUR")
    private Role role;

    @NotNull(message = "L'utilisateur est obligatoire")
    @Schema(description = "ID de l'utilisateur associé", example = "2")
    private Long utilisateurId;

    @NotNull(message = "La conférence est obligatoire")
    @Schema(description = "ID de la conférence associée", example = "1")
    private Long conferenceId;
}
