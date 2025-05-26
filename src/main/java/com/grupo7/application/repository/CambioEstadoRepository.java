package com.grupo7.application.repository;

// Dependencies
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Entidades
import com.grupo7.application.entity.CambioEstado;
import com.grupo7.application.entity.EventoSismico;


@Repository
public interface CambioEstadoRepository extends JpaRepository<CambioEstado, Long> {
    Optional<CambioEstado> findByEventoSismicoAndFechaHoraFinIsNull(EventoSismico eventoSismico);
}