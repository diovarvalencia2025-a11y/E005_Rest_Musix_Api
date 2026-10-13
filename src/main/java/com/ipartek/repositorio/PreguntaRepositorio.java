package com.ipartek.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ipartek.modelo.Pregunta;

@Repository
public interface PreguntaRepositorio extends JpaRepository<Pregunta, Integer>{

}
