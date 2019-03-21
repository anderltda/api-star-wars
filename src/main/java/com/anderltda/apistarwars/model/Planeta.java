package com.anderltda.apistarwars.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author anderson.nascimento
 *
 */
@Entity
@Table(name = "planeta")
public class Planeta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6115168977821393390L;

	private Long id;
	private String nome;
	private String clima;
	private String terreno;
	private Date dataCriacao;
	private Date dataAtualizacao;
	private String films;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "nome", nullable = false, length = 100)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "clima", nullable = false, length = 50)
	public String getClima() {
		return clima;
	}

	public void setClima(String clima) {
		this.clima = clima;
	}

	@Column(name = "terreno", nullable = false, length = 50)
	public String getTerreno() {
		return terreno;
	}

	public void setTerreno(String terreno) {
		this.terreno = terreno;
	}

	@Column(name = "data_criacao", nullable = false)
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Column(name = "data_atualizacao", nullable = true)
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}
	
	@Column(name = "filmes", nullable = false, length = 200)
	public String getFilms() {
		return films;
	}
	
	public void setFilms(String films) {
		this.films = films.replaceAll("[-)\\[\\\\\\]\\\\?.!%$¨@&>#(_+=^*]*","");
	}
	
	@Transient
	public List<String> getFilms_() {
		return !films.isEmpty() ? Arrays.asList(films.split(",")) : new ArrayList<String>();
	}


	@PreUpdate
	public void preUpdate() {
		dataAtualizacao = new Date();
	}

	@PrePersist
	public void prePersist() {
		final Date atual = new Date();
		dataCriacao = (dataCriacao == null ? atual : dataCriacao);
		dataAtualizacao = atual;
	}

	@Override
	public String toString() {
		return "Planeta [id=" + id + ", nome=" + nome + ", clima=" + clima + ", terreno=" + terreno + ", dataCriacao="
				+ dataCriacao + ", dataAtualizacao=" + dataAtualizacao + ", films=" + films + "]";
	}

	
}
