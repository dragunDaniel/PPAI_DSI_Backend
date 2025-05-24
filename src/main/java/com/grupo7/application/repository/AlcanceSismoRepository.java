package com.grupo7.application.repository;

import com.grupo7.application.entity.AlcanceSismo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlcanceSismoRepository extends JpaRepository<AlcanceSismo, Long> {
}