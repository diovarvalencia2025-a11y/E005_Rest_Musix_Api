package com.ipartek.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipartek.modelo.PreguntasUsuario;
import com.ipartek.servicios.PreguntasUsuarioServicio;

@RestController
@RequestMapping("/api/v1/preguntasusuarios")
@CrossOrigin(origins = "*")
public class PreguntasUsuarioControlador {

	@Autowired
	private PreguntasUsuarioServicio pregUsuServicio;

	@GetMapping({"", "/"})
	public ResponseEntity<?> obtenerTodas() {
		return pregUsuServicio.obtenerTodas();
	}

	@GetMapping("/usuario/{usuarioId}")
	public ResponseEntity<?> obtenerPorIdUsuario(@PathVariable("usuarioId") Integer usuarioId) {
		return pregUsuServicio.obtenerPorIdUsuario(usuarioId);
	}

	@GetMapping("/user/{username}")
	public ResponseEntity<?> obtenerPorUsername(@PathVariable("username") String username) {
		return pregUsuServicio.obtenerPorUsername(username);
	}

	@PostMapping({"", "/"})
	public ResponseEntity<?> insertar(@RequestBody PreguntasUsuario pregUsu) {
		return pregUsuServicio.insertar(pregUsu);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@RequestBody PreguntasUsuario pregUsu) {
		return pregUsuServicio.modificar(pregUsu);
	}

	@PostMapping("/validar")
	public ResponseEntity<?> validarRespuestas(
			@RequestParam("username") String username,
			@RequestParam(value = "r1", required = false) String r1,
			@RequestParam(value = "r2", required = false) String r2,
			@RequestParam(value = "r3", required = false) String r3) {
		return pregUsuServicio.validarRespuestas(username, r1, r2, r3);
	}
}
