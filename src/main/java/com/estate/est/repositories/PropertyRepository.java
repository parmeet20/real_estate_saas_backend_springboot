package com.estate.est.repositories;

import com.estate.est.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    // Existing method for getting all properties by user
    @Query("SELECT p FROM Property p WHERE p.owner.id = :userId")
    List<Property> getAllUserProperties(@Param("userId") Long userId);

    // New method for searching properties by country
    @Query("SELECT p FROM Property p WHERE p.country = :country")
    List<Property> findByCountry(@Param("country") String country);
}
