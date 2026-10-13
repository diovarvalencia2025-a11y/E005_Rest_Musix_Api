package com.ipartek.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="user", nullable = false, length = 45, unique = true)
	private String user;
	
	@Column(name="pass", nullable = false, length = 64)
	private String pass;
	
	@Column(name="salt", nullable = false, length = 32)
	private String salt;
	
	@ManyToOne
	@JoinColumn(name="rol_fk", nullable = false)
	private Rol rol_fk;
	
	@Column(name="foto", nullable = false, length = 255)
	private String foto;
	
	@Column(name="intentos", nullable = false, columnDefinition = "INT UNSIGNED DEFAULT 0")
	@Min(value = 0, message = "Los intentos no pueden ser negativos")
	private Integer intentos;
	
}
