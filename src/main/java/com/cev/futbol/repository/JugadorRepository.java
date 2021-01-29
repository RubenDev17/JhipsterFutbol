package com.cev.futbol.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cev.futbol.domain.Jugador;

/**
 * Spring Data  repository for the Jugador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {
	//Buscar jugador por nombre Y edad
	List<Jugador> findByNombreAndEdad(String nombre, int edad);
	//Buscar jugador por nombre O edad (RequestParam)
	List<Jugador> findByNombreOrEdad(String nombre, Optional<Integer> edad);
	//Buscar jugador con edad Entre
	List<Jugador> findByEdadBetween(int edadUno, int edadDos);
}
