package com.hydrotrack.repository;

import com.hydrotrack.model.Fountain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FountainRepository extends JpaRepository<Fountain, Long> {

    Optional<Fountain> findByFountainCode(String fountainCode);

    List<Fountain> findByBuildingBuildingId(Long buildingId);

    List<Fountain> findByFloorNumber(Integer floorNumber);

    List<Fountain> findByStatus(String status);

    List<Fountain> findByFountainType(String fountainType);

    @Query("SELECT f FROM Fountain f WHERE f.building.buildingId = :buildingId AND f.floorNumber = :floor")
    List<Fountain> findByBuildingAndFloor(@Param("buildingId") Long buildingId, @Param("floor") Integer floor);

    @Query("SELECT COUNT(f) FROM Fountain f WHERE f.status = :status")
    long countByStatus(@Param("status") String status);

    @Query("SELECT DISTINCT f.floorNumber FROM Fountain f ORDER BY f.floorNumber")
    List<Integer> findAllFloors();
}
