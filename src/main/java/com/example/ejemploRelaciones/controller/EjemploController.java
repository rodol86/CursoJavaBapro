package com.example.ejemploRelaciones.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.ejemploRelaciones.entity.Alumno;
import com.example.ejemploRelaciones.entity.Curso;
import com.example.ejemploRelaciones.repository.AlumnoRepository;
import com.example.ejemploRelaciones.repository.CursoRepository;

@Controller
public class EjemploController {

	@Autowired
	CursoRepository cursoRepository;

	@Autowired
	AlumnoRepository alumnoRepository;

	/**
	 * Dado que ir a URL "/index" no postea informacion usamos @RequestMapping
	 * Pero dado que el formulario que hay en esa pagina son los datos para que nosotros
	 * instanciemos nuestro objeto 'curso' necesitamos utilizar @ModelAttribute 
	 * para que cuando en index.html usemos th:object="${curso} utilice un objecto 'curso',
	 * y BindingResult para que cuando se haga click en el boton de tipo submit se envie
	 * la informacion del formulario desde el html al controller
	 * 
	 * @param curso
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping("/index")
	public String index(@ModelAttribute Curso curso, BindingResult bindingResult) {
		return "index";
	}
	
	
	/**
	 * La URL '/crearCurso' es quien en base a la informacion cargada en el pagina index.html
	 * creara el registro en la tabla, para esto envia el objeto mediante un POST
	 * que se ejecuto al hacer click en el boton submit. Es por esto que usamos @PostMapping
	 * La informacion del formulario, que en este caso es solo el nombre del curso, esta 
	 * asociado al objecto 'curso' mediante el uso @ModelAttribute.
	 * 
	 * En la pagina agregarAlumnos ademas de necesitar la informacion del curso necesitamos
	 * una lista de todos los alumnos a los cuales se les puede asignar dicho curso. Osea, estariamos
	 * trabajando con 2 objectos al mismo tiempo, por un lado necesitamos el objecto curso para mostrar 
	 * el nombre del curso y saber cual es su ID y por el otro un listado de Alumnos. Pero el tag <form>
	 * solo puede manejar un objeto.
	 * Es por eso que vamos a utlizar el Model. Para esto usamos el 'model.addAttribute(clave, valor)' que funciona
	 * como "clave/valor" donde la primera parte es la clave que utilizamos en el html y la segundo es el valor.
	 * Cada 'model.addAttribute' puede guardar distintos tipos de datos.
	 * 
	 * @param curso
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@PostMapping("/crearCurso")
	public String crearCurso(@ModelAttribute Curso curso, BindingResult bindingResult, Model model) {
		// En base a la informacion del formulario, que es un objeto del tipo Curso la guardo en la base
		// IMPORTANTE: recordar el que metodo save devuelve el objeto guardado en la base
		// Necesitamos hacer asi para obtener el Id que se le genero automaticamente al Curso
		model.addAttribute("curso", cursoRepository.save(curso));
		
		// Crea en la base algunos alumnos para poder agregar al Curso
		cargaInicial();
		
		// El metodo findAll devuelve un tipo de dato Iterable, por lo que
		// creo una ArrayList para guarda cada valor
		List<Alumno> listadoAlumnos = new ArrayList<>();
	    for (Alumno alumno : alumnoRepository.findAll()) {
	    	listadoAlumnos.add(alumno);
	    }
		
	    // Paso a la vista el listado de todos los alumnos como un parametro 'listadoAlumnos'
		model.addAttribute("listadoAlumnos", listadoAlumnos);
		
		return "agregarAlumnos";
	}
	
	/**
	 * Cuando se definimos '/agregarAlumno/{idCurso}/{idAlumno}' lo que estamos haciendo es 
	 * especificar que dentro de la URL van a ver las variable idCurso y idAlumno
	 * y para poder utilizarlas usamos el @PathVariable
	 * 
	 * @param idCurso
	 * @param idAlumno
	 * @param model
	 * @return
	 */
	@GetMapping("/agregarAlumno/{idCurso}/{idAlumno}")
	public String agregarAlumno(@PathVariable("idCurso") long idCurso, @PathVariable("idAlumno") long idAlumno, Model model) {
		// Busco el curso en base al idCurso que recibo como parametro en la URL
		Curso curso = cursoRepository.findById(idCurso).get();

		// Busco el alumno en base al idAlumno que recibo como parametro en la URL
		Alumno alumno = alumnoRepository.findById(idAlumno).get();
		
		// Asigno el curso al alumno
		alumno.setCurso(curso);

		// Guardo el cambio en la base de dato
		alumnoRepository.save(alumno);
		
		// Busco todos los alumnos que no tienen curso o que no tiene este curso
		// y los paso a la vista
		List<Alumno> listadoAlumnos = new ArrayList<>();
	    for (Alumno a : alumnoRepository.findAll()) {
	    	if(a.getCurso() == null || a.getCurso().getId() != idCurso) {
	    		listadoAlumnos.add(a);
	    	}
	    }
		
	    // Paso a la vista el listado de todos los alumnos como un parametro 'listadoAlumnos'
		model.addAttribute("listadoAlumnos", listadoAlumnos);
	    
	    // Paso el curso a la vista para que tenga acceso a todos los datos de la tabla
	    model.addAttribute("curso", curso);
		
		
		return "agregarAlumnos";
	}
	
