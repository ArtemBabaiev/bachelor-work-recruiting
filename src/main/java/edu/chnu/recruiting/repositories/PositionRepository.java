package edu.chnu.recruiting.repositories;

import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.chnu.recruiting.models.Company;
import edu.chnu.recruiting.models.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long>, JpaSpecificationExecutor<Position> {

	Page<Position> findByNameContains(String name, Pageable page);

	List<Position> findByCompany(Company company);
	
	@Modifying
	@Query("UPDATE Position p SET p.active = :uActive WHERE p.id = :sId")
	void setActiveWhereId(@Param("sId") Long sId, @Param("uActive") Boolean uActive);
}
