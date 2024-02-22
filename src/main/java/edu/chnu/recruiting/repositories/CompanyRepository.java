package edu.chnu.recruiting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.chnu.recruiting.models.Company;
import edu.chnu.recruiting.models.security.User;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>{
	boolean existsByName(String name);
	
	Company findByOwner(User owner);
}
