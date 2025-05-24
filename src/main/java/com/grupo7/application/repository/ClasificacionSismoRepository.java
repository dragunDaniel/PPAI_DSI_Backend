package com.grupo7.application.repository;

import com.grupo7.application.entity.ClasificacionSismo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClasificacionSismoRepository extends JpaRepository<ClasificacionSismo, Long> {
}