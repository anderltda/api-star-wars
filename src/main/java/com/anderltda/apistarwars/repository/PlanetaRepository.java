package com.anderltda.apistarwars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.anderltda.apistarwars.model.Planeta;

/**
 * @author anderson.nascimento
 *
 */
public interface PlanetaRepository extends JpaRepository<Planeta, Long> {

	@Transactional(readOnly = true)
	Planeta findByNome(String nome);
}
