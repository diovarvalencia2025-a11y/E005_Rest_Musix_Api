package com.ipartek.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ipartek.modelo.Cancion;

@Repository
public interface CancionRepositorio extends JpaRepository<Cancion, Integer> {

	Optional<Cancion> findByTitulo(String titulo);

	List<Cancion> findByReportadaFalse();

	// Excluye canciones de usuarios con rol 4 (BANEADO)
	@Query("SELECT c FROM Cancion c WHERE c.usuario_fk.rol_fk.id <> 4")
	List<Cancion> findCancionesParaLosUsuarios();

	// Canciones de un usuario específico
	@Query("SELECT c FROM Cancion c WHERE c.usuario_fk.id = :usuarioId")
	List<Cancion> findCancionesPorUsuario(@Param("usuarioId") Integer usuarioId);

	@Query("SELECT c FROM Cancion c WHERE c.usuario_fk.id = :usuarioId")
	List<Cancion> findByUsuario_fk_Id(@Param("usuarioId") Integer usuarioId);

	// Filtro dinámico del Administrador
	@Query("""
			SELECT c FROM Cancion c
			WHERE (:idUsu IS NULL OR c.usuario_fk.id = :idUsu)
			  AND (:idEst IS NULL OR c.estilo_fk.id = :idEst)
			  AND (:durMin IS NULL OR c.duracion >= :durMin)
			  AND (:durMax IS NULL OR c.duracion <= :durMax)
			  AND (:reportada IS NULL OR c.reportada = :reportada)
			""")
	List<Cancion> findFiltro(
			@Param("idUsu") Integer idUsu,
			@Param("idEst") Integer idEst,
			@Param("durMin") Integer duracionMin,
			@Param("durMax") Integer duracionMax,
			@Param("reportada") Boolean reportada);

	// Búsqueda avanzada según enunciado: por título, duración entre 2 valores, estilo y texto en la descripción
	@Query("""
			SELECT c FROM Cancion c
			WHERE (:titulo IS NULL OR LOWER(c.titulo) LIKE LOWER(CONCAT('%', :titulo, '%')))
			  AND (:durMin IS NULL OR c.duracion >= :durMin)
			  AND (:durMax IS NULL OR c.duracion <= :durMax)
			  AND (:idEst IS NULL OR c.estilo_fk.id = :idEst)
			  AND (:textoDesc IS NULL OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :textoDesc, '%')))
			  AND (c.usuario_fk.rol_fk.id <> 4)
			""")
	List<Cancion> buscarCancionesCriterios(
			@Param("titulo") String titulo,
			@Param("durMin") Integer durMin,
			@Param("durMax") Integer durMax,
			@Param("idEst") Integer idEst,
			@Param("textoDesc") String textoDesc);

	@org.springframework.data.jpa.repository.Modifying
	@org.springframework.transaction.annotation.Transactional
	@Query("DELETE FROM Cancion c WHERE c.usuario_fk.id = :usuarioId")
	void deleteByUsuario_fk_Id(@Param("usuarioId") Integer usuarioId);
}
