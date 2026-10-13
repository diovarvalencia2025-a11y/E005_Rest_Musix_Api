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
import org.springframework.web.multipart.MultipartFile;

import com.ipartek.modelo.Usuario;
import com.ipartek.servicios.UsuarioServicio;

@RestController
@RequestMapping("/api/v1/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioControlador {

	@Autowired
	private UsuarioServicio usuarioServicio;

	@GetMapping({"", "/"})
	public ResponseEntity<?> obtenerTodos() {
		return usuarioServicio.obtenerTodos();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> obtenerPorId(@PathVariable("id") Integer id) {
		return usuarioServicio.obtenerPorId(id);
	}

	@GetMapping("/user/{username}")
	public ResponseEntity<?> obtenerPorUsername(@PathVariable("username") String username) {
		return usuarioServicio.obtenerPorUser(username);
	}

	@PostMapping({"", "/"})
	public ResponseEntity<?> insertar(@RequestBody Usuario usuario) {
		return usuarioServicio.insertar(usuario);
	}

	@PutMapping({"", "/"})
	public ResponseEntity<?> modificar(@RequestBody Usuario usuario) {
		return usuarioServicio.modificar(usuario);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> modificarConId(@PathVariable("id") Integer id, @RequestBody Usuario usuario) {
		usuario.setId(id);
		return usuarioServicio.modificar(usuario);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> borrar(@PathVariable("id") Integer id) {
		return usuarioServicio.borrar(id);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Usuario login) {
		return usuarioServicio.validarLogin(login);
	}

	@PutMapping("/sumar-intento")
	public ResponseEntity<?> sumarIntento(@RequestBody Usuario usu) {
		return usuarioServicio.sumarIntento(usu);
	}

	@PutMapping("/resetear-intentos")
	public ResponseEntity<?> resetearIntentos(@RequestBody Usuario usu) {
		return usuarioServicio.resetearIntentos(usu);
	}

	@PutMapping("/{id}/resetear-intentos")
	public ResponseEntity<?> resetearIntentosPorId(@PathVariable("id") Integer id) {
		Usuario usu = new Usuario();
		usu.setId(id);
		return usuarioServicio.resetearIntentos(usu);
	}

	@org.springframework.web.bind.annotation.RequestMapping(value = "/{id}/banear", method = {org.springframework.web.bind.annotation.RequestMethod.PUT, org.springframework.web.bind.annotation.RequestMethod.POST})
	public ResponseEntity<?> banear(@PathVariable("id") Integer id) {
		return usuarioServicio.banear(id);
	}

	@org.springframework.web.bind.annotation.RequestMapping(value = "/{id}/desbanear", method = {org.springframework.web.bind.annotation.RequestMethod.PUT, org.springframework.web.bind.annotation.RequestMethod.POST})
	public ResponseEntity<?> desbanear(@PathVariable("id") Integer id) {
		return usuarioServicio.desbanear(id);
	}

	@PutMapping("/{id}/cambiar-pass")
	public ResponseEntity<?> cambiarPassword(@PathVariable("id") Integer id, @RequestParam("nuevoPass") String nuevoPass) {
		return usuarioServicio.cambiarPassword(id, nuevoPass);
	}

	@PostMapping("/upload-foto")
	public ResponseEntity<?> subirFoto(@RequestParam("archivo") MultipartFile archivo) {
		return usuarioServicio.guardarFoto(archivo);
	}

	// ==========================================
	// ENDPOINTS EVALUADOS POR EL PROFESOR
	// ==========================================

	@GetMapping("/ValidarUsuario")
	public ResponseEntity<?> validarUsuario(Usuario usu) {
		return usuarioServicio.validarUsuario(usu);
	}

	@GetMapping("/ValidarRecuperacion")
	public ResponseEntity<?> validarRecuperacion(@RequestParam("usu") String usu,
			@RequestParam("preg1") Integer preg1,
			@RequestParam("preg2") Integer preg2,
			@RequestParam("preg3") Integer preg3,
			@RequestParam("resp1") String resp1,
			@RequestParam("resp2") String resp2,
			@RequestParam("resp3") String resp3) {
		return usuarioServicio.validarRecuperacion(usu, preg1, preg2, preg3, resp1, resp2, resp3);
	}
}
