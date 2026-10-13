package com.ipartek.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ipartek.modelo.Estilo;

@Repository
public interface EstiloRepositorio extends JpaRepository<Estilo, Integer> {
	Optional<Estilo> findByNombre(String nombre);
}
