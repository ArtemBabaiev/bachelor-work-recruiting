package edu.chnu.recruiting.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.chnu.recruiting.models.security.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	Optional<User> findByUsername(String username);
	
	boolean existsByUsernameOrEmail(String username, String email);
	
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	
	@Query(value = "SELECT * FROM users u WHERE u.username LIKE CONCAT('%', ?1, '%') AND u.role_id = ?2",
			countQuery = "SELECT * FROM users u WHERE u.username LIKE CONCAT('%', ?1, '%') AND u.role_id = ?2",
			nativeQuery = true)
	List<User> findAllUsernameLikeAndRoleIs(String username, Long roleId, Pageable pageable);

	//List<User> findAllByUsernameContaining(String like);
}
