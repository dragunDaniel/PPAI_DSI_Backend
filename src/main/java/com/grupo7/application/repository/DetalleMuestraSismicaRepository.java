package com.grupo7.application.repository;

import com.grupo7.application.entity.DetalleMuestraSismica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleMuestraSismicaRepository extends JpaRepository<DetalleMuestraSismica, Long> {
}