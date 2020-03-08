package com.example.ejemploRelaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.ejemploRelaciones.repository.AlumnoRepository;

@SpringBootApplication
public class EjemploRelacionesApplication {

	@Autowired
	AlumnoRepository alumnoRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(EjemploRelacionesApplication.class, args);
	}

}
