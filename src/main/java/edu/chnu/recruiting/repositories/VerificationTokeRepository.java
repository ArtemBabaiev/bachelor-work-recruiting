package edu.chnu.recruiting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.security.VerificationToken;

@Repository
public interface VerificationTokeRepository extends JpaRepository<VerificationToken, Long> {
	VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}
