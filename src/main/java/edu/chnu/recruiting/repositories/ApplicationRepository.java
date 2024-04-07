package edu.chnu.recruiting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.chnu.recruiting.models.ApplicationSummary;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationSummary, Long>, JpaSpecificationExecutor<ApplicationSummary>{
	@Modifying
	@Query("update ApplicationSummary a set a.status = :uStatus where a.id = :sId")
	void updateStatus(@Param("sId") Long sId, @Param("uStatus") String uStatus);
	
	@Modifying
	@Query("update ApplicationSummary a set a.status = :uStatus, a.rejectReason = :uReason where a.id = :sId")
	void updateStatusAndReason(@Param("sId") Long sId, @Param("uStatus") String uStatus, @Param("uReason") String uReason);
}
