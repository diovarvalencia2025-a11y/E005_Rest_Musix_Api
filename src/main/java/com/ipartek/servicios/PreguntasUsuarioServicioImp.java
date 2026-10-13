package com.ipartek.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ipartek.modelo.Pregunta;
import com.ipartek.modelo.PreguntasUsuario;
import com.ipartek.modelo.Usuario;
import com.ipartek.pojos.MsgEstado;
import com.ipartek.repositorio.PreguntaRepositorio;
import com.ipartek.repositorio.PreguntasUsuarioRepositorio;
import com.ipartek.repositorio.UsuarioRepositorio;

@Service
public class PreguntasUsuarioServicioImp implements PreguntasUsuarioServicio {

	@Autowired
	private PreguntasUsuarioRepositorio PregUsuRepo;

	@Autowired
	private UsuarioRepositorio usuarioRepo;

	@Autowired
	private PreguntaRepositorio preguntaRepo;

	@Override
	public ResponseEntity<?> obtenerTodas() {
		try {
			List<PreguntasUsuario> lista = PregUsuRepo.findAll();

			if (lista.size() > 0) {
				return ResponseEntity.ok(lista);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "NO SE PUDO OBTENER TODAS LAS PREGUNTAS DE USUARIOS"));
		}
	}

	@Override
	public ResponseEntity<?> obtenerPorIdUsuario(Integer id) {
		try {
			List<PreguntasUsuario> lista = PregUsuRepo.findPrPreguntasUsuarioByUsuarioId(id);

			if (lista.size() > 0) {
				return ResponseEntity.ok(lista);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "NO SE PUDO OBTENER LA PREGUNTA DE USUARIOS CON ESE ID"));
		}
	}

	@Override
	public ResponseEntity<?> insertar(PreguntasUsuario pregUsu) {
		try {
			if (pregUsu == null) {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "LA PREGUNTA DE USUARIO VINO VACIA"));
			}

			// Soporta tanto ID nulo como 0
			if (pregUsu.getId() == null || pregUsu.getId() == 0) {
				// Cargar entidades completas si vienen solo con ID
				if (pregUsu.getUsuario_fk() != null && pregUsu.getUsuario_fk().getId() != null) {
					Usuario u = usuarioRepo.findById(pregUsu.getUsuario_fk().getId()).orElse(null);
					if (u != null) {
						pregUsu.setUsuario_fk(u);
					}
				}
				if (pregUsu.getPregunta_fk() != null && pregUsu.getPregunta_fk().getId() != null) {
					Pregunta p = preguntaRepo.findById(pregUsu.getPregunta_fk().getId()).orElse(null);
					if (p != null) {
						pregUsu.setPregunta_fk(p);
					}
				}

				if (pregUsu.getRespuesta() == null || pregUsu.getRespuesta().isBlank()) {
					return ResponseEntity.badRequest().body(new MsgEstado("400", "LA RESPUESTA NO PUEDE ESTAR VACIA"));
				}

				PreguntasUsuario PregUsuInsertado = PregUsuRepo.save(pregUsu);
				return ResponseEntity.status(201).body(PregUsuInsertado);
			} else {
				return ResponseEntity.badRequest()
						.body(new MsgEstado("400", "LA PREGUNTA DE USUARIO VINO CON FORMATO NO VALIDO"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "NO SE PUDO INSERTAR LA PREGUNTA DE USUARIO"));
		}
	}

	@Override
	public ResponseEntity<?> modificar(PreguntasUsuario pregUsu) {
		try {
			if (pregUsu != null && pregUsu.getId() != null && pregUsu.getId() > 0) {
				PreguntasUsuario PregUsuModificado = PregUsuRepo.save(pregUsu);
				return ResponseEntity.ok(PregUsuModificado);
			} else {
				return ResponseEntity.badRequest()
						.body(new MsgEstado("400", "LA PREGUNTA DE USUARIO VINO CON FORMATO NO VALIDO"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "NO SE PUDO MODIFICAR LA PREGUNTA DE USUARIO"));
		}
	}

	@Override
	public ResponseEntity<?> obtenerPorUsername(String username) {
		try {
			List<PreguntasUsuario> lista = PregUsuRepo.findByUsuario_fk_User(username);
			if (lista != null && !lista.isEmpty()) {
				return ResponseEntity.ok(lista);
			}
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "ERROR OBTENIENDO PREGUNTAS DEL USUARIO"));
		}
	}

	@Override
	public ResponseEntity<?> validarRespuestas(String username, String r1, String r2, String r3) {
		try {
			List<PreguntasUsuario> guardadas = PregUsuRepo.findByUsuario_fk_User(username);
			if (guardadas == null || guardadas.size() < 3) {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "El usuario no tiene 3 preguntas configuradas"));
			}

			String g1 = guardadas.get(0).getRespuesta().trim().toLowerCase();
			String g2 = guardadas.get(1).getRespuesta().trim().toLowerCase();
			String g3 = guardadas.get(2).getRespuesta().trim().toLowerCase();

			boolean v1 = r1 != null && r1.trim().equalsIgnoreCase(g1);
			boolean v2 = r2 != null && r2.trim().equalsIgnoreCase(g2);
			boolean v3 = r3 != null && r3.trim().equalsIgnoreCase(g3);

			if (v1 && v2 && v3) {
				return ResponseEntity.ok(new MsgEstado("200", "Respuestas correctas"));
			} else {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "Respuestas incorrectas"));
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new MsgEstado("500", "Error validando respuestas"));
		}
	}
}
