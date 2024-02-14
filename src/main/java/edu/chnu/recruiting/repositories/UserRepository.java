package edu.chnu.recruiting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.chnu.recruiting.models.security.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
