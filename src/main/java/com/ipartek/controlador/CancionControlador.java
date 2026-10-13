package com.ipartek.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipartek.modelo.Cancion;
import com.ipartek.servicios.CancionServicio;

@RestController
@RequestMapping("/api/v1/canciones")
@CrossOrigin(origins = "*")
public class CancionControlador {

	@Autowired
	private CancionServicio cancionServicio;

	@GetMapping({"", "/"})
	public ResponseEntity<?> obtenerTodas() {
		return cancionServicio.obtenerTodas();
	}

	@GetMapping("/admin")
	public ResponseEntity<?> obtenerTodasAdmin() {
		return cancionServicio.obtenerTodasAdmin();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> obtenerPorId(@PathVariable("id") Integer id) {
		return cancionServicio.obtenerPorId(id);
	}

	@GetMapping("/usuario/{usuarioId}")
	public ResponseEntity<?> obtenerPorUsuario(@PathVariable("usuarioId") Integer usuarioId) {
		return cancionServicio.obtenerTodasPorUsuario(usuarioId);
	}

	@GetMapping("/filtro")
	public ResponseEntity<?> buscarFiltro(
			@RequestParam(value = "idUsu", required = false) Integer idUsu,
			@RequestParam(value = "idEst", required = false) Integer idEst,
			@RequestParam(value = "duracionMin", required = false) Integer duracionMin,
			@RequestParam(value = "duracionMax", required = false) Integer duracionMax,
			@RequestParam(value = "reportada", required = false) Boolean reportada) {
		return cancionServicio.buscarFiltro(idUsu, idEst, duracionMin, duracionMax, reportada);
	}

	@GetMapping("/buscar")
	public ResponseEntity<?> buscarCriterios(
			@RequestParam(value = "titulo", required = false) String titulo,
			@RequestParam(value = "durMin", required = false) Integer durMin,
			@RequestParam(value = "durMax", required = false) Integer durMax,
			@RequestParam(value = "idEst", required = false) Integer idEst,
			@RequestParam(value = "textoDesc", required = false) String textoDesc) {
		return cancionServicio.buscarCriterios(titulo, durMin, durMax, idEst, textoDesc);
	}

	@PostMapping({"", "/"})
	public ResponseEntity<?> insertar(@RequestBody Cancion cancion) {
		return cancionServicio.insertar(cancion);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@RequestBody Cancion cancion) {
		return cancionServicio.modificar(cancion);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> modificarConId(@PathVariable("id") Integer id, @RequestBody Cancion cancion) {
		cancion.setId(id);
		return cancionServicio.modificar(cancion);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> borrar(@PathVariable("id") Integer id) {
		return cancionServicio.borrar(id);
	}

	@PutMapping("/{id}/reportar")
	public ResponseEntity<?> reportar(@PathVariable("id") Integer id) {
		return cancionServicio.reportar(id);
	}
}
