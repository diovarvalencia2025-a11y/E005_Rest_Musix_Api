package com.ipartek.servicios;

import org.springframework.http.ResponseEntity;

public interface EstiloServicio {
	
	public ResponseEntity<?> obtenerTodos();
	public ResponseEntity<?> obtenerPorId(Integer id);
	//public ResponseEntity<?> insertar(Estilo est);
	//public ResponseEntity<?> modificar(Estilo est);
	//public ResponseEntity<?> borrar(Integer id);
	
	
	

}
