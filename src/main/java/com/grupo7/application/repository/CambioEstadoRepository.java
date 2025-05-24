package com.grupo7.application.repository;

import com.grupo7.application.entity.CambioEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CambioEstadoRepository extends JpaRepository<CambioEstado, Long> {
    Optional<CambioEstado> findByEventoSismicoIdAndFechaHoraFinIsNull(Long eventoSismicoId);
}