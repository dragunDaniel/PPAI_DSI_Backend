package com.grupo7.application.repository;

import com.grupo7.application.entity.OrigenDeGeneracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrigenDeGeneracionRepository extends JpaRepository<OrigenDeGeneracion, Long> {
}