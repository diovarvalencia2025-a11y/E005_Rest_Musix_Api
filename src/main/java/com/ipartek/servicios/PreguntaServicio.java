package com.ipartek.servicios;

import org.springframework.http.ResponseEntity;

public interface PreguntaServicio {
	
	public ResponseEntity<?> obtenerTodas();
	public ResponseEntity<?> obtenerPorId(Integer id);
	//public ResponseEntity<?> insertar(Pregunta pre);
	//public ResponseEntity<?> modificar(Pregunta pre);
	//public ResponseEntity<?> borrar(Integer id);
	
	
	

}
