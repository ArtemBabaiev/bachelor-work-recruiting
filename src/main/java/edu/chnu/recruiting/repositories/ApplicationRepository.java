package edu.chnu.recruiting.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.chnu.recruiting.models.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID>{

}
