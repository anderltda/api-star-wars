package com.anderltda.apistarwars.dto;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

/**
 * @author anderson.nascimento
 *
 */
public class PlanetaDTO {
	
	private Long id;
	private String nome;
	private String clima;
	private String terreno;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@NotEmpty(message = "Nome não pode ser vazio.")
	@Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 100 caracteres.")
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@NotEmpty(message = "Clima não pode ser vazio.")
	@Length(min = 3, max = 50, message = "Clima deve conter entre 3 e 50 caracteres.")
	public String getClima() {
		return clima;
	}
	
	public void setClima(String clima) {
		this.clima = clima;
	}
	
	@NotEmpty(message = "Terreno não pode ser vazio.")
	@Length(min = 3, max = 50, message = "Terreno deve conter entre 3 e 50 caracteres.")
	public String getTerreno() {
		return terreno;
	}
	
	public void setTerreno(String terreno) {
		this.terreno = terreno;
	}
	
	@Override
	public String toString() {
		return "Planeta [id = " + id + ", nome = " + nome + ", clima = " + clima + ", terreno = " + terreno + "]";
	}
}
