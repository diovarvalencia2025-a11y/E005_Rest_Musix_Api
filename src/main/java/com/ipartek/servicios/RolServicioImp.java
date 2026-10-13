package com.ipartek.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ipartek.modelo.Rol;
import com.ipartek.pojos.MsgEstado;
import com.ipartek.repositorio.RolRepositorio;

@Service
public class RolServicioImp implements RolServicio {

	@Autowired
	private RolRepositorio rolRepo;

	@Override
	public ResponseEntity<?> obtenerTodos() {
		try {
			List<Rol> lista = rolRepo.findAll();

			if (lista != null && !lista.isEmpty()) {
				return ResponseEntity.ok(lista);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "NO SE PUDO OBTENER TODOS LOS ROLES"));
		}
	}

	@Override
	public ResponseEntity<?> obtenerPorId(Integer id) {
		try {
			Rol rol = rolRepo.findById(id).orElse(null);

			if (rol != null && rol.getId() != null && rol.getId() > 0) {
				return ResponseEntity.ok(rol);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError()
					.body(new MsgEstado("500", "NO SE PUDO OBTENER EL ROL CON ESE ID"));
		}
	}

}
