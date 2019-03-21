package com.anderltda.apistarwars.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anderltda.apistarwars.dto.PlanetaDTO;
import com.anderltda.apistarwars.dto.SwapiDTO;
import com.anderltda.apistarwars.model.Planeta;
import com.anderltda.apistarwars.response.Response;
import com.anderltda.apistarwars.service.PlanetaService;

/**
 * @author anderson.nascimento
 *
 */
@RestController
@RequestMapping("/api/planeta")
@CrossOrigin(origins = "*")
public class PlanetaController {

	private static final Logger log = LoggerFactory.getLogger(PlanetaController.class);

	@Autowired
	private PlanetaService planetaService;

	@Value("${paginacao.qtd_por_pagina}")
	private int size;
		
	/**
	 * Método responsável pelos serviços
	 * 
	 * @return PlanetaService
	 */
	public PlanetaService getPlanetaService() {
		return planetaService;
	}

	/**
	 * Funcionalidade - Adicionar um planeta (com nome, clima e terreno) - Informe
	 * apenas o nome do planeta existente, que o serviço irá cadastrar os demais
	 * campos
	 * 
	 * @param planetaDTO
	 * @param result
	 * @return ResponseEntity<Response<PlanetaDTO>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping(value = "/cadastrar")
	public ResponseEntity<Response<PlanetaDTO>> cadastrar(@Valid @RequestBody PlanetaDTO planetaDTO,
			BindingResult result) throws NoSuchAlgorithmException {

		log.info("Funcionalidade - Adicionar um planeta (com nome, clima e terreno): {}", planetaDTO.toString());

		Response<PlanetaDTO> response = new Response<PlanetaDTO>();

		validarDadosExistentes(planetaDTO, result);

		if (result.hasErrors()) {

			log.error("Erro validando dados de cadastro do planeta: {}", result.getAllErrors());

			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

			return ResponseEntity.badRequest().body(response);
		}

		Planeta planeta = this.converterDtoParaPlaneta(planetaDTO);

		getPlanetaService().persistir(planeta);

		response.setData(this.converterPlanetaDTO(planeta));

		return ResponseEntity.ok(response);
	}

	/**
	 * Funcionalidade - Listar planetas do banco de dados
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping(value = "/all/db")
	public ResponseEntity<Response<Page<PlanetaDTO>>> buscarDbAll(@RequestParam(value = "pag", defaultValue = "0") int page) {

		log.info("Funcionalidade - Listar planetas do banco de dados");

		Response<Page<PlanetaDTO>> response = new Response<Page<PlanetaDTO>>();

		PageRequest pageRequest = PageRequest.of(page, size);

		Page<Planeta> planetas = getPlanetaService().buscarPorTodos(pageRequest);

		Page<PlanetaDTO> planetaDTOs = planetas.map(ps -> this.converterPlanetaDTO(ps));

		response.setData(planetaDTOs);

		return ResponseEntity.ok(response);
	}
	
	
	/**
	 * Funcionalidade - Listar planetas da API do Star Wars
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping(value = "/all/api")
	public ResponseEntity<Response<SwapiDTO>> buscarApiAll(@RequestParam(value = "page", defaultValue = "0") int page) {

		log.info("Funcionalidade - Listar planetas da api Star Wars page: {}", page);

		Response<SwapiDTO> response = getPlanetaService().listarPlanetasAPIStarWars(page);

		return ResponseEntity.ok(response);
	}

	/**
	 * Funcionalidade - Buscar por nome no banco de dados
	 * 
	 * @param nome
	 * @return ResponseEntity<Response<PlanetaDTO>>
	 */
	@GetMapping(value = "/nome/{nome}")
	public ResponseEntity<Response<PlanetaDTO>> buscarPorNome(@PathVariable("nome") String nome) throws NoSuchAlgorithmException {

		log.info("Funcionalidade - Buscar por nome no banco de dados: {}", nome);

		Response<PlanetaDTO> response = new Response<PlanetaDTO>();

		Optional<Planeta> planeta = getPlanetaService().buscarPorNome(nome);

		if (!planeta.isPresent()) {

			log.error("Planeta não encontrado pelo nome: {} ", nome);

			response.getErrors().add("Planeta não encontrado");

			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.converterPlanetaDTO(planeta.get()));

		return ResponseEntity.ok(response);
	}

	/**
	 * Funcionalidade - Buscar por ID no banco de dados
	 * 
	 * @param id
	 * @return ResponseEntity<Response<PlanetaDTO>>
	 */
	@GetMapping(value = "/id/{id}")
	public ResponseEntity<Response<PlanetaDTO>> buscarId(@PathVariable("id") Long id) {

		log.info("Funcionalidade - Buscar por ID no banco de dados: {}", id);

		Response<PlanetaDTO> response = new Response<PlanetaDTO>();

		Optional<Planeta> planeta = getPlanetaService().buscarPorId(id);

		if (!planeta.isPresent()) {

			log.error("Planeta não encontrado pelo ID: {} ", id);

			response.getErrors().add("Planeta não encontrado");

			return ResponseEntity.badRequest().body(response);
		}

		Optional<PlanetaDTO> planetaDTO = planeta.map(p -> this.converterPlanetaDTO(p));

		response.setData(planetaDTO.get());

		return ResponseEntity.ok(response);
	}

	/**
	 * Funcionalidade - Remover planeta
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/remover/{id}")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {

		log.info("Funcionalidade - Remover planeta: {}", id);

		Response<String> response = new Response<String>();

		Optional<Planeta> planeta = getPlanetaService().buscarPorId(id);

		if (!planeta.isPresent()) {

			log.info("Erro ao remover devido ao planeta ID: {} ser inválido.", id);

			response.getErrors().add("Erro ao remover planeta. Registro não encontrado para o id " + id);

			return ResponseEntity.badRequest().body(response);
		}

		getPlanetaService().remover(id);

		return ResponseEntity.ok(new Response<String>());
	}

	/**
	 * Verifica se o planeta já existe na base de dados.
	 * 
	 * @param target
	 * @param result
	 */
	private void validarDadosExistentes(PlanetaDTO target, BindingResult result) {

		getPlanetaService().buscarPorNome(target.getName())
				.ifPresent(emp -> result.addError(new ObjectError("planeta", "Planeta já existente.")));

		Optional.of(getPlanetaService().buscarPlanetaAPIStarWars(target)).filter(x -> x.getName() == null).ifPresent(x -> result
				.addError(new ObjectError("planeta", "Planeta não existente na API Star Wars (https://swapi.co/).")));
	}

	/**
	 * Popula um DTO com os dados do planeta.
	 * 
	 * @param planeta
	 * @return PlanetaDTO
	 */
	private PlanetaDTO converterPlanetaDTO(Planeta planeta) {

		PlanetaDTO planetaDTO = new PlanetaDTO();

		planetaDTO.setId(planeta.getId());

		planetaDTO.setName(planeta.getNome());

		planetaDTO.setClimate(planeta.getClima());

		planetaDTO.setTerrain(planeta.getTerreno());

		planetaDTO.setFilms(planeta.getFilms_());

		return planetaDTO;
	}

	/**
	 * Converte os dados do DTO para planeta.
	 * 
	 * @param planetaDTO
	 * @return Planeta
	 */
	private Planeta converterDtoParaPlaneta(PlanetaDTO planetaDTO) {

		Planeta planeta = new Planeta();

		planeta.setNome(planetaDTO.getName());

		planeta.setClima(planetaDTO.getClimate());

		planeta.setTerreno(planetaDTO.getTerrain());

		planeta.setFilms(planetaDTO.getFilms().toString());

		return planeta;
	}
}
