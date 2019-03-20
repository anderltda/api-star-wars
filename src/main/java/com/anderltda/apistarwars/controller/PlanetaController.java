package com.anderltda.apistarwars.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
import org.springframework.web.client.RestTemplate;

import com.anderltda.apistarwars.dto.PlanetaDTO;
import com.anderltda.apistarwars.model.Planeta;
import com.anderltda.apistarwars.model.Planets;
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
	
	private static final String HEADER_ = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";
	
	private static final String URL = "https://swapi.co/api/planets/{number}/";

	@Autowired
	private PlanetaService planetaService;
	
	@Value("${paginacao.qtd_por_pagina}")
	private int size;
		
	/**
	 * Funcionalidade - Adicionar um planeta (com nome, clima e terreno)
	 * 
	 * @param planetaDTO
	 * @param result
	 * @return ResponseEntity<Response<PlanetaDTO>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity<Response<PlanetaDTO>> cadastrar(@Valid @RequestBody PlanetaDTO planetaDTO, BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Funcionalidade - Adicionar um planeta (com nome, clima e terreno): {}", planetaDTO.toString());
		
		Response<PlanetaDTO> response = new Response<PlanetaDTO>();

		validarDadosExistentes(planetaDTO, result);
		
		Planeta planeta = this.converterDtoParaPlaneta(planetaDTO);

		if (result.hasErrors()) {
			
			log.error("Erro validando dados de cadastro do planeta: {}", result.getAllErrors());
			
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}

		this.planetaService.persistir(planeta);	
		
		response.setData(this.converterPlanetaDTO(planeta));
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Funcionalidade - Listar planetas do banco de dados
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping(value = "/all")
	public ResponseEntity<Response<Page<PlanetaDTO>>> buscarAll(@RequestParam(value = "pag", defaultValue = "0") int page) {

		log.info("Funcionalidade - Listar planetas do banco de dados");
		
		Response<Page<PlanetaDTO>> response = new Response<Page<PlanetaDTO>>();
		
		PageRequest pageRequest = PageRequest.of(page, size);
		
		Page<Planeta> planetas = this.planetaService.buscarPorTodos(pageRequest);
		
		Page<PlanetaDTO> planetaDTOs = planetas.map(ps -> this.converterPlanetaDTO(ps));

		response.setData(planetaDTOs);

		return ResponseEntity.ok(response);
	}
	
	/**
	 * Funcionalidade - Buscar por nome no banco de dados
	 * 
	 * @param nome
	 * @return ResponseEntity<Response<PlanetaDTO>>
	 */
	@GetMapping(value = "/nome/{nome}")
	public ResponseEntity<Response<PlanetaDTO>> cadastrar(@PathVariable("nome") String nome) throws NoSuchAlgorithmException {
				
		log.info("Funcionalidade - Buscar por nome no banco de dados: {}", nome);
		
		Response<PlanetaDTO> response = new Response<PlanetaDTO>();
		
		Optional<Planeta> planeta = this.planetaService.buscarPorNome(nome);
				
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

		Optional<Planeta> planeta = this.planetaService.buscarPorId(id);
		
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
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		
		log.info("Funcionalidade - Remover planeta: {}", id);
		
		Response<String> response = new Response<String>();
		
		Optional<Planeta> planeta = this.planetaService.buscarPorId(id);

		if (!planeta.isPresent()) {
			
			log.info("Erro ao remover devido ao planeta ID: {} ser inválido.", id);
			
			response.getErrors().add("Erro ao remover planeta. Registro não encontrado para o id " + id);
			
			return ResponseEntity.badRequest().body(response);
		}

		this.planetaService.remover(id);
		
		return ResponseEntity.ok(new Response<String>());
	}

	/**
	 * Verifica se o planeta já existe na base de dados.
	 * 
	 * @param planetaDTO
	 * @param result
	 */
	private void validarDadosExistentes(PlanetaDTO planetaDTO, BindingResult result) {
		this.planetaService.buscarPorNome(planetaDTO.getNome())
				.ifPresent(emp -> result.addError(new ObjectError("planeta", "Planeta já existente.")));
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
		planetaDTO.setNome(planeta.getNome());
		planetaDTO.setClima(planeta.getClima());
		planetaDTO.setTerreno(planeta.getTerreno());
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
		planeta.setNome(planetaDTO.getNome());
		planeta.setClima(planetaDTO.getClima());
		planeta.setTerreno(planetaDTO.getTerreno());
		return planeta;
	}
	
	
	public static void main(String args[]) {
        
		RestTemplate restTemplate = new RestTemplate();        
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", HEADER_);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        final ResponseEntity<Planets> planets = restTemplate.exchange(URL, HttpMethod.GET, entity, Planets.class, "1");

        log.debug(planets.getBody().toString());
        
    }
	
}
