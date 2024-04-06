package edu.chnu.recruiting.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.chnu.recruiting.models.Application;
import edu.chnu.recruiting.models.Position;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID>, JpaSpecificationExecutor<Application>{
	@Modifying
	@Query("update Application a set a.status = :uStatus where a.id = :sId")
	void updateStatus(@Param("sId") UUID sId, @Param("uStatus") String uStatus);
	
	@Modifying
	@Query("update Application a set a.status = :uStatus, a.rejectReason = :uReason where a.id = :sId")
	void updateStatusAndReason(@Param("sId") UUID sId, @Param("uStatus") String uStatus, @Param("uReason") String uReason);
}