	@GetMapping("/detalleCurso/{idCurso}")
	public String agregarAlumnos(@PathVariable("idCurso") long idCurso, Model model) {
		// Busco el curso en base al idCurso que recibo como parametro en la URL
		Curso curso = cursoRepository.findById(idCurso).get();
		
		// Paso el curso a la vista para que tenga acceso a todos los datos de la tabla
	    model.addAttribute("curso", curso);
		
		return "detalleCurso";
	}
	
	@GetMapping("/eliminarAlumno/{idCurso}/{idAlumno}")
	public String eliminarAlumnos(@PathVariable("idCurso") long idCurso, @PathVariable("idAlumno") long idAlumno, Model model) {
		// Busco el alumno en base al idAlumno que recibo como parametro en la URL
		Alumno a = alumnoRepository.findById(idAlumno).get();
		
		// Dado que la relacion es OnetoMany para Curso y ManyToOne para Alumno, necesito eliminar la relacion desde
		// la entidad Alumno.
		a.setCurso(null);
		
		// Guardo el cambio en la tabla Alumno
		alumnoRepository.save(a);
	    
		// Una vez que borramos el curso de la tabla Alumno, debemos volver a obtener los datos del curso para
		// poder reflejar el cambio
		Curso curso = cursoRepository.findById(idCurso).get();
		
		// Paso el curso a la vista para que tenga acceso a todos los datos de la tabla
	    model.addAttribute("curso", curso);
		
		return "detalleCurso";
	}

	@GetMapping("/cargarAlumnos/{idCurso}")
	public String agregarAlumno(@PathVariable("idCurso") long idCurso, Model model) {
		// Busco el curso en base al idCurso que recibo como parametro en la URL
		Curso curso = cursoRepository.findById(idCurso).get();

		// Busco todos los alumnos que no tienen curso o que no tiene este curso
		// y los paso a la vista
		List<Alumno> listadoAlumnos = new ArrayList<>();
	    for (Alumno a : alumnoRepository.findAll()) {
	    	if(a.getCurso() == null || a.getCurso().getId() != idCurso) {
	    		listadoAlumnos.add(a);
	    	}
	    }
		
	    // Paso a la vista el listado de todos los alumnos como un parametro 'listadoAlumnos'
		model.addAttribute("listadoAlumnos", listadoAlumnos);
		
	    // Paso el curso a la vista para que tenga acceso a todos los datos de la tabla
	    model.addAttribute("curso", curso);
		
	    
		return "agregarAlumnos";
	}
	
	private void cargaInicial() {
		alumnoRepository.save(new Alumno("Ariel", "Besomi", null));
		alumnoRepository.save(new Alumno("Silvio", "Heiler", null));
		alumnoRepository.save(new Alumno("Rodolfo", "Zavala", null));
	}
}
