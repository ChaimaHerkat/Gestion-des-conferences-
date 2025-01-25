package com.conference.api.repositories;

import com.conference.api.entities.Conference;
import com.conference.api.entities.ConferenceEtat;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConferenceRepository extends JpaRepository<Conference, Integer> {
    List<Conference> findByThematique(String thematique);
    List<Conference> findByEtat(@NotNull(message = "L'état de la conférence est obligatoire") ConferenceEtat etat);
}
