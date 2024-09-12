package com.estate.est.repositories;

import com.estate.est.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    @Query("SELECT p FROM Property p WHERE p.owner.id = :userId")
    List<Property> getAllUserProperties(@Param("userId") Long userId);
}