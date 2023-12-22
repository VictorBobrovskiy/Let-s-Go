package com.digsol.main.location;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    Collection<Location> findAllByLatAndLon(Double lat, Double lon);

    @Query(value = "SELECT l.* " +
            "FROM locations l " +
            "WHERE distance(l.lat, l.lon, :userLat, :userLon) <= :distance " +
            "ORDER BY distance(l.lat, l.lon, :userLat, :userLon)",
            nativeQuery = true)
    Page<Location> findAllByDistanceOrderByDistanceAsc(Double userLat, Double userLon, Double distance, Pageable pageable);

    @Query(value = "SELECT l.* " +
            "FROM locations l " +
            "ORDER BY distance(l.lat, l.lon, :userLat, :userLon)",
            nativeQuery = true)
    Page<Location> findAllOrderByDistanceAsc(Double userLat, Double userLon, Pageable pageable);


    @Query(value = "SELECT l.* FROM locations l ORDER BY l.name", nativeQuery = true)
    Page<Location> findAllOrderByName(Pageable pageable);

    Page<Location> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<Location> findByName(String name);
}