package br.com.lestcode.moviebattle.dto;

import lombok.Data;

@Data
public class RoundDto {

	private Integer id;
	private MovieDto movieA;
	private MovieDto movieB;
	
}
