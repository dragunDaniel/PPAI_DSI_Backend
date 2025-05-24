package com.grupo7.application.repository;

import com.grupo7.application.entity.MagnitudRitcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MagnitudRitcherRepository extends JpaRepository<MagnitudRitcher, Long> {
}