package com.ipartek.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ipartek.modelo.Pregunta;
import com.ipartek.pojos.MsgEstado;
import com.ipartek.repositorio.PreguntaRepositorio;

@Service
public class PreguntaServicioImp implements PreguntaServicio {

	@Autowired
	private PreguntaRepositorio preguntaRepo;

	@Override
	public ResponseEntity<?> obtenerTodas() {
		try {
			List<Pregunta> lista = preguntaRepo.findAll();

			if (lista != null && !lista.isEmpty()) {
				return ResponseEntity.ok(lista);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "NO SE PUDO OBTENER TODAS LAS PREGUNTAS"));
		}
	}

	@Override
	public ResponseEntity<?> obtenerPorId(Integer id) {
		try {
			Pregunta pre = preguntaRepo.findById(id).orElse(null);

			if (pre != null && pre.getId() != null && pre.getId() > 0) {
				return ResponseEntity.ok(pre);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "NO SE PUDO OBTENER LA PREGUNTA CON ESE ID"));
		}
	}

}
