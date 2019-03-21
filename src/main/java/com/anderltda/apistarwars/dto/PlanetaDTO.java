package com.anderltda.apistarwars.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author anderson.nascimento
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanetaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6337803976036092235L;

	private Long id;
	private String name;
	private String climate;
	private String terrain;
	private List<String> films;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotEmpty(message = "Nome não pode ser vazio.")
	@Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 100 caracteres.")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// @NotEmpty(message = "Clima não pode ser vazio.")
	// @Length(min = 3, max = 200, message = "Clima deve conter entre 3 e 50
	// caracteres.")
	public String getClimate() {
		return climate;
	}

	public void setClimate(String climate) {
		this.climate = climate;
	}

	// @NotEmpty(message = "Terreno não pode ser vazio.")
	// @Length(min = 3, max = 200, message = "Terreno deve conter entre 3 e 50
	// caracteres.")
	public String getTerrain() {
		return terrain;
	}

	public void setTerrain(String terrain) {
		this.terrain = terrain;
	}

	public List<String> getFilms() {
		return films;
	}

	public void setFilms(List<String> films) {
		this.films = films;
	}

	public Integer getAparicoes() {
		return (!films.isEmpty() ? films.size() : 0);
	}

	@Override
	public String toString() {
		return "PlanetaDTO [id=" + id + ", name=" + name + ", climate=" + climate + ", terrain=" + terrain + ", films="
				+ films + "]";
	}

}
