package com.ipartek.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipartek.servicios.EstiloServicio;

@RestController
@RequestMapping("/api/v1/estilos")
@CrossOrigin(origins = "*")
public class EstiloControlador {

	@Autowired
	private EstiloServicio estiloServicio;

	@GetMapping({"", "/"})
	public ResponseEntity<?> obtenerTodos() {
		return estiloServicio.obtenerTodos();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> obtenerPorId(@PathVariable("id") Integer id) {
		return estiloServicio.obtenerPorId(id);
	}
}
