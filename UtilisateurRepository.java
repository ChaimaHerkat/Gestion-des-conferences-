package com.conference.api.repositories;

import com.conference.api.entities.Role;
import com.conference.api.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {

    Optional<Utilisateur> findByEmail(String email);

    // Correct query to fetch users by role using JOIN with UserRole
    @Query("SELECT u FROM Utilisateur u JOIN u.userRoles ur WHERE ur.role = :role")
    List<Utilisateur> findByRole(@Param("role") Role role);
}
