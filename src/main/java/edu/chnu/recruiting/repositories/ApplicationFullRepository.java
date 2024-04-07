package edu.chnu.recruiting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import edu.chnu.recruiting.models.ApplicationFull;

@Repository
public interface ApplicationFullRepository extends JpaRepository<ApplicationFull, Long>, JpaSpecificationExecutor<ApplicationFull>{
	
}
