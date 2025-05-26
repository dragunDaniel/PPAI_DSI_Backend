package com.grupo7.application.repository;

// Dependencies
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Entidades
import com.grupo7.application.entity.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
    Optional<Estado> findByNombreEstado(String nombreEstado);
}