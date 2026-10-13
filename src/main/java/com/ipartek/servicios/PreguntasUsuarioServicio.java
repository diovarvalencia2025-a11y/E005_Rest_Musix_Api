package com.ipartek.servicios;

import org.springframework.http.ResponseEntity;

import com.ipartek.modelo.PreguntasUsuario;

public interface PreguntasUsuarioServicio {

	public ResponseEntity<?> obtenerTodas();

	public ResponseEntity<?> obtenerPorIdUsuario(Integer id);

	public ResponseEntity<?> insertar(PreguntasUsuario pregUsu);

	public ResponseEntity<?> modificar(PreguntasUsuario pregUsu);

	public ResponseEntity<?> obtenerPorUsername(String username);

	public ResponseEntity<?> validarRespuestas(String username, String r1, String r2, String r3);
}
