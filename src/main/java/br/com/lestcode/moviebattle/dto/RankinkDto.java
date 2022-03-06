package br.com.lestcode.moviebattle.dto;

import lombok.Getter;

@Getter
public class RankinkDto {

	private Integer position;
	private String userName;
	private Float score;
	
	public RankinkDto(Integer position, String userName, Float score) {
		super();
		this.position = position;
		this.userName = userName;
		this.score = score;
	}
	
}
