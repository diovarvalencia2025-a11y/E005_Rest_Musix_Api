package com.ipartek.servicios;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ipartek.auxiliar.HashUtil;
import com.ipartek.modelo.Rol;
import com.ipartek.modelo.Usuario;
import com.ipartek.pojos.MsgEstado;
import com.ipartek.repositorio.CancionRepositorio;
import com.ipartek.repositorio.PreguntasUsuarioRepositorio;
import com.ipartek.repositorio.RolRepositorio;
import com.ipartek.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServicioImp implements UsuarioServicio {

	@Autowired
	private UsuarioRepositorio usuarioRepo;

	@Autowired
	private RolRepositorio rolRepo;

	@Autowired
	private CancionRepositorio cancionRepo;

	@Autowired
	private PreguntasUsuarioRepositorio pregUsuRepo;

	@Autowired(required = false)
	private com.ipartek.componente.JwtUtil jwtUtil;

	@Override
	public ResponseEntity<?> obtenerTodos() {
		try {
			List<Usuario> lista = usuarioRepo.findAll();

			if (lista.size() > 0) {
				return ResponseEntity.ok(lista);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "NO SE PUDO OBTENER TODOS LOS USUARIOS"));
		}
	}

	@Override
	public ResponseEntity<?> obtenerPorId(Integer id) {
		try {
			Usuario usu = usuarioRepo.findById(id).orElse(null);

			if (usu != null && usu.getId() != null && usu.getId() > 0) {
				return ResponseEntity.ok(usu);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "NO SE PUDO OBTENER EL USUARIO CON ESE ID"));
		}
	}

	@Override
	public ResponseEntity<?> obtenerPorUser(String user) {
		try {
			Usuario usu = usuarioRepo.findByUser(user).orElse(null);
			if (usu != null) {
				return ResponseEntity.ok(usu);
			}
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "ERROR BUSCANDO USUARIO POR USERNAME"));
		}
	}

	@Override
	public ResponseEntity<?> insertar(Usuario usu) {
		try {
			if (usu == null) {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "EL USUARIO VINO VACIO"));
			}

			if (usu.getUser() == null || usu.getUser().isBlank()) {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "EL NOMBRE DE USUARIO ES OBLIGATORIO"));
			}

			if (usuarioRepo.existsByUser(usu.getUser())) {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "EL USUARIO YA EXISTE"));
			}

			if (usu.getId() == null || usu.getId() == 0) {
				// 1. Asignar rol por defecto (USER) si no viene asignado
				if (usu.getRol_fk() == null || usu.getRol_fk().getId() == null) {
					Rol rolUser = rolRepo.findByNombre("USER")
							.orElseGet(() -> rolRepo.findById(2).orElseGet(() -> rolRepo.save(new Rol(null, "USER"))));
					usu.setRol_fk(rolUser);
				} else {
					Rol rolExistente = rolRepo.findById(usu.getRol_fk().getId()).orElse(null);
					if (rolExistente != null) {
						usu.setRol_fk(rolExistente);
					}
				}

				// 2. Asignar foto por defecto si viene nula o vacía
				if (usu.getFoto() == null || usu.getFoto().isBlank()) {
					usu.setFoto("default.png");
				}

				// 3. Generar sal y encriptar contraseña con sal + pimienta Galea
				if (usu.getSalt() == null || usu.getSalt().isBlank()) {
					usu.setSalt(HashUtil.generarSalt());
				}

				String passPlanaOCliente = usu.getPass() != null ? usu.getPass() : "";
				// Si no viene con formato sha256 de 64 caracteres, hashear primero con Galea
				String hashCliente = passPlanaOCliente.length() == 64 ? passPlanaOCliente
						: HashUtil.sha256(passPlanaOCliente + "Galea");
				String passFinalConSal = HashUtil.calcularHashFinal(hashCliente, usu.getSalt());
				usu.setPass(passFinalConSal);

				// 4. Intentos iniciales en 0
				usu.setIntentos(0);

				Usuario usuInsertado = usuarioRepo.save(usu);
				return ResponseEntity.status(201).body(usuInsertado);
			} else {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "EL USUARIO VINO CON FORMATO NO VALIDO"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new MsgEstado("500", "NO SE PUDO INSERTAR EL USUARIO"));
		}
	}

	@Override
	public ResponseEntity<?> modificar(Usuario usu) {
		try {
			if (usu != null && usu.getId() != null && usu.getId() > 0) {
				Usuario existente = usuarioRepo.findById(usu.getId()).orElse(null);
				if (existente == null) {
					return ResponseEntity.notFound().build();
				}

				// Actualizar campos
				if (usu.getUser() != null && !usu.getUser().isBlank()) {
					existente.setUser(usu.getUser());
				}
				if (usu.getFoto() != null && !usu.getFoto().isBlank()) {
					existente.setFoto(usu.getFoto());
				}
				if (usu.getRol_fk() != null && usu.getRol_fk().getId() != null) {
					Rol r = rolRepo.findById(usu.getRol_fk().getId()).orElse(null);
					if (r != null) {
						existente.setRol_fk(r);
					}
				}
				if (usu.getIntentos() != null) {
					existente.setIntentos(usu.getIntentos());
				}

				Usuario usuActualizado = usuarioRepo.save(existente);
				return ResponseEntity.ok(usuActualizado);
			} else {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "EL USUARIO VINO CON FORMATO NO VALIDO"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new MsgEstado("500", "NO SE PUDO MODIFICAR EL USUARIO"));
		}
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public ResponseEntity<?> borrar(Integer id) {
		try {
			if (usuarioRepo.existsById(id)) {
				pregUsuRepo.deleteByUsuario_fk_Id(id);
				cancionRepo.deleteByUsuario_fk_Id(id);
				usuarioRepo.deleteById(id);
				return ResponseEntity.ok(new MsgEstado("200", "Borrado correctamente"));
			} else {
				return ResponseEntity.ok(new MsgEstado("204", "NO ESTABA; NO SE BORRO"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new MsgEstado("500", "NO SE PUDO BORRAR EL USUARIO"));
		}
	}

	@Override
	public ResponseEntity<?> sumarIntento(Usuario usu) {
		try {
			if (usu == null || usu.getId() == null) {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "USUARIO NO VALIDO"));
			}

			Usuario usuTemp = usuarioRepo.findById(usu.getId()).orElse(null);

			if (usuTemp != null) {
				int actuales = usuTemp.getIntentos() != null ? usuTemp.getIntentos() : 0;
				usuTemp.setIntentos(actuales + 1);

				Usuario usuActualizado = usuarioRepo.save(usuTemp);
				return ResponseEntity.ok(usuActualizado);
			} else {
				return ResponseEntity.ok(new MsgEstado("204", "NO ESTABA; NO SE SUMO INTENTO"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new MsgEstado("500", "NO SE PUDO SUMAR EL INTENTO"));
		}
	}

	@Override
	public ResponseEntity<?> resetearIntentos(Usuario usu) {
		try {
			if (usu == null || usu.getId() == null) {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "USUARIO NO VALIDO"));
			}

			Usuario usuTemp = usuarioRepo.findById(usu.getId()).orElse(null);

			if (usuTemp != null) {
				usuTemp.setIntentos(0);

				Usuario usuActualizado = usuarioRepo.save(usuTemp);
				return ResponseEntity.ok(usuActualizado);
			} else {
				return ResponseEntity.ok(new MsgEstado("204", "NO ESTABA; NO SE RESETEARON LOS INTENTOS"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "NO SE PUDIERON RESETEAR LOS INTENTOS"));
		}
	}

	@Override
	public ResponseEntity<?> validarLogin(Usuario login) {
		try {
			if (login == null || login.getUser() == null || login.getUser().isBlank()) {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "El usuario es obligatorio"));
			}

			Usuario usu = usuarioRepo.findByUser(login.getUser()).orElse(null);
			if (usu == null) {
				return ResponseEntity.badRequest().body(new MsgEstado("404", "Usuario o contraseña incorrectos"));
			}

			// Verificar si el usuario está bloqueado por intentos
			if (usu.getIntentos() != null && usu.getIntentos() >= 3) {
				return ResponseEntity.status(403)
						.body(new MsgEstado("403", "Usuario bloqueado tras 3 intentos fallidos. Restaura tu contraseña."));
			}

			// Verificar si el usuario está baneado o bloqueado por rol
			if (usu.getRol_fk() != null) {
				Integer rId = usu.getRol_fk().getId();
				String rNom = usu.getRol_fk().getNombre();
				if ((rId != null && (rId == 4 || rId == 3)) ||
					(rNom != null && ("BANEADO".equalsIgnoreCase(rNom) || "BLOQUEADO".equalsIgnoreCase(rNom)))) {
					return ResponseEntity.status(403)
							.body(new MsgEstado("403", "Este usuario ha sido bloqueado o baneado por un administrador."));
				}
			}

			// Validar contraseña
			String passRecibida = login.getPass() != null ? login.getPass() : "";
			String hashCliente = passRecibida.length() == 64 ? passRecibida : HashUtil.sha256(passRecibida + "Galea");
			String hashCalculadoConSal = HashUtil.calcularHashFinal(hashCliente, usu.getSalt());

			if (hashCalculadoConSal.equalsIgnoreCase(usu.getPass())) {
				// Login correcto: resetear intentos
				usu.setIntentos(0);
				usuarioRepo.save(usu);
				return ResponseEntity.ok(usu);
			} else {
				// Login fallido: sumar intento
				int nuevosIntentos = (usu.getIntentos() != null ? usu.getIntentos() : 0) + 1;
				usu.setIntentos(nuevosIntentos);
				usuarioRepo.save(usu);

				if (nuevosIntentos >= 3) {
					return ResponseEntity.status(403)
							.body(new MsgEstado("403", "Has alcanzado 3 intentos fallidos. Tu cuenta ha sido bloqueada."));
				} else {
					return ResponseEntity.badRequest()
							.body(new MsgEstado("400", "Usuario o contraseña incorrectos. Intentos: " + nuevosIntentos + "/3"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new MsgEstado("500", "Error validando credenciales"));
		}
	}

	@Override
	public ResponseEntity<?> banear(Integer id) {
		try {
			Usuario u = usuarioRepo.findById(id).orElse(null);
			if (u == null) {
				return ResponseEntity.notFound().build();
			}

			Rol rolBaneado = rolRepo.findByNombre("BANEADO")
					.or(() -> rolRepo.findById(4))
					.or(() -> rolRepo.findByNombre("BLOQUEADO"))
					.or(() -> rolRepo.findById(3))
					.orElseGet(() -> rolRepo.save(new Rol(null, "BANEADO")));

			u.setRol_fk(rolBaneado);
			u.setIntentos(3);
			Usuario guardado = usuarioRepo.save(u);
			return ResponseEntity.ok(guardado);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new MsgEstado("500", "Error al banear usuario"));
		}
	}

	@Override
	public ResponseEntity<?> desbanear(Integer id) {
		try {
			Usuario u = usuarioRepo.findById(id).orElse(null);
			if (u == null) {
				return ResponseEntity.notFound().build();
			}

			Rol rolUser = rolRepo.findByNombre("USUARIO")
					.or(() -> rolRepo.findByNombre("USER"))
					.or(() -> rolRepo.findById(2))
					.or(() -> rolRepo.findById(5))
					.orElseGet(() -> rolRepo.save(new Rol(null, "USER")));

			u.setRol_fk(rolUser);
			u.setIntentos(0);
			Usuario guardado = usuarioRepo.save(u);
			return ResponseEntity.ok(guardado);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new MsgEstado("500", "Error al desbanear usuario"));
		}
	}

	@Override
	public ResponseEntity<?> cambiarPassword(Integer id, String nuevoPass) {
		try {
			Usuario u = usuarioRepo.findById(id).orElse(null);
			if (u == null) {
				return ResponseEntity.notFound().build();
			}

			if (u.getSalt() == null || u.getSalt().isBlank()) {
				u.setSalt(HashUtil.generarSalt());
			}

			String hashCliente = nuevoPass.length() == 64 ? nuevoPass : HashUtil.sha256(nuevoPass + "Galea");
			String hashConSal = HashUtil.calcularHashFinal(hashCliente, u.getSalt());

			u.setPass(hashConSal);
			u.setIntentos(0); // Desbloquea la cuenta al cambiar contraseña
			usuarioRepo.save(u);
			return ResponseEntity.ok(new MsgEstado("200", "Contraseña cambiada y cuenta desbloqueada"));
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new MsgEstado("500", "Error cambiando contraseña"));
		}
	}

	@Override
	public ResponseEntity<?> guardarFoto(MultipartFile archivo) {
		try {
			if (archivo == null || archivo.isEmpty()) {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "El archivo de imagen está vacío"));
			}

			// Asegurar que las carpetas existan
			Path dirRaiz = Paths.get("imagenes");
			File fRaiz = dirRaiz.toFile();
			if (!fRaiz.exists()) {
				fRaiz.mkdirs();
			}

			Path dirStatic = Paths.get("src", "main", "resources", "static", "imagenes");
			File fStatic = dirStatic.toFile();
			if (!fStatic.exists()) {
				fStatic.mkdirs();
			}

			// Nombre único
			String extension = "";
			String orig = archivo.getOriginalFilename();
			if (orig != null && orig.contains(".")) {
				extension = orig.substring(orig.lastIndexOf("."));
			} else {
				extension = ".png";
			}
			String nombreGenerado = "user_" + UUID.randomUUID().toString().substring(0, 8) + extension;

			// Guardar en ambas ubicaciones para disponibilidad inmediata
			Path destino1 = dirRaiz.resolve(nombreGenerado);
			try (InputStream is = archivo.getInputStream()) {
				Files.copy(is, destino1, StandardCopyOption.REPLACE_EXISTING);
			}

			try {
				Path destino2 = dirStatic.resolve(nombreGenerado);
				try (InputStream is = archivo.getInputStream()) {
					Files.copy(is, destino2, StandardCopyOption.REPLACE_EXISTING);
				}
			} catch (Exception ignored) {
			}

			return ResponseEntity.ok(new MsgEstado("200", nombreGenerado));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new MsgEstado("500", "Error guardando la foto de perfil"));
		}
	}

	// ==========================================
	// MÉTODOS DE COMPATIBILIDAD CON EL PROFESOR
	// ==========================================

	@Override
	public ResponseEntity<?> validarUsuario(Usuario usu) {
		try {
			if (usu == null || usu.getUser() == null || usu.getUser().isBlank()) {
				return ResponseEntity.ok(new MsgEstado("200", "usuario inexistente"));
			}

			Usuario usuTemp = usuarioRepo.findByUser(usu.getUser()).orElse(null);

			if (usuTemp != null && usuTemp.getId() != null && usuTemp.getId() > 0) {
				// Comprobación de contraseña: soporta hashing del profesor y hashing con Galea
				String passPlanaOCliente = usu.getPass() != null ? usu.getPass() : "";
				String passCalculadaProfesor = com.ipartek.auxiliar.Auxiliar.hashear(passPlanaOCliente + usuTemp.getSalt());
				
				String hashCliente = passPlanaOCliente.length() == 64 ? passPlanaOCliente : HashUtil.sha256(passPlanaOCliente + "Galea");
				String passCalculadaGalea = HashUtil.calcularHashFinal(hashCliente, usuTemp.getSalt());

				boolean passCoincide = passCalculadaProfesor.equalsIgnoreCase(usuTemp.getPass())
						|| passCalculadaGalea.equalsIgnoreCase(usuTemp.getPass())
						|| passPlanaOCliente.equalsIgnoreCase(usuTemp.getPass());

				String nombreRol = (usuTemp.getRol_fk() != null && usuTemp.getRol_fk().getNombre() != null)
						? usuTemp.getRol_fk().getNombre() : "USUARIO";

				if (passCoincide && (nombreRol.equalsIgnoreCase("ADMIN") || nombreRol.equalsIgnoreCase("USUARIO") || nombreRol.equalsIgnoreCase("USER"))) {
					// Login exitoso: resetear intentos
					usuTemp.setIntentos(0);
					usuarioRepo.save(usuTemp);

					if (jwtUtil != null) {
						String token = jwtUtil.generateToken(usuTemp.getUser(), nombreRol.toUpperCase());
						return ResponseEntity.ok(token);
					} else {
						return ResponseEntity.ok(new MsgEstado("200", "Login correcto"));
					}
				} else if (passCoincide && (nombreRol.equalsIgnoreCase("BLOQUEADO") || nombreRol.equalsIgnoreCase("BANEADO"))) {
					return ResponseEntity.ok(new MsgEstado("200", "Usuario bloqueado o baneado"));
				} else if (!passCoincide) {
					// Falló contraseña: sumar intento
					int actuales = usuTemp.getIntentos() != null ? usuTemp.getIntentos() : 0;
					usuTemp.setIntentos(actuales + 1);
					usuarioRepo.save(usuTemp);

					if (usuTemp.getIntentos() >= 3) {
						Rol rolBloq = rolRepo.findByNombre("BLOQUEADO")
								.or(() -> rolRepo.findById(3))
								.orElseGet(() -> rolRepo.save(new Rol(null, "BLOQUEADO")));
						usuTemp.setRol_fk(rolBloq);
						usuarioRepo.save(usuTemp);
						return ResponseEntity.ok(new MsgEstado("200", "te acaban de bloquear la cuenta"));
					}

					return ResponseEntity.ok(new MsgEstado("200", "usuario metido mal la pass"));
				}
			} else {
				return ResponseEntity.ok(new MsgEstado("200", "usuario inexistente"));
			}

			return ResponseEntity.ok(new MsgEstado("200", "Usuario o pass no valido"));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new MsgEstado("500", "NO SE PUDIERON VALIDAR CREDENCIALES"));
		}
	}

	@Override
	public ResponseEntity<?> validarRecuperacion(String usu, Integer preg1, Integer preg2, Integer preg3,
			String resp1, String resp2, String resp3) {
		try {
			if (usu == null || usu.isBlank()) {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "Usuario obligatorio"));
			}

			Usuario usuTemp = usuarioRepo.findByUser(usu).orElse(null);
			if (usuTemp == null) {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "Usuario inexistente"));
			}

			List<com.ipartek.modelo.PreguntasUsuario> guardadas = pregUsuRepo.findByUsuario_fk_User(usu);
			if (guardadas == null || guardadas.size() < 3) {
				guardadas = pregUsuRepo.findPrPreguntasUsuarioByUsuarioId(usuTemp.getId());
			}

			if (guardadas == null || guardadas.size() < 3) {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "El usuario no tiene 3 preguntas configuradas"));
			}

			String sal = usuTemp.getSalt() != null ? usuTemp.getSalt() : "";

			String[] respuestasEnviadas = new String[] {
					resp1 != null ? resp1.trim() : "",
					resp2 != null ? resp2.trim() : "",
					resp3 != null ? resp3.trim() : ""
			};

			int aciertos = 0;
			for (com.ipartek.modelo.PreguntasUsuario pu : guardadas) {
				String guardada = pu.getRespuesta() != null ? pu.getRespuesta().trim() : "";
				for (String rEnv : respuestasEnviadas) {
					if (rEnv.isEmpty()) continue;
					String hashProfesor = com.ipartek.auxiliar.Auxiliar.hashear(rEnv + sal);
					if (guardada.equalsIgnoreCase(rEnv) || guardada.equalsIgnoreCase(hashProfesor)) {
						aciertos++;
						break;
					}
				}
			}

			if (aciertos >= 3) {
				return ResponseEntity.ok(new MsgEstado("200", "Respuestas de seguridad correctas"));
			} else {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "Respuestas de seguridad incorrectas"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new MsgEstado("500", "Error validando recuperación"));
		}
	}
}
