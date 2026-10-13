package com.ipartek.servicios;

import org.springframework.http.ResponseEntity;

import com.ipartek.modelo.Cancion;

public interface CancionServicio {

	public ResponseEntity<?> obtenerTodas();

	public ResponseEntity<?> obtenerTodasAdmin();

	public ResponseEntity<?> obtenerPorId(Integer id);

	public ResponseEntity<?> obtenerTodasPorUsuario(Integer id);

	public ResponseEntity<?> buscarFiltro(Integer idUsu, Integer idEst, Integer duracionMin, Integer duracionMax, Boolean reportada);

	public ResponseEntity<?> insertar(Cancion can);

	public ResponseEntity<?> modificar(Cancion can);

	public ResponseEntity<?> borrar(Integer id);

	public ResponseEntity<?> reportar(Integer id);

	public ResponseEntity<?> buscarCriterios(String titulo, Integer durMin, Integer durMax, Integer idEst, String textoDesc);
}
