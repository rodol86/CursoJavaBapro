package com.example.ejemploRelaciones.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Alumno {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String nombre;
	
	@Column
	private String apellido;
	
	/*
	 * Dado que un curso tiene varios alumos y que definimos que la relacion es bidireccional.
	 * Quien tenga el 'Many' es la tabla quien contiene la Foreign Key, que en este caso seria Curso.
	 * Y especificamos esa relacion con el JoinColumn
	 */
	@ManyToOne
    @JoinColumn(name="alumnos")
	private Curso curso;

	public Long getId() {
		return id;
	}

	public void setIdAlumno(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Alumno(String nombre, String apellido, Curso curso) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.curso = curso;
	}

	public Alumno() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
}
