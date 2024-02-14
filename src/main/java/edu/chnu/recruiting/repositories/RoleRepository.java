package edu.chnu.recruiting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.chnu.recruiting.models.security.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}

