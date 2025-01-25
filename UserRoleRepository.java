package com.conference.api.repositories;

import com.conference.api.entities.Role;
import com.conference.api.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    // Retrieve UserRoles by User ID
    List<UserRole> findByUtilisateurId(int utilisateurId);

    // Retrieve UserRoles by Conference ID
    List<UserRole> findByConferenceId(int conferenceId);

    // Retrieve UserRoles by Role
    List<UserRole> findByRole(Role role);
    // Retrieve UserRole by User ID and Role
    Optional<UserRole> findByUtilisateurIdAndRole(int utilisateurId, Role role);
}
