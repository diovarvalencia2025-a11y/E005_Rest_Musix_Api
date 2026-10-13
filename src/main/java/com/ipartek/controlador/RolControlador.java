package com.ipartek.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipartek.servicios.RolServicio;

@RestController
@RequestMapping("/api/v1/roles")
@CrossOrigin(origins = "*")
public class RolControlador {

	@Autowired
	private RolServicio rolServicio;

	@GetMapping
	public ResponseEntity<?> obtenerTodos() {
		return rolServicio.obtenerTodos();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> obtenerPorId(@PathVariable("id") Integer id) {
		return rolServicio.obtenerPorId(id);
	}
}
