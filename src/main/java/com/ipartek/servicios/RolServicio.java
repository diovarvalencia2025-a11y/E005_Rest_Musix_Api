package com.ipartek.servicios;

import org.springframework.http.ResponseEntity;

public interface RolServicio {
	
	public ResponseEntity<?> obtenerTodos();
	public ResponseEntity<?> obtenerPorId(Integer id);
//	public ResponseEntity<?> insertar(Rol rol);
//	public ResponseEntity<?> modificar(Rol rol);
//	public ResponseEntity<?> borrar(Integer id);
	
	
	

}
