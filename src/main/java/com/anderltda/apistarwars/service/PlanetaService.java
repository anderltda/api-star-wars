package com.anderltda.apistarwars.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.anderltda.apistarwars.dto.PlanetaDTO;
import com.anderltda.apistarwars.dto.SwapiDTO;
import com.anderltda.apistarwars.model.Planeta;
import com.anderltda.apistarwars.response.Response;

/**
 * @author anderson.nascimento
 *
 */
public interface PlanetaService {
	
	/**
	 * Retorna um planeta dado um ID.
	 * 
	 * @param nome
	 * @return Optional<Empresa>
	 */
	Optional<Planeta> buscarPorId(Long id);
	
	/**
	 * Retorna uma lista paginada de planetas.
	 * 
	 * @param pageRequest
	 * @return Page<Planeta>
	 */
	Page<Planeta> buscarPorTodos(PageRequest pageRequest);
	
	/**
	 * Retorna um planeta dado um Nome.
	 * 
	 * @param nome
	 * @return Optional<Empresa>
	 */
	Optional<Planeta> buscarPorNome(String nome);

	/**
	 * Cadastra um novo planeta na base de dados.
	 * 
	 * @param planeta
	 * @return Planeta
	 */
	Planeta persistir(Planeta planeta);
	
	/**
	 * Remove um planeta da base de dados.
	 * 
	 * @param id
	 */
	void remover(Long id);
	
	/**
	 * Responsável por buscar um planeta pelo na api - API pública do Star Wars
	 * (https://swapi.co/)
	 * 
	 * @param target
	 * @return PlanetaDTO
	 */
	PlanetaDTO buscarPlanetaAPIStarWars(PlanetaDTO target);
	
	/**
	 * Responsável por lista planetas da api - API pública do Star Wars
	 * (https://swapi.co/)
	 * 
	 * @param page
	 * @return Response<SwapiDTO>
	 */
	Response<SwapiDTO> listarPlanetasAPIStarWars(Integer page);
}
