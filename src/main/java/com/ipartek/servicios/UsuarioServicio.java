package com.ipartek.servicios;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.ipartek.modelo.Usuario;

public interface UsuarioServicio {

	public ResponseEntity<?> obtenerTodos();

	public ResponseEntity<?> obtenerPorId(Integer id);

	public ResponseEntity<?> obtenerPorUser(String user);

	public ResponseEntity<?> insertar(Usuario usu);

	public ResponseEntity<?> modificar(Usuario usu);

	public ResponseEntity<?> borrar(Integer id);

	public ResponseEntity<?> sumarIntento(Usuario usu);

	public ResponseEntity<?> resetearIntentos(Usuario usu);

	public ResponseEntity<?> validarLogin(Usuario login);

	public ResponseEntity<?> banear(Integer id);

	public ResponseEntity<?> desbanear(Integer id);

	public ResponseEntity<?> cambiarPassword(Integer id, String nuevoPass);

	public ResponseEntity<?> guardarFoto(MultipartFile archivo);

	// Métodos compatibles con las pruebas de evaluación del profesor (JWT y Recuperación)
	public ResponseEntity<?> validarUsuario(Usuario usu);

	public ResponseEntity<?> validarRecuperacion(String usu, Integer preg1, Integer preg2, Integer preg3,
			String resp1, String resp2, String resp3);
}
