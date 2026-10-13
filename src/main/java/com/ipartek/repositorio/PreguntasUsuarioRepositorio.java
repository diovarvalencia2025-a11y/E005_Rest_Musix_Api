package com.ipartek.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ipartek.modelo.PreguntasUsuario;

@Repository
public interface PreguntasUsuarioRepositorio extends JpaRepository<PreguntasUsuario, Integer> {

	// Método con la firma exacta del profesor en v5
	@Query("SELECT p FROM PreguntasUsuario p WHERE p.usuario_fk.id = :usuario_fk")
	List<PreguntasUsuario> findPrPreguntasUsuarioByUsuarioId(@Param("usuario_fk") Integer usuario_fk);

	@Query("SELECT p FROM PreguntasUsuario p WHERE p.usuario_fk.id = :usuarioId")
	List<PreguntasUsuario> findByUsuario_fk_Id(@Param("usuarioId") Integer usuarioId);

	@Query("SELECT p FROM PreguntasUsuario p WHERE p.usuario_fk.user = :user")
	List<PreguntasUsuario> findByUsuario_fk_User(@Param("user") String user);

	@Modifying
	@Transactional
	@Query("DELETE FROM PreguntasUsuario p WHERE p.usuario_fk.id = :usuarioId")
	void deleteByUsuario_fk_Id(@Param("usuarioId") Integer usuarioId);
}
