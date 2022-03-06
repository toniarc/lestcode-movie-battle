package br.com.lestcode.moviebattle.dto;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Data;

@Data
public class MovieDto {

	@Id
	@Column(name = "imdb_id")
	private String imdbId;
	private String name;
	private String year;
	
}
