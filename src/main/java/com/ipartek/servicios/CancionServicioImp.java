package com.ipartek.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ipartek.modelo.Cancion;
import com.ipartek.modelo.Estilo;
import com.ipartek.modelo.Usuario;
import com.ipartek.pojos.MsgEstado;
import com.ipartek.repositorio.CancionRepositorio;
import com.ipartek.repositorio.EstiloRepositorio;
import com.ipartek.repositorio.UsuarioRepositorio;

@Service
public class CancionServicioImp implements CancionServicio {

	@Autowired
	private CancionRepositorio cancionRepo;

	@Autowired
	private UsuarioRepositorio usuarioRepo;

	@Autowired
	private EstiloRepositorio estiloRepo;

	@Override
	public ResponseEntity<?> obtenerTodas() {
		try {
			List<Cancion> lista = cancionRepo.findCancionesParaLosUsuarios();

			if (lista != null && !lista.isEmpty()) {
				return ResponseEntity.ok(lista);
			} else {
				return ResponseEntity.noContent().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "NO SE PUDO OBTENER TODAS LAS CANCIONES (LVL USUARIO)"));
		}
	}

	@Override
	public ResponseEntity<?> obtenerTodasAdmin() {
		try {
			List<Cancion> lista = cancionRepo.findAll();

			if (lista != null && !lista.isEmpty()) {
				return ResponseEntity.ok(lista);
			} else {
				return ResponseEntity.noContent().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "NO SE PUDO OBTENER TODAS LAS CANCIONES (LVL ADMIN)"));
		}
	}

	@Override
	public ResponseEntity<?> obtenerPorId(Integer id) {
		try {
			Cancion can = cancionRepo.findById(id).orElse(null);

			if (can != null && can.getId() != null && can.getId() > 0) {
				return ResponseEntity.ok(can);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "NO SE PUDO OBTENER LA CANCION CON ESE ID"));
		}
	}

	@Override
	public ResponseEntity<?> obtenerTodasPorUsuario(Integer id) {
		try {
			List<Cancion> lista = cancionRepo.findCancionesPorUsuario(id);

			if (lista != null && !lista.isEmpty()) {
				return ResponseEntity.ok(lista);
			} else {
				return ResponseEntity.noContent().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "NO SE PUDO OBTENER TODAS LAS CANCIONES DE UN USUARIO POR SU ID"));
		}
	}

	@Override
	public ResponseEntity<?> buscarFiltro(Integer idUsu, Integer idEst, Integer duracionMin, Integer duracionMax,
			Boolean reportada) {
		try {
			List<Cancion> lista = cancionRepo.findFiltro(idUsu, idEst, duracionMin, duracionMax, reportada);

			if (lista != null && !lista.isEmpty()) {
				return ResponseEntity.ok(lista);
			} else {
				return ResponseEntity.noContent().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "NO SE PUDO OBTENER TODAS LAS CANCIONES CON ESE FILTRO"));
		}
	}

	@Override
	public ResponseEntity<?> insertar(Cancion can) {
		try {
			if (can == null) {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "LA CANCION VINO VACIA"));
			}

			if (can.getId() == null || can.getId() == 0) {
				// Asociar usuario y estilo completos
				if (can.getUsuario_fk() != null && can.getUsuario_fk().getId() != null) {
					Usuario u = usuarioRepo.findById(can.getUsuario_fk().getId()).orElse(null);
					if (u != null) {
						can.setUsuario_fk(u);
					}
				}
				if (can.getEstilo_fk() != null && can.getEstilo_fk().getId() != null) {
					Estilo est = estiloRepo.findById(can.getEstilo_fk().getId()).orElse(null);
					if (est != null) {
						can.setEstilo_fk(est);
					}
				}

				if (can.getReportada() == null) {
					can.setReportada(false);
				}

				Cancion canInsertada = cancionRepo.save(can);
				return ResponseEntity.status(201).body(canInsertada);
			} else {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "LA CANCION VINO CON FORMATO NO VALIDO"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new MsgEstado("500", "NO SE PUDO INSERTAR LA CANCION"));
		}
	}

	@Override
	public ResponseEntity<?> modificar(Cancion can) {
		try {
			if (can != null && can.getId() != null && can.getId() > 0) {
				Cancion existente = cancionRepo.findById(can.getId()).orElse(null);
				if (existente == null) {
					return ResponseEntity.notFound().build();
				}

				if (can.getTitulo() != null && !can.getTitulo().isBlank()) {
					existente.setTitulo(can.getTitulo());
				}
				if (can.getEnlace() != null && !can.getEnlace().isBlank()) {
					existente.setEnlace(can.getEnlace());
				}
				if (can.getDuracion() != null) {
					existente.setDuracion(can.getDuracion());
				}
				if (can.getDescripcion() != null) {
					existente.setDescripcion(can.getDescripcion());
				}
				if (can.getEstilo_fk() != null && can.getEstilo_fk().getId() != null) {
					Estilo est = estiloRepo.findById(can.getEstilo_fk().getId()).orElse(null);
					if (est != null) {
						existente.setEstilo_fk(est);
					}
				}
				if (can.getReportada() != null) {
					existente.setReportada(can.getReportada());
				}

				Cancion canActualizada = cancionRepo.save(existente);
				return ResponseEntity.ok(canActualizada);
			} else {
				return ResponseEntity.badRequest().body(new MsgEstado("400", "LA CANCION VINO CON FORMATO NO VALIDO"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new MsgEstado("500", "NO SE PUDO MODIFICAR LA CANCION"));
		}
	}

	@Override
	public ResponseEntity<?> borrar(Integer id) {
		try {
			if (cancionRepo.existsById(id)) {
				cancionRepo.deleteById(id);
				return ResponseEntity.ok(new MsgEstado("200", "Borrado correctamente"));
			} else {
				return ResponseEntity.ok(new MsgEstado("204", "NO ESTABA; NO SE BORRO"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new MsgEstado("500", "NO SE PUDO BORRAR LA CANCION"));
		}
	}

	@Override
	public ResponseEntity<?> reportar(Integer id) {
		try {
			Cancion canReportada = cancionRepo.findById(id).orElse(null);
			if (canReportada != null) {
				canReportada.setReportada(true);
				canReportada = cancionRepo.save(canReportada);
				return ResponseEntity.ok(canReportada);
			} else {
				return ResponseEntity.ok(new MsgEstado("204", "NO ESTABA; NO SE REPORTO"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new MsgEstado("500", "NO SE PUDO REPORTAR LA CANCION"));
		}
	}

	@Override
	public ResponseEntity<?> buscarCriterios(String titulo, Integer durMin, Integer durMax, Integer idEst,
			String textoDesc) {
		try {
			List<Cancion> lista = cancionRepo.buscarCancionesCriterios(titulo, durMin, durMax, idEst, textoDesc);
			if (lista != null && !lista.isEmpty()) {
				return ResponseEntity.ok(lista);
			} else {
				return ResponseEntity.noContent().build();
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "ERROR BUSCANDO CANCIONES POR CRITERIOS"));
		}
	}
}
