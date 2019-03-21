package com.anderltda.apistarwars.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.anderltda.apistarwars.dto.PlanetaDTO;
import com.anderltda.apistarwars.dto.SwapiDTO;
import com.anderltda.apistarwars.model.Planeta;
import com.anderltda.apistarwars.repository.PlanetaRepository;
import com.anderltda.apistarwars.response.Response;
import com.anderltda.apistarwars.service.PlanetaService;
import com.anderltda.apistarwars.util.ApiStarWarsConstants;

/**
 * @author anderson.nascimento
 *
 */
@Service
public class PlanetaServiceImpl implements PlanetaService {

	private static final Logger log = LoggerFactory.getLogger(PlanetaServiceImpl.class);

	@Autowired
	private PlanetaRepository planetaRepository;

	@Override
	public Optional<Planeta> buscarPorId(Long id) {

		log.info("Buscando um planeta para o nome {}", id);

		return planetaRepository.findById(id);
	}

	@Override
	public Page<Planeta> buscarPorTodos(PageRequest pageRequest) {

		log.info("Buscando todos os planetas");

		return planetaRepository.findAll(pageRequest);
	}

	@Override
	public Optional<Planeta> buscarPorNome(String nome) {

		log.info("Buscando um planeta para o nome {}", nome);

		return Optional.ofNullable(planetaRepository.findByNome(nome));
	}

	@Override
	public Planeta persistir(Planeta planeta) {

		log.info("Persistindo planeta: {}", planeta.toString());

		return this.planetaRepository.save(planeta);
	}

	@Override
	public void remover(Long id) {

		log.info("Removendo o planeta ID {}", id);

		this.planetaRepository.deleteById(id);
	}

	@Override
	public PlanetaDTO buscarPlanetaAPIStarWars(PlanetaDTO target) {

		log.info("Buscando um planeta pela api - API pública do Star Wars (https://swapi.co/) pelo planeta: {}",
				target.getName());

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		headers.add("user-agent", ApiStarWarsConstants.HEADER);

		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		final ResponseEntity<SwapiDTO> swapiDTOs = restTemplate.exchange(ApiStarWarsConstants.URL_BUSCAR_PLANETA, HttpMethod.GET,
				entity, SwapiDTO.class, target.getName());

		PlanetaDTO source = swapiDTOs.getBody().getResults().stream().findAny().orElse(new PlanetaDTO());

		BeanUtils.copyProperties(source, target);

		return source;
	}
	
	@Override
	public Response<SwapiDTO> listarPlanetasAPIStarWars(Integer page) {
		
		Response<SwapiDTO> response = new Response<SwapiDTO>();

		try {
			
			log.info("Lista planetas da api - API pública do Star Wars (https://swapi.co/) page:", page);

			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();

			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			headers.add("user-agent", ApiStarWarsConstants.HEADER);

			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

			final ResponseEntity<SwapiDTO> swapiDTOs = restTemplate.exchange(ApiStarWarsConstants.URL_LISTAGEM_PLANETAS, HttpMethod.GET,
					entity, SwapiDTO.class, page);

			response.setData(swapiDTOs.getBody());
			
		} catch (Exception ex) {
			List<String> errors = new ArrayList<String>();
			errors.add(ex.getLocalizedMessage());
			response.setErrors(errors);
		}

		return response;
	}
}
