package com.ipartek.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "preguntas_usuarios")
public class PreguntasUsuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="usuario_fk", nullable = false)
	private Usuario usuario_fk;
	
	@ManyToOne
	@JoinColumn(name="pregunta_fk", nullable = false)
	private Pregunta pregunta_fk;
	
	@Column(name="texto", nullable = false, length = 90 )
	private String respuesta;

}
