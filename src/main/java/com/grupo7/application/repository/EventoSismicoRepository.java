package com.grupo7.application.repository;

import com.grupo7.application.entity.EventoSismico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoSismicoRepository extends JpaRepository<EventoSismico, Long> {
}