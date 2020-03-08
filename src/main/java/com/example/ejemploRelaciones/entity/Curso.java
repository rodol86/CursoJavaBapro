package com.example.ejemploRelaciones.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Curso {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String nombre;
	
	/*
	 * Dado que la relacion entre Curso y Alumno es bireccional y que es la tabla que no tiene Forein Key
	 * hay que usar el mappedBy
	 */
	@OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
	private List<Alumno> alumnos = new ArrayList<Alumno>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Alumno> getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(List<Alumno> alumnos) {
		this.alumnos = alumnos;
	}
	
	public Curso(String nombre, List<Alumno> alumnos) {
		super();
		this.nombre = nombre;
		this.alumnos = alumnos;
	}

	public Curso() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
			
}
