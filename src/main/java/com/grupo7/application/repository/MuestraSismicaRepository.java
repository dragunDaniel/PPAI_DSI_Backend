package com.grupo7.application.repository;

import com.grupo7.application.entity.MuestraSismica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MuestraSismicaRepository extends JpaRepository<MuestraSismica, Long> {
}