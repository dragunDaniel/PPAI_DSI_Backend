package com.grupo7.application.repository;

import com.grupo7.application.entity.EventoSismico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventoSismicoRepository extends JpaRepository<EventoSismico, Long> {

    /**
     * Busca un EventoSismico por su ID, cargando eagermente todas las relaciones
     * necesarias para el DTO de DatosRegistradosDTO en una sola consulta.
     * Se usa DISTINCT para evitar duplicados en las colecciones mapeadas.
     *
     * @param id El ID del EventoSismico a buscar.
     * @return Un Optional que contiene el EventoSismico si es encontrado, con sus detalles cargados.
     */
    @Query("""
        SELECT DISTINCT e
          FROM EventoSismico e
          LEFT JOIN FETCH e.alcanceSismo
          LEFT JOIN FETCH e.clasificacionSismo
          LEFT JOIN FETCH e.origenGeneracion
          LEFT JOIN FETCH e.seriesTemporales st
          LEFT JOIN FETCH st.sismografo s
          LEFT JOIN FETCH st.muestrasSismicas ms
          LEFT JOIN FETCH ms.detallesMuestra dm
         WHERE e.id = :id
    """)
    Optional<EventoSismico> findByIdWithDetails(@Param("id") Long id);

}
