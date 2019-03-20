package com.anderltda.apistarwars.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.anderltda.apistarwars.model.Planeta;
import com.anderltda.apistarwars.repository.PlanetaRepository;
import com.anderltda.apistarwars.service.PlanetaService;

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
}
