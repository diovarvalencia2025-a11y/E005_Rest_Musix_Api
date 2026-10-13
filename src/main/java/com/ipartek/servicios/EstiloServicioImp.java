package com.ipartek.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ipartek.modelo.Estilo;
import com.ipartek.pojos.MsgEstado;
import com.ipartek.repositorio.EstiloRepositorio;

@Service
public class EstiloServicioImp implements EstiloServicio {

	@Autowired
	private EstiloRepositorio estiloRepo;

	@Override
	public ResponseEntity<?> obtenerTodos() {
		try {
			List<Estilo> lista = estiloRepo.findAll();

			if (lista != null && !lista.isEmpty()) {
				return ResponseEntity.ok(lista);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "NO SE PUDO OBTENER TODOS LOS ESTILOS"));
		}
	}

	@Override
	public ResponseEntity<?> obtenerPorId(Integer id) {
		try {
			Estilo est = estiloRepo.findById(id).orElse(null);

			if (est != null && est.getId() != null && est.getId() > 0) {
				return ResponseEntity.ok(est);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "NO SE PUDO OBTENER EL ESTILO CON ESE ID"));
		}
	}

}
