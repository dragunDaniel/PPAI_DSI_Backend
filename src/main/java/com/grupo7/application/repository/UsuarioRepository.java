package com.grupo7.application.repository;

// Dependencies
import org.springframework.data.jpa.repository.JpaRepository;

// Entidades
import com.grupo7.application.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
