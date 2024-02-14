package edu.chnu.recruiting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.chnu.recruiting.models.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

}
