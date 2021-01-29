package com.cev.futbol.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cev.futbol.domain.Equipo;

/**
 * Spring Data  repository for the Equipo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {
	//Buscar todos los equipos ordenados por nombre
	List<Equipo> findAllByOrderByNombre();
	//Recuperamos los alumnos de un nombre en concreto
	List<Equipo> findByNombre(String nombre);
	//Recuperamos los alumnos cuyo nombre contenga "algo"
	List<Equipo> findByNombreContaining(String nombre);
	//Recuperamos los equipos que jueguen en una liga con nombre X
	List<Equipo> findByLiga_nombreEquals(String nombre);
	//Recuperamos equipos fundados despues de "X"
	List<Equipo> findByFechaDeFundacionGreaterThan(Instant fecha);
	//Recuperamos los equipos de una liga "x" fundados después de "x"
	List<Equipo> findByLiga_nombreAndFechaDeFundacionGreaterThan(String nombre, Instant fecha);
}
