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
@Table(name = "canciones")
public class Cancion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "titulo", nullable = false, length = 90)
	private String titulo;

	@Column(name = "enlace", nullable = false, length = 90)
	private String enlace;

	@Column(name = "duracion", nullable = false, columnDefinition = "INT UNSIGNED")
	private Integer duracion;

	@ManyToOne
	@JoinColumn(name = "estilo_fk", nullable = false, referencedColumnName = "id")
	private Estilo estilo_fk;

	@Column(name = "descripcion", nullable = false, columnDefinition = "Text")
	private String descripcion;

	@Column(name = "reportada", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Boolean reportada;

	@ManyToOne
	@JoinColumn(name = "usuario_fk", referencedColumnName = "id", nullable = false)
	private Usuario usuario_fk;

	public Estilo getEstilo() {
		return estilo_fk;
	}

	public void setEstilo(Estilo estilo) {
		this.estilo_fk = estilo;
	}

	public Usuario getUsuario() {
		return usuario_fk;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario_fk = usuario;
	}
}
