package com.anderltda.apistarwars.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author anderson.nascimento
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SwapiDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6942104898749392206L;
	
	private String count;
	private String next;
	private String previous;
	private List<PlanetaDTO> results;

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public List<PlanetaDTO> getResults() {
		return results;
	}

	public void setResults(List<PlanetaDTO> results) {
		this.results = results;
	}

}
