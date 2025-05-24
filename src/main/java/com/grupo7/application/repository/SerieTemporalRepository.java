package com.grupo7.application.repository;

import com.grupo7.application.entity.SerieTemporal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SerieTemporalRepository extends JpaRepository<SerieTemporal, Long> {
}