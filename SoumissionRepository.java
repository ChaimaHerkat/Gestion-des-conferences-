package com.conference.api.repositories;

import com.conference.api.entities.Soumission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoumissionRepository extends JpaRepository<Soumission, Integer> {
    // Trouver toutes les soumissions pour une conférence
    List<Soumission> findByConferenceId(int conferenceId);

    // Trouver toutes les soumissions créées par un auteur
    List<Soumission> findByAuteurId(int auteurId);

    // Trouver toutes les soumissions par état
    List<Soumission> findByEtat(String etat);
}
