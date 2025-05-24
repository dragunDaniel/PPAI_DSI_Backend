package com.grupo7.application.repository;

import com.grupo7.application.entity.Sismografo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SismografoRepository extends JpaRepository<Sismografo, Long> {
}