package com.grupo7.application.repository;

import com.grupo7.application.entity.TipoDeDato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoDeDatoRepository extends JpaRepository<TipoDeDato, Long> {
    TipoDeDato findByDenominacion(String denominacion);
}