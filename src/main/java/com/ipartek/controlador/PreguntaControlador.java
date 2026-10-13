package com.ipartek.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipartek.servicios.PreguntaServicio;

@RestController
@RequestMapping("/api/v1/preguntas")
@CrossOrigin(origins = "*")
public class PreguntaControlador {

	@Autowired
	private PreguntaServicio preguntaServicio;

	@GetMapping({"", "/"})
	public ResponseEntity<?> obtenerTodas() {
		return preguntaServicio.obtenerTodas();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> obtenerPorId(@PathVariable("id") Integer id) {
		return preguntaServicio.obtenerPorId(id);
	}
}
