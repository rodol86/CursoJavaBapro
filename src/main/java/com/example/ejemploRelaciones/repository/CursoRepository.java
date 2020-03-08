package com.example.ejemploRelaciones.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ejemploRelaciones.entity.Curso;

@Repository
public interface CursoRepository extends CrudRepository<Curso, Long> {

	// Ejemplo del uso de Query
	@Query("SELECT curso FROM Curso curso  WHERE curso.nombre=(:nombre)")
    Curso buscarPorNombre(@Param("nombre") String nombre);

}
